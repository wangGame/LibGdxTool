package com.kw.gdx.pathbbc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.FloatArray;

/**
 * Path attachment 编辑数据。
 *
 * 这里用折线/多边形方式保存 path 顶点。
 * 如果后续要完全模拟 Spine 的 Bezier path，可以在每个节点扩展 in/out handle。
 */
public class EditablePathAttachment extends EditableAttachment {
    /**
     * true：沿 path 取点时按真实长度均匀运动。
     * false：按线段索引/百分比取点，速度更省但可能不均匀。
     */
    public boolean constantSpeed = true;

    /** path 总长度缓存。 */
    public float length;

    /** 每段累计长度缓存，长度为 edgeCount + 1，第一项为 0。 */
    public final FloatArray cumulativeLengths = new FloatArray();

    public EditablePathAttachment(String name) {
        super(name, AttachmentKind.path);
        closed = false;
    }

    @Override
    protected void onVerticesChanged() {
        rebuildLengthTable();
    }

    public void setConstantSpeed(boolean constantSpeed) {
        this.constantSpeed = constantSpeed;
        rebuildLengthTable();
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
        rebuildLengthTable();
    }

    public void rebuildLengthTable() {
        cumulativeLengths.clear();
        cumulativeLengths.add(0f);
        length = 0f;
        if (vertices.size < 2) return;

        int edgeCount = closed ? vertices.size : vertices.size - 1;
        for (int i = 0; i < edgeCount; i++) {
            Vector2 a = vertices.get(i);
            Vector2 b = vertices.get((i + 1) % vertices.size);
            length += a.dst(b);
            cumulativeLengths.add(length);
        }
    }

    /**
     * 根据 0..1 采样 path 上的位置。
     * constantSpeed=true 时按长度采样；false 时按线段比例采样。
     */
    public Vector2 sample(float t, Vector2 out) {
        if (out == null) out = new Vector2();
        if (vertices.size == 0) return out.setZero();
        if (vertices.size == 1) return out.set(vertices.first());

        if (t < 0f) t = 0f;
        else if (t > 1f) t = 1f;

        int edgeCount = closed ? vertices.size : vertices.size - 1;
        if (edgeCount <= 0) return out.set(vertices.first());

        int edgeIndex;
        float localT;

        if (constantSpeed && length > 0f) {
            float target = t * length;
            edgeIndex = 0;
            while (edgeIndex + 1 < cumulativeLengths.size && cumulativeLengths.get(edgeIndex + 1) < target) {
                edgeIndex++;
            }
            float start = cumulativeLengths.get(edgeIndex);
            float end = cumulativeLengths.get(edgeIndex + 1);
            localT = end == start ? 0f : (target - start) / (end - start);
        } else {
            float raw = t * edgeCount;
            edgeIndex = Math.min((int)raw, edgeCount - 1);
            localT = raw - edgeIndex;
        }

        Vector2 a = vertices.get(edgeIndex);
        Vector2 b = vertices.get((edgeIndex + 1) % vertices.size);
        return out.set(a).lerp(b, localT);
    }
}
