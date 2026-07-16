package com.tony.dominoes.mesh;

public final class MeshEditToolState {
    private MeshEditMode mode = MeshEditMode.MODIFY;
    private boolean deformed = true;
    private boolean dim;
    private boolean isolate;
    private boolean triangles = true;

    public MeshEditMode mode() {
        return mode;
    }

    public void setMode(MeshEditMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("mode cannot be null");
        }
        this.mode = mode;
    }

    public boolean deformed() {
        return deformed;
    }

    public void setDeformed(boolean deformed) {
        this.deformed = deformed;
    }

    public boolean dim() {
        return dim;
    }

    public void setDim(boolean dim) {
        this.dim = dim;
    }

    public boolean isolate() {
        return isolate;
    }

    public void setIsolate(boolean isolate) {
        this.isolate = isolate;
    }

    public boolean triangles() {
        return triangles;
    }

    public void setTriangles(boolean triangles) {
        this.triangles = triangles;
    }
}
