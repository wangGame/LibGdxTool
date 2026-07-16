package com.tony.dominoes.mesh;

public final class MeshVertex {
    private final int id;
    private float setupX;
    private float setupY;
    private float x;
    private float y;
    private float u;
    private float v;

    MeshVertex(int id, float x, float y, float u, float v) {
        this.id = id;
        this.setupX = x;
        this.setupY = y;
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
    }

    public int id() {
        return id;
    }

    public float x() {
        return x;
    }

    public float setupX() {
        return setupX;
    }

    public float y() {
        return y;
    }

    public float setupY() {
        return setupY;
    }

    public float u() {
        return u;
    }

    public float v() {
        return v;
    }

    void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    void setSetupPosition(float setupX, float setupY) {
        this.setupX = setupX;
        this.setupY = setupY;
    }

    void translate(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    void translateSetup(float dx, float dy) {
        this.setupX += dx;
        this.setupY += dy;
    }

    void resetDeformation() {
        this.x = setupX;
        this.y = setupY;
    }

    void freezeDeformation() {
        this.setupX = x;
        this.setupY = y;
    }

    void setUv(float u, float v) {
        this.u = u;
        this.v = v;
    }

    public MeshVertexSnapshot snapshot() {
        return new MeshVertexSnapshot(id, setupX, setupY, x, y, u, v);
    }
}
