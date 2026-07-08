package com.tony.dominoes.mesh;

public final class MeshBounds {
    private final float minX;
    private final float minY;
    private final float maxX;
    private final float maxY;

    public MeshBounds(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public float minX() {
        return minX;
    }

    public float minY() {
        return minY;
    }

    public float maxX() {
        return maxX;
    }

    public float maxY() {
        return maxY;
    }

    public float width() {
        return maxX - minX;
    }

    public float height() {
        return maxY - minY;
    }
}
