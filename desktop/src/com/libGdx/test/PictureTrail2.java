package com.libGdx.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

public class PictureTrail2 extends Actor implements Pool.Poolable {
    private TextureRegion region;
    private boolean overlay = false;
    private final FloatQueue positions = new FloatQueue(MAX_POSITIONS * 2);
    private final float[] vertices = new float[MAX_POSITIONS * 5 * 2]; //every point: x,y,color,u,v; every position: two points.
    private final float[] tmpDist = new float[MAX_POSITIONS];
    private final short[] indices = new short[MAX_POSITIONS * 6];
    private static final int MAX_POSITIONS = 150;
    private static final float THRESHOLD = 5 * 5;
    private float maxLength;

    public PictureTrail2() {
        for (short i = 0; i < MAX_POSITIONS; ++i) {
            short start = (short) (i * 2);
            indices[i * 6] = start;
            indices[i * 6 + 1] = (short) (start + 1);
            indices[i * 6 + 2] = (short) (start + 2);
            indices[i * 6 + 3] = (short) (start + 2);
            indices[i * 6 + 4] = (short) (start + 3);
            indices[i * 6 + 5] = (short) (start + 1);
        }
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
        this.maxLength = region.getRegionHeight() * region.getRegionHeight();
        System.out.println("-");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (this.positions.size >= 2) {
            float lastX = this.positions.getTail(0);
            float lastY = this.positions.getTail(1);
            float dx = lastX - x;
            float dy = lastY - y;

            if (dx * dx + dy * dy < THRESHOLD) {
                return;
            }
        }
        removePos();
        this.positions.addData(y);
        this.positions.addData(x);

    }

    public void removePos(){
        if (this.positions.size < 2) return;
        float lastX = this.positions.getTail(0);
        float lastY = this.positions.getTail(1);
        float dx = lastX - x;
        float dy = lastY - y;
        if (dx * dx + dy * dy > maxLength) {
            this.positions.delete();
            this.positions.delete();
            removePos();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (this.region == null) {
            return;
        }
        Color color = getColor();
        float c = Color.toFloatBits(color.r, color.g, color.b, color.a * parentAlpha * 0.5f);
        if (this.positions.size < 4) {
            return;
        }
        float totalLength = 0f;
        float prevX = this.positions.getTail(0);
        float prevY = this.positions.getTail(1);
        float halfWidth = this.region.getRegionWidth() / 2f;
        float maxLength = this.region.getRegionHeight();
        int totalPositions = 0;
        for (int i = this.positions.size-1; i >= 0; i -= 2) {
            float x = this.positions.getTail(i);
            float y = this.positions.getTail(i - 1);
            float dx;
            float dy;
            if (i == 0) {
                float nextX = this.positions.getTail(i - 2);
                float nextY = this.positions.getTail(i - 3);
                dy = y - nextY;
                dx = x - nextX;
            } else {
                dy = prevY - y;
                dx = prevX - x;
            }
            float angle = MathUtils.atan2(dy, dx);
            float oxCCW = MathUtils.cos(angle + MathUtils.PI / 2) * halfWidth;
            float oyCCW = MathUtils.sin(angle + MathUtils.PI / 2) * halfWidth;
            float oxCW = -oxCCW;
            float oyCW = -oyCCW;
            float thisLineMaxLength = maxLength - totalLength;
            float ex, ey;
            boolean ended = false;
            float dist = i == 0 ? 0 : (float) Math.sqrt(dy * dy + dx * dx);
            if (dist > thisLineMaxLength) {
                ended = true;
                ex = prevX + MathUtils.cos(angle - MathUtils.PI) * thisLineMaxLength;
                ey = prevY + MathUtils.sin(angle - MathUtils.PI) * thisLineMaxLength;
                totalLength += thisLineMaxLength;
                this.tmpDist[totalPositions] = thisLineMaxLength;
            } else {
                ex = x;
                ey = y;
                totalLength += dist;
                this.tmpDist[totalPositions] = dist;
            }
            int start = totalPositions * 5 * 2;

            vertices[start] = ex + oxCCW;
            vertices[start + 1] = ey + oyCCW;
            vertices[start + 2] = c;

            vertices[start + 5] = ex + oxCW;
            vertices[start + 5 + 1] = ey + oyCW;
            vertices[start + 5 + 2] = c;

            totalPositions++;
            prevX = x;
            prevY = y;

            if (ended) {
                break;
            }
        }
        float currentLength = 0f;
        float u = region.getU();
        float v = region.getV(); // top (but smaller, since image is loaded upside down)
        float u2 = region.getU2();
        float v2 = region.getV2(); // bottom
        float vDist = Math.abs(v - v2);

        for (int i = 0; i < totalPositions; i++) {
            float bottom = (currentLength + tmpDist[i]) / totalLength;
            int start = i * 5 * 2;
            vertices[start + 3] = u;
            vertices[start + 4] = bottom * vDist + v;

            vertices[start + 5 + 3] = u2;
            vertices[start + 5 + 4] = vertices[start + 4];

            currentLength += tmpDist[i];
        }
        int src = batch.getBlendSrcFunc();
        int dst = batch.getBlendDstFunc();
        if (!overlay) {
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        }
        ((PolygonSpriteBatch) batch).draw(
                region.getTexture(),
                vertices,
                0,
                totalPositions * 10, indices,
                0,
                totalPositions * 6 - 6);
        batch.setBlendFunction(src, dst);
        region.setRegionWidth((int) (region.getRegionWidth()));
        region.setRegionHeight((int) (region.getRegionHeight()));
    }


    @Override
    public void reset() {
        this.positions.clear();
        this.region = null;
    }

    static class FloatQueue {
        private final int maxSize;
        private float[] data;
        private int head;
        private int tail;
        private int size;

        FloatQueue(int maxSize) {
            data = new float[maxSize];
            head = 0;
            tail = 0;
            size = 0;
            this.maxSize = maxSize;
        }

        public void addData(float v) {
            data[head] = v;
            head = (head+1) % maxSize;
            size ++;
            if (size>=maxSize){
                tail = head+1;
            }
        }

        public void clear() {
            size = 0;
            tail = 0;
            head = 0;
        }

        public float getTail(int i) {
            if (i >= this.size) {
                throw new IndexOutOfBoundsException("index " + i + " is greater than size " + this.size);
            }
            return this.data[(this.tail + i) % this.maxSize];
        }

        public void delete(){
            if (size<=0)return;
            size --;
            data[tail] = 0;
            tail = (tail + 1) % maxSize;
        }
    }
}
