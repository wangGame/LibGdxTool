package com.kw.gdx.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PageFlipDrawable extends TextureRegionDrawable {

    /**
     * 每条 strip 用 4 个顶点。
     * 每个顶点:
     * x, y, color, u, v
     *
     * 所以每条 strip 是 20 个 float。
     */
    private final float[] vertices = new float[20];

    /**
     * 翻页进度：
     * 0 = 完全展开
     * 1 = 翻完
     */
    private float progress = 0f;

    /**
     * 竖向切多少段。
     * 越大越平滑，但 draw call 越多。
     */
    private int strips = 32;

    /**
     * 卷曲强度。
     */
    private float curlStrength = 0.18f;

    /**
     * 翻页时上下弯曲强度。
     */
    private float verticalBend = 0.08f;

    /**
     * true = 从右往左翻
     * false = 从左往右翻
     */
    private boolean fromRight = true;

    public PageFlipDrawable(Texture texture) {
        super(new TextureRegion(texture));
    }

    public PageFlipDrawable(TextureRegion region) {
        super(region);
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        TextureRegion region = getRegion();
        Texture texture = region.getTexture();

        float baseU = region.getU();
        float baseV = region.getV();
        float baseU2 = region.getU2();
        float baseV2 = region.getV2();

        Color batchColor = batch.getColor();

        float p = MathUtils.clamp(progress, 0f, 1f);

        /**
         * progress = 0 时，不翻页，直接正常画。
         * 这样避免初始状态因为分段计算出现误差。
         */
        if (p <= 0.0001f) {
            super.draw(batch, x, y, width, height);
            return;
        }

        for (int i = 0; i < strips; i++) {
            float n1 = i / (float) strips;
            float n2 = (i + 1) / (float) strips;

            drawStrip(
                    batch,
                    texture,
                    x,
                    y,
                    width,
                    height,
                    n1,
                    n2,
                    baseU,
                    baseV,
                    baseU2,
                    baseV2,
                    batchColor,
                    p
            );
        }
    }

    private void drawStrip(
            Batch batch,
            Texture texture,
            float x,
            float y,
            float width,
            float height,
            float n1,
            float n2,
            float baseU,
            float baseV,
            float baseU2,
            float baseV2,
            Color batchColor,
            float p
    ) {
        /**
         * 如果从左往右翻，就把归一化坐标反过来算。
         */
        float calcN1 = fromRight ? n1 : 1f - n2;
        float calcN2 = fromRight ? n2 : 1f - n1;

        float x1Local = getPageX(calcN1, width, p);
        float x2Local = getPageX(calcN2, width, p);

        float y1Offset = getPageYBend(calcN1, height, p);
        float y2Offset = getPageYBend(calcN2, height, p);

        if (!fromRight) {
            x1Local = width - x1Local;
            x2Local = width - x2Local;
        }

        float leftX = x + x1Local;
        float rightX = x + x2Local;

        float bottomY1 = y + y1Offset;
        float topY1 = y + height - y1Offset;

        float bottomY2 = y + y2Offset;
        float topY2 = y + height - y2Offset;

        /**
         * UV 根据原始 n1/n2 来算，不根据变形后的 x 算。
         */
        float u1 = MathUtils.lerp(baseU, baseU2, n1);
        float u2 = MathUtils.lerp(baseU, baseU2, n2);

        float vTop = baseV;
        float vBottom = baseV2;

        /**
         * 简单暗部。
         * 越靠近正在卷起的位置，颜色越暗一点。
         */
        float shade = getShade((n1 + n2) * 0.5f, p);

        float color = Color.toFloatBits(
                batchColor.r * shade,
                batchColor.g * shade,
                batchColor.b * shade,
                batchColor.a
        );

        int idx = 0;

        // 左下
        vertices[idx++] = leftX;
        vertices[idx++] = bottomY1;
        vertices[idx++] = color;
        vertices[idx++] = u1;
        vertices[idx++] = vBottom;

        // 左上
        vertices[idx++] = leftX;
        vertices[idx++] = topY1;
        vertices[idx++] = color;
        vertices[idx++] = u1;
        vertices[idx++] = vTop;

        // 右上
        vertices[idx++] = rightX;
        vertices[idx++] = topY2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = vTop;

        // 右下
        vertices[idx++] = rightX;
        vertices[idx++] = bottomY2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = vBottom;

        batch.draw(texture, vertices, 0, vertices.length);
    }

    /**
     * 翻页核心：
     *
     * n 是 0~1 的横向比例。
     *
     * 从右往左翻时：
     * n = 0 左边
     * n = 1 右边
     *
     * progress 越大，右侧越往左卷。
     */
    private float getPageX(float n, float width, float p) {
        /**
         * fold 表示折线位置。
         *
         * p = 0 时，fold = 1，折线在最右边。
         * p = 1 时，fold = 0，折线到最左边。
         */
        float fold = 1f - p;

        /**
         * 没被翻到的区域保持原样。
         */
        if (n <= fold) {
            return n * width;
        }

        /**
         * 被翻起区域，t 是它在翻起部分里的比例。
         */
        float t = (n - fold) / Math.max(0.0001f, 1f - fold);

        /**
         * 基础向左翻。
         */
        float flipped = fold - t * p;

        /**
         * 加一点弧形卷曲，让它不像硬折线。
         */
        float curl = MathUtils.sin(t * MathUtils.PI) * curlStrength * p;

        return (flipped + curl) * width;
    }

    /**
     * 翻页时的上下弯曲。
     * 中间卷起时上下会略微收进去。
     */
    private float getPageYBend(float n, float height, float p) {
        float fold = 1f - p;

        if (n <= fold) {
            return 0f;
        }

        float t = (n - fold) / Math.max(0.0001f, 1f - fold);

        float bend = MathUtils.sin(t * MathUtils.PI) * verticalBend * p;

        return bend * height;
    }

    /**
     * 简单明暗变化。
     */
    private float getShade(float n, float p) {
        float fold = 1f - p;

        if (n <= fold) {
            return 1f;
        }

        float t = (n - fold) / Math.max(0.0001f, 1f - fold);

        /**
         * 中间偏暗，边缘恢复一点。
         */
        float dark = MathUtils.sin(t * MathUtils.PI) * 0.35f * p;

        return 1f - dark;
    }

    public void setProgress(float progress) {
        this.progress = MathUtils.clamp(progress, 0f, 1f);
    }

    public float getProgress() {
        return progress;
    }

    public void setStrips(int strips) {
        this.strips = Math.max(4, strips);
    }

    public int getStrips() {
        return strips;
    }

    public void setCurlStrength(float curlStrength) {
        this.curlStrength = curlStrength;
    }

    public void setVerticalBend(float verticalBend) {
        this.verticalBend = verticalBend;
    }

    public void setFromRight(boolean fromRight) {
        this.fromRight = fromRight;
    }

    public boolean isFromRight() {
        return fromRight;
    }
}