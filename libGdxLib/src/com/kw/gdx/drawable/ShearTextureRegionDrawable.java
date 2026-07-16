package com.kw.gdx.drawable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.math.MathUtils;

public class ShearTextureRegionDrawable extends TextureRegionDrawable {

    /**
     * SpriteBatch / CpuPolygonSpriteBatch 顶点格式：
     *
     * x, y, color, u, v
     *
     * 4 个点，所以是 4 * 5 = 20
     */
    private final float[] vertices = new float[20];

    /**
     * Spine 风格 shear，单位：角度
     */
    private float shearX = 0f;
    private float shearY = 0f;

    /**
     * 普通 Drawable.draw(batch, x, y, width, height)
     * 拿不到 Actor 的 origin，所以这里自己定义一个默认 origin。
     *
     * 默认中心点：0.5, 0.5
     */
    private float originRatioX = 0.5f;
    private float originRatioY = 0.5f;

    public ShearTextureRegionDrawable(TextureRegion region) {
        super(region);
    }

    public ShearTextureRegionDrawable(Texture texture) {
        super(new TextureRegion(texture));
    }

    public void updateTextureRegion(TextureRegion region) {
        setRegion(region);
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        float originX = width * originRatioX;
        float originY = height * originRatioY;

        drawShear(
                batch,
                x,
                y,
                originX,
                originY,
                width,
                height,
                1f,
                1f,
                0f
        );
    }

    /**
     * 如果 Image 走 TransformDrawable 分支，
     * 这里也能支持 scale / rotation。
     */
    @Override
    public void draw(
            Batch batch,
            float x,
            float y,
            float originX,
            float originY,
            float width,
            float height,
            float scaleX,
            float scaleY,
            float rotation
    ) {
        drawShear(
                batch,
                x,
                y,
                originX,
                originY,
                width,
                height,
                scaleX,
                scaleY,
                rotation
        );
    }

    private void drawShear(
            Batch batch,
            float x,
            float y,
            float originX,
            float originY,
            float width,
            float height,
            float scaleX,
            float scaleY,
            float rotation
    ) {
        TextureRegion region = getRegion();
        Texture texture = region.getTexture();

        float u = region.getU();
        float v = region.getV();
        float u2 = region.getU2();
        float v2 = region.getV2();

        Color batchColor = batch.getColor();
        float color = batchColor.toFloatBits();

        /**
         * 这里是类似 Spine 的矩阵写法：
         *
         * a = cos(rotation + shearX) * scaleX
         * b = cos(rotation + 90 + shearY) * scaleY
         * c = sin(rotation + shearX) * scaleX
         * d = sin(rotation + 90 + shearY) * scaleY
         */
        float a = MathUtils.cosDeg(rotation + shearX) * scaleX;
        float b = MathUtils.cosDeg(rotation + 90f + shearY) * scaleY;
        float c = MathUtils.sinDeg(rotation + shearX) * scaleX;
        float d = MathUtils.sinDeg(rotation + 90f + shearY) * scaleY;

        float worldOriginX = x + originX;
        float worldOriginY = y + originY;

        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;

        /**
         * 四个角点，局部坐标经过 shear / scale / rotation 后变成世界坐标。
         *
         * 左下
         * 左上
         * 右上
         * 右下
         */
        float x1 = fx * a + fy * b + worldOriginX;
        float y1 = fx * c + fy * d + worldOriginY;

        float x2 = fx * a + fy2 * b + worldOriginX;
        float y2 = fx * c + fy2 * d + worldOriginY;

        float x3 = fx2 * a + fy2 * b + worldOriginX;
        float y3 = fx2 * c + fy2 * d + worldOriginY;

        float x4 = fx2 * a + fy * b + worldOriginX;
        float y4 = fx2 * c + fy * d + worldOriginY;

        int idx = 0;

        // 左下
        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;

        // 左上
        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;

        // 右上
        vertices[idx++] = x3;
        vertices[idx++] = y3;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;

        // 右下
        vertices[idx++] = x4;
        vertices[idx++] = y4;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        batch.draw(texture, vertices, 0, vertices.length);
    }

    public void setShear(float shearX, float shearY) {
        this.shearX = shearX;
        this.shearY = shearY;
    }

    public void setShearX(float shearX) {
        this.shearX = shearX;
    }

    public void setShearY(float shearY) {
        this.shearY = shearY;
    }

    public float getShearX() {
        return shearX;
    }

    public float getShearY() {
        return shearY;
    }

    /**
     * 设置普通 draw 模式下的倾斜中心点。
     *
     * 0, 0     表示左下角
     * 0.5,0.5 表示中心点
     * 1, 1     表示右上角
     */
    public void setOriginRatio(float originRatioX, float originRatioY) {
        this.originRatioX = originRatioX;
        this.originRatioY = originRatioY;
    }
}