package com.tony.dominoes.mesh;

public final class MeshDemo {
    private MeshDemo() {
    }

    public static EditableMesh createGridDemo() {
        EditableMesh mesh = EditableMesh.grid(4.0f, 3.0f, 2, 2);
        mesh.validate().requireValid();
        return mesh;
    }

    public static String createGridDemoSummary() {
        return createGridDemo().toJsonSummary().toString(2);
    }
}
