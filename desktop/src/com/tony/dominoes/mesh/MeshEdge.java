package com.tony.dominoes.mesh;

public final class MeshEdge {
    private final int a;
    private final int b;

    public MeshEdge(int a, int b) {
        if (a == b) {
            throw new IllegalArgumentException("edge endpoints must be different: " + a);
        }
        if (b < a) {
            this.a = b;
            this.b = a;
        } else {
            this.a = a;
            this.b = b;
        }
    }

    public int a() {
        return a;
    }

    public int b() {
        return b;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof MeshEdge)) {
            return false;
        }
        MeshEdge edge = (MeshEdge) object;
        return a == edge.a && b == edge.b;
    }

    @Override
    public int hashCode() {
        return 31 * a + b;
    }

    @Override
    public String toString() {
        return a + "," + b;
    }
}
