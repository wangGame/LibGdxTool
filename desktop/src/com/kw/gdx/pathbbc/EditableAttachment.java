package com.kw.gdx.pathbbc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

/**
 * Path / BoundingBox / Clipping 的公共顶点编辑模型。
 *
 * 顶点统一使用 local 坐标保存。transform 是附件级变换。
 */
public abstract class EditableAttachment {
    public final String name;
    public final AttachmentKind kind;
    public final Array<Vector2> vertices = new Array<Vector2>();
    public final Transform2D transform = new Transform2D();

    /** 是否闭合。BoundingBox / Clipping 一般闭合，Path 可配置。 */
    public boolean closed = true;

    protected EditableAttachment(String name, AttachmentKind kind) {
        this.name = name;
        this.kind = kind;
    }

    public int vertexCount() {
        return vertices.size;
    }

    public Vector2 getVertex(int index) {
        checkIndex(index);
        return vertices.get(index);
    }

    public void setVertex(int index, float x, float y) {
        checkIndex(index);
        vertices.get(index).set(x, y);
    }

    public void translateVertex(int index, float dx, float dy) {
        checkIndex(index);
        vertices.get(index).add(dx, dy);
    }

    public int addVertex(float x, float y) {
        vertices.add(new Vector2(x, y));
        onVerticesChanged();
        return vertices.size - 1;
    }

    public int insertVertex(int index, float x, float y) {
        if (index < 0) index = 0;
        if (index > vertices.size) index = vertices.size;
        vertices.insert(index, new Vector2(x, y));
        onVerticesChanged();
        return index;
    }

    public Vector2 removeVertex(int index) {
        checkIndex(index);
        Vector2 removed = vertices.removeIndex(index);
        onVerticesChanged();
        return removed;
    }

    public void reverse() {
        vertices.reverse();
        onVerticesChanged();
    }

    /**
     * Freeze 当前 rotation / scale / shear。
     * 视觉形状不变，但顶点会被烘焙到当前变换后的坐标；
     * rotation=0, scale=1, shear=0。
     */
    public void freezeRotationScale() {
        if (vertices.size == 0) return;

        Array<Vector2> baked = new Array<Vector2>(vertices.size);
        for (int i = 0; i < vertices.size; i++) {
            Vector2 v = vertices.get(i);
            Vector2 world = transform.apply(v.x, v.y);
            // 保留 attachment 的 x/y，所以顶点写成相对 x/y 的坐标。
            baked.add(new Vector2(world.x - transform.x, world.y - transform.y));
        }
        vertices.clear();
        vertices.addAll(baked);
        transform.resetRotationScaleShear();
        onVerticesChanged();
    }

    public int findNearestVertex(float x, float y, float maxDistance) {
        int best = -1;
        float best2 = maxDistance * maxDistance;
        for (int i = 0; i < vertices.size; i++) {
            Vector2 v = vertices.get(i);
            float dx = v.x - x;
            float dy = v.y - y;
            float dst2 = dx * dx + dy * dy;
            if (dst2 <= best2) {
                best2 = dst2;
                best = i;
            }
        }
        return best;
    }

    /**
     * 找最近线段，返回插入点 index。闭合时最后一个点到第一个点也参与。
     */
    public int findInsertIndexOnNearestEdge(float x, float y) {
        if (vertices.size < 2) return vertices.size;

        float best2 = Float.MAX_VALUE;
        int insertIndex = vertices.size;
        int edgeCount = closed ? vertices.size : vertices.size - 1;
        for (int i = 0; i < edgeCount; i++) {
            Vector2 a = vertices.get(i);
            Vector2 b = vertices.get((i + 1) % vertices.size);
            float dst2 = distancePointSegment2(x, y, a.x, a.y, b.x, b.y);
            if (dst2 < best2) {
                best2 = dst2;
                insertIndex = i + 1;
            }
        }
        return insertIndex;
    }

    public FloatArray toFloatArray() {
        FloatArray out = new FloatArray(vertices.size * 2);
        for (int i = 0; i < vertices.size; i++) {
            Vector2 v = vertices.get(i);
            out.add(v.x);
            out.add(v.y);
        }
        return out;
    }

    public void setVertices(float... xy) {
        vertices.clear();
        for (int i = 0; i + 1 < xy.length; i += 2) {
            vertices.add(new Vector2(xy[i], xy[i + 1]));
        }
        onVerticesChanged();
    }

    protected void onVerticesChanged() {
    }

    protected void checkIndex(int index) {
        if (index < 0 || index >= vertices.size) {
            throw new IndexOutOfBoundsException("index=" + index + ", size=" + vertices.size);
        }
    }

    private static float distancePointSegment2(float px, float py, float ax, float ay, float bx, float by) {
        float abx = bx - ax;
        float aby = by - ay;
        float apx = px - ax;
        float apy = py - ay;
        float len2 = abx * abx + aby * aby;
        if (len2 == 0f) {
            float dx = px - ax;
            float dy = py - ay;
            return dx * dx + dy * dy;
        }
        float t = (apx * abx + apy * aby) / len2;
        if (t < 0f) t = 0f;
        else if (t > 1f) t = 1f;
        float cx = ax + abx * t;
        float cy = ay + aby * t;
        float dx = px - cx;
        float dy = py - cy;
        return dx * dx + dy * dy;
    }
}
