package com.kw.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kw.gdx.BaseGame;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.pathbbc.EditableAttachment;
import com.kw.gdx.pathbbc.EditableBoundingBoxAttachment;
import com.kw.gdx.pathbbc.EditableClippingAttachment;
import com.kw.gdx.pathbbc.EditablePathAttachment;
import com.kw.gdx.pathbbc.EditorMode;
import com.kw.gdx.pathbbc.PathBoundingClippingEditor;
import com.kw.gdx.pathbbc.PathBoundingClippingPreviewActor;

/**
 * Path / Bounding Box / Clipping 编辑模块落地 Screen。
 *
 * 这个 Screen 只演示编辑流程：
 * - 选择附件
 * - 移动 / 新建 / 删除顶点
 * - 反转 path
 * - 切换 path constant speed
 * - 设置 clipping end slot
 * - freeze rotation/scale
 *
 * 接入真实 Spine 数据时，把 EditableAttachment 和你的 Attachment 数据互转即可。
 */
public class PathBoundingClippingScreen extends BaseScreen {
    private static final String TAG = "PathBBC";

    private PathBoundingClippingEditor editor;
    private PathBoundingClippingPreviewActor previewActor;

    private int endSlotCounter = 1;

    public PathBoundingClippingScreen(BaseGame game) {
        super(game);
    }

    @Override
    protected void initData() {
        editor = new PathBoundingClippingEditor();
        createDemoAttachments();
    }

    @Override
    public void initView() {
        previewActor = new PathBoundingClippingPreviewActor(editor);
        previewActor.setPosition(0, 0);
        previewActor.setSize(Constant.GAMEWIDTH, Constant.GAMEHIGHT);
        stage.addActor(previewActor);
        previewActor.toFront();
    }

    @Override
    protected void initTouch() {
        super.initTouch();
        installPointerInput();
        installKeyboardInput();
    }

    @Override
    protected void r() {
        // BaseScreen 已经把 R 键留给 r()，这里用作 path reverse。
        editor.reversePathIfPossible();
        logSelected("reverse path");
    }

    private void installPointerInput() {
        previewActor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.RIGHT) {
                    EditorMode old = editor.mode;
                    editor.mode = EditorMode.deleteVertex;
                    boolean handled = editor.pointerDown(x, y);
                    editor.mode = old;
                    return handled;
                }
                return editor.pointerDown(x, y);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                editor.pointerDragged(x, y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                editor.pointerUp(x, y);
            }
        });
    }

    private void installKeyboardInput() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.TAB:
                        editor.selectNextAttachment();
                        logSelected("select next");
                        return true;
                    case Input.Keys.NUM_1:
                        editor.setMode(EditorMode.moveVertex);
                        return true;
                    case Input.Keys.NUM_2:
                        editor.setMode(EditorMode.addVertex);
                        return true;
                    case Input.Keys.NUM_3:
                        editor.setMode(EditorMode.deleteVertex);
                        return true;
                    case Input.Keys.FORWARD_DEL:
                    case Input.Keys.BACKSPACE:
                        editor.deleteSelectedVertex();
                        return true;
                    case Input.Keys.C:
                        editor.togglePathConstantSpeed();
                        logSelected("toggle constant speed");
                        return true;
                    case Input.Keys.O:
                        editor.togglePathClosed();
                        logSelected("toggle closed");
                        return true;
                    case Input.Keys.F:
                        editor.freezeSelectedRotationScale();
                        logSelected("freeze rotation/scale");
                        return true;
                    case Input.Keys.E:
                        editor.setSelectedClippingEndSlot("slot_end_" + endSlotCounter++);
                        logSelected("set clipping end slot");
                        return true;
                    case Input.Keys.T:
                        applyDemoTransformToSelected();
                        logSelected("apply demo transform");
                        return true;
                }
                return false;
            }
        });
    }

    private void createDemoAttachments() {
        float cx = Constant.GAMEWIDTH * 0.5f;
        float cy = Constant.GAMEHIGHT * 0.5f;

        EditablePathAttachment path = new EditablePathAttachment("demo_path");
        path.setVertices(
                cx - 260, cy - 40,
                cx - 160, cy + 100,
                cx - 20, cy + 40,
                cx + 120, cy + 150,
                cx + 260, cy - 30
        );
        path.constantSpeed = true;
        path.rebuildLengthTable();
        editor.addAttachment(path);

        EditableBoundingBoxAttachment box = new EditableBoundingBoxAttachment("demo_bbox");
        box.setVertices(
                cx - 210, cy - 210,
                cx - 80, cy - 250,
                cx + 20, cy - 190,
                cx - 40, cy - 80,
                cx - 190, cy - 80
        );
        editor.addAttachment(box);

        EditableClippingAttachment clipping = new EditableClippingAttachment("demo_clip");
        clipping.endSlotName = "slot_end_1";
        clipping.setVertices(
                cx + 120, cy - 240,
                cx + 320, cy - 210,
                cx + 360, cy - 80,
                cx + 220, cy - 40,
                cx + 80, cy - 110
        );
        editor.addAttachment(clipping);

        editor.selectAttachment(path);
    }

    private void applyDemoTransformToSelected() {
        EditableAttachment attachment = editor.selectedAttachment;
        if (attachment == null) return;

        // 为了演示 Freeze：先给它一个旋转/缩放，再按 F 烘焙进去。
        Vector2 center = calculateCenter(attachment);
        attachment.transform.x = center.x;
        attachment.transform.y = center.y;
        for (int i = 0; i < attachment.vertices.size; i++) {
            attachment.vertices.get(i).sub(center);
        }
        attachment.transform.rotation += 18f;
        attachment.transform.scaleX *= 1.15f;
        attachment.transform.scaleY *= 0.85f;
        if (attachment instanceof EditablePathAttachment) {
            ((EditablePathAttachment) attachment).rebuildLengthTable();
        }
    }

    private Vector2 calculateCenter(EditableAttachment attachment) {
        Vector2 center = new Vector2();
        if (attachment.vertices.size == 0) return center;
        for (int i = 0; i < attachment.vertices.size; i++) {
            center.add(attachment.vertices.get(i));
        }
        center.scl(1f / attachment.vertices.size);
        return center;
    }

    private void logSelected(String action) {
        EditableAttachment attachment = editor.selectedAttachment;
        if (attachment == null) {
            Gdx.app.log(TAG, action + ": none");
            return;
        }
        Gdx.app.log(TAG, action + ": " + attachment.kind + " / " + attachment.name
                + ", vertices=" + attachment.vertexCount());
    }

    @Override
    public void dispose() {
        if (previewActor != null) previewActor.dispose();
        super.dispose();
    }
}
