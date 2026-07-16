package com.tony.dominoes.mesh;

public final class MeshAttachmentModel {
    private final String name;
    private final MeshEditorDisplay display = new MeshEditorDisplay();
    private EditableMesh mesh;
    private LinkedEditableMesh linkedMesh;
    private boolean editDeformedMesh = true;

    public MeshAttachmentModel(String name, EditableMesh mesh) {
        if (mesh == null) {
            throw new IllegalArgumentException("mesh cannot be null");
        }
        this.name = name == null ? "" : name;
        this.mesh = mesh;
    }

    public String name() {
        return name;
    }

    public MeshEditorDisplay display() {
        return display;
    }

    public boolean editDeformedMesh() {
        return editDeformedMesh;
    }

    public void setEditDeformedMesh(boolean editDeformedMesh) {
        this.editDeformedMesh = editDeformedMesh;
    }

    public EditableMesh mesh() {
        return linkedMesh != null ? linkedMesh.effectiveMesh() : mesh;
    }

    public EditableMesh localMesh() {
        return linkedMesh != null ? linkedMesh.localMesh() : mesh;
    }

    public boolean linked() {
        return linkedMesh != null && linkedMesh.linked();
    }

    public String meshType() {
        if (linked()) {
            return "linked mesh";
        }
        if (mesh.linkedMeshCount() > 0) {
            return "source mesh";
        }
        return "mesh";
    }

    public LinkedEditableMesh linkedMesh() {
        return linkedMesh;
    }

    public LinkedEditableMesh createLinkedMesh(EditableMesh parent, boolean inheritParentDeform) {
        linkedMesh = parent.createLinkedMesh(inheritParentDeform);
        return linkedMesh;
    }

    public EditableMesh unlinkLinkedMesh() {
        if (linkedMesh == null) {
            return mesh;
        }
        mesh = linkedMesh.unlink();
        linkedMesh = null;
        return mesh;
    }

    public void moveVertex(int id, float x, float y) {
        if (editDeformedMesh) {
            mesh().moveVertex(id, x, y);
        } else {
            localMesh().moveSetupVertex(id, x, y);
        }
    }

    public void resetDeformation() {
        localMesh().resetDeformation();
    }

    public void freezeDeformation() {
        localMesh().freezeDeformation();
    }

    public void freezeCurrentTransform() {
        localMesh().freezeCurrentTransform();
    }

    public void freezeCurrentTransform(float width, float height, boolean centered) {
        localMesh().freezeCurrentTransform(width, height, centered);
    }
}
