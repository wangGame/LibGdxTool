package com.tony.dominoes.mesh;

public final class MeshTriangle {
    private final int a;
    private final int b;
    private final int c;

    public MeshTriangle(int a, int b, int c) {
        if (a == b || a == c || b == c) {
            throw new IllegalArgumentException("triangle vertices must be unique: " + a + "," + b + "," + c);
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int a() {
        return a;
    }

    public int b() {
        return b;
    }

    public int c() {
        return c;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof MeshTriangle)) {
            return false;
        }
        MeshTriangle triangle = (MeshTriangle) object;
        return hasSameVertices(triangle.a, triangle.b, triangle.c);
    }

    @Override
    public int hashCode() {
        int min = Math.min(a, Math.min(b, c));
        int max = Math.max(a, Math.max(b, c));
        int middle = a + b + c - min - max;
        int result = min;
        result = 31 * result + middle;
        result = 31 * result + max;
        return result;
    }

    @Override
    public String toString() {
        return a + "," + b + "," + c;
    }

    private boolean hasSameVertices(int otherA, int otherB, int otherC) {
        return contains(otherA) && contains(otherB) && contains(otherC);
    }

    private boolean contains(int vertex) {
        return a == vertex || b == vertex || c == vertex;
    }
}
