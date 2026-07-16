package com.tony.dominoes.mesh;

public final class MeshVertexSnapshot {
    private final int id;
    private final float setupX;
    private final float setupY;
    private final float x;
    private final float y;
    private final float u;
    private final float v;

    public MeshVertexSnapshot(int id, float x, float y, float u, float v) {
        this(id, x, y, x, y, u, v);
    }

    public MeshVertexSnapshot(int id, float setupX, float setupY, float x, float y, float u, float v) {
        this.id = id;
        this.setupX = setupX;
        this.setupY = setupY;
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
    }

    public int id() {
        return id;
    }

    public float setupX() {
        return setupX;
    }

    public float setupY() {
        return setupY;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float u() {
        return u;
    }

    public float v() {
        return v;
    }

    public float deformX() {
        return x - setupX;
    }

    public float deformY() {
        return y - setupY;
    }
}
