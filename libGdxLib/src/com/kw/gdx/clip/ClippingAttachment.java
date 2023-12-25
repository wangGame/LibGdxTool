package com.kw.gdx.clip;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/21 14:22
 */
public class ClippingAttachment {
    //clip的位置
    private float clipX;
    private float clipY;
    //顶点的个数
    private int vertNum;
    //顶点值
    protected float[] vertices;

    public void setVerties(float [] floats){
        this.vertNum = floats.length;
        vertices = floats;
    }

    /**
     * 裁剪区域的大小 以及位置
     * @param start
     * @param count
     * @param worldVertices
     * @param offset
     * @param stride
     */
    public void computeWorldVertices (int start,
                                      int count,
                                      float[] worldVertices,
                                      int offset,
                                      int stride) {
        count = offset + (count >> 1) * stride;
        float[] vertices = this.vertices;
        float a = Constant.a;
        float b = Constant.b;
        float c = Constant.c;
        float d = Constant.d;
        for (int v = start, w = offset; w < count; v += 2, w += stride) {
            float vx = vertices[v], vy = vertices[v + 1];
            worldVertices[w] = vx * a + vy * b + clipX;
            worldVertices[w + 1] = vx * c + vy * d + clipY;
        }
    }

    public void setClipX(float clipX) {
        this.clipX = clipX;
    }

    public void setClipY(float clipY) {
        this.clipY = clipY;
    }

    public int getVertNum() {
        return vertNum;
    }
}
