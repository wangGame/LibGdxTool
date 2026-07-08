package com.tony.dominoes.mesh;

public final class MeshGenerateSettings {
    private int columns = 4;
    private int rows = 3;
    private int maxVertices = 3000;

    public int columns() {
        return columns;
    }

    public void setColumns(int columns) {
        if (columns < 1) {
            throw new IllegalArgumentException("columns must be >= 1");
        }
        this.columns = columns;
    }

    public int rows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows < 1) {
            throw new IllegalArgumentException("rows must be >= 1");
        }
        this.rows = rows;
    }

    public int maxVertices() {
        return maxVertices;
    }

    public void setMaxVertices(int maxVertices) {
        if (maxVertices < 4) {
            throw new IllegalArgumentException("maxVertices must be >= 4");
        }
        this.maxVertices = maxVertices;
    }

    public int vertexCount() {
        return (columns + 1) * (rows + 1);
    }
}
