package com.tony.dominoes.mesh;

public final class MeshEditorDisplay {
    private boolean verticesAndEdgesVisible = true;
    private boolean triangleLinesVisible = true;
    private boolean imageDimmed;
    private boolean isolated;

    public boolean verticesAndEdgesVisible() {
        return verticesAndEdgesVisible;
    }

    public void setVerticesAndEdgesVisible(boolean visible) {
        this.verticesAndEdgesVisible = visible;
    }

    public boolean triangleLinesVisible() {
        return triangleLinesVisible;
    }

    public void setTriangleLinesVisible(boolean visible) {
        this.triangleLinesVisible = visible;
    }

    public boolean imageDimmed() {
        return imageDimmed;
    }

    public void setImageDimmed(boolean imageDimmed) {
        this.imageDimmed = imageDimmed;
    }

    public boolean isolated() {
        return isolated;
    }

    public void setIsolated(boolean isolated) {
        this.isolated = isolated;
    }
}
