package com.kw.gdx.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.example.trace.TraceResult;

/** Draws the source texture and overlays the traced outline / triangulation. */
public class TracePreviewActor extends Actor {
    private final Texture texture;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private TraceResult result;
    private boolean drawOutline = true;
    private boolean drawTriangles;

    public TracePreviewActor(Texture texture) {
        this.texture = texture;
        setSize(texture.getWidth(), texture.getHeight());
    }

    public void setTraceResult(TraceResult result) {
        this.result = result;
    }

    public void setDrawOutline(boolean drawOutline) {
        this.drawOutline = drawOutline;
    }

    public void setDrawTriangles(boolean drawTriangles) {
        this.drawTriangles = drawTriangles;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());

        if (result == null || (!drawOutline && !drawTriangles)) return;

        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        float sx = getWidth() / result.imageWidth;
        float sy = getHeight() / result.imageHeight;

        if (drawTriangles && result.vertices != null && result.indices != null) {
            shapeRenderer.setColor(0f, 0.75f, 1f, 1f);
            drawTriangleLines(result, sx, sy);
        }

        if (drawOutline) {
            shapeRenderer.setColor(1f, 0.15f, 0.15f, 1f);
            for (float[] polygon : result.outlines) {
                drawPolygon(polygon, sx, sy);
            }
            shapeRenderer.setColor(1f, 0.75f, 0f, 1f);
            for (float[] polygon : result.holes) {
                drawPolygon(polygon, sx, sy);
            }
        }

        shapeRenderer.end();
        batch.begin();
    }

    private void drawTriangleLines(TraceResult result, float sx, float sy) {
        float[] v = result.vertices;
        short[] ids = result.indices;
        for (int i = 0; i + 2 < ids.length; i += 3) {
            drawVertexLine(v, ids[i], ids[i + 1], sx, sy);
            drawVertexLine(v, ids[i + 1], ids[i + 2], sx, sy);
            drawVertexLine(v, ids[i + 2], ids[i], sx, sy);
        }
    }

    private void drawVertexLine(float[] vertices, int a, int b, float sx, float sy) {
        int ai = a * 4;
        int bi = b * 4;
        shapeRenderer.line(
                getX() + vertices[ai] * sx,
                getY() + vertices[ai + 1] * sy,
                getX() + vertices[bi] * sx,
                getY() + vertices[bi + 1] * sy
        );
    }

    private void drawPolygon(float[] polygon, float sx, float sy) {
        if (polygon == null || polygon.length < 6) return;
        for (int i = 0; i < polygon.length; i += 2) {
            int j = (i + 2) % polygon.length;
            shapeRenderer.line(
                    getX() + polygon[i] * sx,
                    getY() + polygon[i + 1] * sy,
                    getX() + polygon[j] * sx,
                    getY() + polygon[j + 1] * sy
            );
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
