package com.kw.gdx.pathbbc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Path / Bounding Box / Clipping 的编辑控制器。
 *
 * 它不关心具体 UI，只负责：
 * - 当前选中附件
 * - 顶点增删改
 * - path reverse / constant speed
 * - clipping end slot
 * - freeze transform
 */
public class PathBoundingClippingEditor {
    public final Array<EditableAttachment> attachments = new Array<EditableAttachment>();

    public EditableAttachment selectedAttachment;
    public int selectedVertex = -1;
    public EditorMode mode = EditorMode.moveVertex;

    public float pickRadius = 18f;

    private final Vector2 dragStart = new Vector2();
    private final Vector2 vertexStart = new Vector2();
    private boolean dragging;

    public void addAttachment(EditableAttachment attachment) {
        attachments.add(attachment);
        if (selectedAttachment == null) selectAttachment(attachment);
    }

    public void selectAttachment(EditableAttachment attachment) {
        selectedAttachment = attachment;
        selectedVertex = -1;
        dragging = false;
    }

    public void selectNextAttachment() {
        if (attachments.size == 0) return;
        int index = selectedAttachment == null ? -1 : attachments.indexOf(selectedAttachment, true);
        index = (index + 1) % attachments.size;
        selectAttachment(attachments.get(index));
    }

    public boolean pointerDown(float x, float y) {
        if (selectedAttachment == null) return false;

        if (mode == EditorMode.addVertex) {
            int insertIndex = selectedAttachment.findInsertIndexOnNearestEdge(x, y);
            selectedVertex = selectedAttachment.insertVertex(insertIndex, x, y);
            return true;
        }

        int hit = selectedAttachment.findNearestVertex(x, y, pickRadius);
        if (mode == EditorMode.deleteVertex) {
            if (hit != -1) {
                selectedAttachment.removeVertex(hit);
                selectedVertex = -1;
                return true;
            }
            return false;
        }

        if (hit != -1) {
            selectedVertex = hit;
            dragStart.set(x, y);
            vertexStart.set(selectedAttachment.getVertex(hit));
            dragging = true;
            return true;
        }

        selectedVertex = -1;
        return false;
    }

    public boolean pointerDragged(float x, float y) {
        if (selectedAttachment == null || selectedVertex == -1 || !dragging) return false;
        if (mode != EditorMode.moveVertex && mode != EditorMode.select) return false;

        float dx = x - dragStart.x;
        float dy = y - dragStart.y;
        selectedAttachment.setVertex(selectedVertex, vertexStart.x + dx, vertexStart.y + dy);
        return true;
    }

    public void pointerUp(float x, float y) {
        dragging = false;
    }

    public void addVertexAtEnd(float x, float y) {
        if (selectedAttachment == null) return;
        selectedVertex = selectedAttachment.addVertex(x, y);
    }

    public void deleteSelectedVertex() {
        if (selectedAttachment == null || selectedVertex == -1) return;
        selectedAttachment.removeVertex(selectedVertex);
        if (selectedAttachment.vertexCount() == 0) selectedVertex = -1;
        else if (selectedVertex >= selectedAttachment.vertexCount()) selectedVertex = selectedAttachment.vertexCount() - 1;
    }

    public void reversePathIfPossible() {
        if (selectedAttachment instanceof EditablePathAttachment) {
            selectedAttachment.reverse();
        }
    }

    public void togglePathConstantSpeed() {
        if (selectedAttachment instanceof EditablePathAttachment) {
            EditablePathAttachment path = (EditablePathAttachment) selectedAttachment;
            path.setConstantSpeed(!path.constantSpeed);
        }
    }

    public void togglePathClosed() {
        if (selectedAttachment instanceof EditablePathAttachment) {
            EditablePathAttachment path = (EditablePathAttachment) selectedAttachment;
            path.setClosed(!path.closed);
        } else if (selectedAttachment != null) {
            selectedAttachment.closed = !selectedAttachment.closed;
        }
    }

    public void freezeSelectedRotationScale() {
        if (selectedAttachment != null) {
            selectedAttachment.freezeRotationScale();
        }
    }

    public void setSelectedClippingEndSlot(String endSlotName) {
        if (selectedAttachment instanceof EditableClippingAttachment) {
            ((EditableClippingAttachment) selectedAttachment).setEndSlotName(endSlotName);
        }
    }

    public void setMode(EditorMode mode) {
        this.mode = mode;
    }
}
