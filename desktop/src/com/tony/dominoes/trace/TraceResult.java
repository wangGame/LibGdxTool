package com.tony.dominoes.trace;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;

/** CPU-side trace result. Create Mesh only on the GL/render thread. */
public class TraceResult {
    public final int imageWidth;
    public final int imageHeight;

    /** Each outline is x,y,x,y... in image-local coordinates. */
    public final Array<float[]> outlines = new Array<>();

    /** Transparent holes found inside outlines. LibGDX EarClippingTriangulator can't handle these directly. */
    public final Array<float[]> holes = new Array<>();

    /** x,y,u,v per vertex. Ready for Mesh if indices is non-empty. */
    public float[] vertices;

    /** Triangle indices into vertices / 4. */
    public short[] indices;

    public boolean hasHoles;
    public long elapsedMillis;
    public boolean flipY;

    public TraceResult(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public int getVertexCount() {
        return vertices == null ? 0 : vertices.length / 4;
    }

    public int getTriangleCount() {
        return indices == null ? 0 : indices.length / 3;
    }

    /**
     * Creates a LibGDX Mesh using Position.xy + TexCoord0.xy.
     * Call this in render thread, and remember to dispose the returned Mesh.
     */
    public Mesh toMesh(boolean isStatic) {
        if (vertices == null || vertices.length == 0 || indices == null || indices.length == 0) {
            throw new IllegalStateException("TraceResult has no triangulated mesh data.");
        }
        Mesh mesh = new Mesh(isStatic, getVertexCount(), indices.length,
                new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0"));
        mesh.setVertices(vertices);
        mesh.setIndices(indices);
        return mesh;
    }

    /** Utility for drawing outlines with ShapeRenderer.polygon. */
    public float[] getLargestOutline() {
        float[] best = null;
        float bestArea = -1f;
        for (float[] p : outlines) {
            float a = Math.abs(area(p));
            if (a > bestArea) {
                bestArea = a;
                best = p;
            }
        }
        return best;
    }

    public static float area(float[] polygon) {
        if (polygon == null || polygon.length < 6) return 0f;
        float sum = 0f;
        int n = polygon.length;
        for (int i = 0; i < n; i += 2) {
            int j = (i + 2) % n;
            sum += polygon[i] * polygon[j + 1] - polygon[j] * polygon[i + 1];
        }
        return sum * 0.5f;
    }

    static ShortArray triangulate(float[] polygon) {
        return new EarClippingTriangulator().computeTriangles(polygon);
    }
}
