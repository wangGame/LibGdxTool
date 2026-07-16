package com.tony.dominoes.mesh;

public final class LinkedEditableMesh {
    private EditableMesh parent;
    private EditableMesh local;
    private boolean inheritParentDeform;

    public LinkedEditableMesh(EditableMesh parent, boolean inheritParentDeform) {
        if (parent == null) {
            throw new IllegalArgumentException("parent cannot be null");
        }
        this.parent = parent;
        this.local = parent.copy();
        this.inheritParentDeform = inheritParentDeform;
        if (!inheritParentDeform) {
            this.local.resetDeformation();
        }
    }

    public boolean linked() {
        return parent != null;
    }

    public boolean inheritParentDeform() {
        return inheritParentDeform;
    }

    public void setInheritParentDeform(boolean inheritParentDeform) {
        this.inheritParentDeform = inheritParentDeform;
        if (!inheritParentDeform && parent != null) {
            local = parent.copy();
            local.resetDeformation();
        }
    }

    public EditableMesh parentMesh() {
        return parent;
    }

    public EditableMesh localMesh() {
        return local;
    }

    public EditableMesh effectiveMesh() {
        if (parent != null && inheritParentDeform) {
            return parent;
        }
        return local;
    }

    public EditableMesh unlink() {
        EditableMesh unlinked = effectiveMesh().copy();
        parent = null;
        local = unlinked;
        inheritParentDeform = false;
        return local;
    }

    void detachFromParent() {
        parent = null;
        inheritParentDeform = false;
    }
}
