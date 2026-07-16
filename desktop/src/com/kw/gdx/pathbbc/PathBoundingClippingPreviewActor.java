package com.kw.gdx.pathbbc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kw.gdx.asset.Asset;

/**
 * 简单预览 Actor。
 *
 * 作用：在 LibGDX Stage 里画 path / bounding box / clipping 的线和点。
 * 真正项目里你可以把 draw 部分接到自己的 viewport renderer。
 */
public class PathBoundingClippingPreviewActor extends Actor {
    private final PathBoundingClippingEditor editor;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final Vector2 tmp = new Vector2();
    private final Vector2 tmp2 = new Vector2();

    public boolean drawNames = true;
    public boolean drawHud = true;
    public boolean drawTransformPreview = true;

    public PathBoundingClippingPreviewActor(PathBoundingClippingEditor editor) {
        this.editor = editor;
        this.shapeRenderer = new ShapeRenderer();
        this.font = Asset.getAsset().loadBitFont();
        setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        drawGrid();
        for (int i = 0; i < editor.attachments.size; i++) {
            EditableAttachment attachment = editor.attachments.get(i);
            drawAttachmentLines(attachment, attachment == editor.selectedAttachment);
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < editor.attachments.size; i++) {
            EditableAttachment attachment = editor.attachments.get(i);
            drawAttachmentPoints(attachment, attachment == editor.selectedAttachment);
        }
        shapeRenderer.end();

        batch.begin();
        if (drawNames) drawLabels(batch);
        if (drawHud) drawHud(batch);
    }

    private void drawGrid() {
        shapeRenderer.setColor(0.18f, 0.18f, 0.18f, 1f);
        float w = getWidth();
        float h = getHeight();
        for (int x = 0; x <= w; x += 50) {
            shapeRenderer.line(getX() + x, getY(), getX() + x, getY() + h);
        }
        for (int y = 0; y <= h; y += 50) {
            shapeRenderer.line(getX(), getY() + y, getX() + w, getY() + y);
        }
    }

    private void drawAttachmentLines(EditableAttachment attachment, boolean selected) {
        if (attachment.vertexCount() < 2) return;

        switch (attachment.kind) {
            case path:
                shapeRenderer.setColor(selected ? Color.CYAN : Color.SKY);
                break;
            case boundingBox:
                shapeRenderer.setColor(selected ? Color.YELLOW : Color.GOLD);
                break;
            case clipping:
                shapeRenderer.setColor(selected ? Color.RED : Color.SCARLET);
                break;
        }

        int lineCount = attachment.closed ? attachment.vertexCount() : attachment.vertexCount() - 1;
        for (int i = 0; i < lineCount; i++) {
            Vector2 a = worldTo(attachment, i, tmp);
            Vector2 b = worldTo(attachment, (i + 1) % attachment.vertexCount(), tmp2);
            shapeRenderer.line(a.x, a.y, b.x, b.y);
        }

        if (drawTransformPreview) {
            Vector2 origin = tmp.set(attachment.transform.x, attachment.transform.y);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.circle(origin.x, origin.y, 4f, 12);
        }
    }

    private void drawAttachmentPoints(EditableAttachment attachment, boolean selected) {
        for (int i = 0; i < attachment.vertexCount(); i++) {
            Vector2 p = worldTo(attachment, i, tmp);
            boolean selectedPoint = selected && editor.selectedVertex == i;
            if (selectedPoint) {
                shapeRenderer.setColor(Color.WHITE);
                shapeRenderer.circle(p.x, p.y, 7f, 16);
            }

            switch (attachment.kind) {
                case path:
                    shapeRenderer.setColor(selectedPoint ? Color.CYAN : Color.SKY);
                    break;
                case boundingBox:
                    shapeRenderer.setColor(selectedPoint ? Color.YELLOW : Color.GOLD);
                    break;
                case clipping:
                    shapeRenderer.setColor(selectedPoint ? Color.RED : Color.SCARLET);
                    break;
            }
            shapeRenderer.circle(p.x, p.y, selected ? 5f : 4f, 16);
        }
    }

    private void drawLabels(Batch batch) {
        for (int i = 0; i < editor.attachments.size; i++) {
            EditableAttachment attachment = editor.attachments.get(i);
            if (attachment.vertexCount() == 0) continue;
            Vector2 p = worldTo(attachment, 0, tmp);
            font.setColor(attachment == editor.selectedAttachment ? Color.WHITE : Color.LIGHT_GRAY);
            font.draw(batch, attachment.kind + ": " + attachment.name, p.x + 8f, p.y + 18f);
        }
    }

    private void drawHud(Batch batch) {
        font.setColor(Color.WHITE);
        String name = editor.selectedAttachment == null ? "none" : editor.selectedAttachment.kind + " / " + editor.selectedAttachment.name;
        String extra = "";
        if (editor.selectedAttachment instanceof EditablePathAttachment) {
            EditablePathAttachment path = (EditablePathAttachment) editor.selectedAttachment;
            extra = " | constantSpeed=" + path.constantSpeed + " | closed=" + path.closed + " | length=" + (int)path.length;
        } else if (editor.selectedAttachment instanceof EditableClippingAttachment) {
            EditableClippingAttachment clipping = (EditableClippingAttachment) editor.selectedAttachment;
            extra = " | endSlot=" + clipping.endSlotName;
        }

        float y = getY() + getHeight() - 16f;
        font.draw(batch, "Path / Bounding Box / Clipping Editor", getX() + 12f, y);
        y -= 20f;
        font.draw(batch, "Selected: " + name + " | vertex=" + editor.selectedVertex + " | mode=" + editor.mode + extra, getX() + 12f, y);
        y -= 20f;
        font.draw(batch, "TAB switch attachment | 1 move | 2 add | 3 delete | R reverse path | C constant speed | O closed | F freeze | E set clipping end slot", getX() + 12f, y);
    }

    private Vector2 worldTo(EditableAttachment attachment, int index, Vector2 out) {
        Vector2 local = attachment.vertices.get(index);
        Vector2 p = attachment.transform.apply(local.x, local.y);
        return out.set(getX() + p.x, getY() + p.y);
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
