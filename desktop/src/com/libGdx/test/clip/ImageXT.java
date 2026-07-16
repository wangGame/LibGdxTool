package com.libGdx.test.clip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.CpuPolygonSpriteBatch;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ShortArray;
import com.kw.gdx.asset.Asset;

public class ImageXT extends Actor {

    private Texture texture;

    private float[] spriteVertices;
    private short[] polygonTriangles;
    private float[] polygonPoints;

    private EarClippingTriangulator triangulator;

    /**
     * 类似 Spine 里的 shearX / shearY。
     * 单位：角度。
     */
    private float shearXDeg = 0f;
    private float shearYDeg = 0f;

    public ImageXT() {
        texture = Asset.getAsset().getTexture("assets/shuoming.png");

        spriteVertices = new float[19 * 5];
        polygonPoints = new float[19 * 2];

        triangulator = new EarClippingTriangulator();

        initPolygonPoints();
        initTriangles();

        setSize(500, 500);

        /**
         * 让倾斜围绕中心点发生。
         * 如果你想围绕左下角倾斜，可以改成 setOrigin(0, 0);
         */
        setOrigin(250, 250);
    }

    private void initPolygonPoints() {
        int i = 0;

        // 19 个点，按顺时针顺序
        polygonPoints[i++] = 0;
        polygonPoints[i++] = 500;

        polygonPoints[i++] = 100;
        polygonPoints[i++] = 500;

        polygonPoints[i++] = 200;
        polygonPoints[i++] = 500;

        polygonPoints[i++] = 300;
        polygonPoints[i++] = 500;

        polygonPoints[i++] = 400;
        polygonPoints[i++] = 500;

        polygonPoints[i++] = 500;
        polygonPoints[i++] = 500;

        polygonPoints[i++] = 500;
        polygonPoints[i++] = 400;

        polygonPoints[i++] = 500;
        polygonPoints[i++] = 300;

        polygonPoints[i++] = 500;
        polygonPoints[i++] = 200;

        polygonPoints[i++] = 500;
        polygonPoints[i++] = 100;

        polygonPoints[i++] = 500;
        polygonPoints[i++] = 0;

        polygonPoints[i++] = 400;
        polygonPoints[i++] = 0;

        polygonPoints[i++] = 300;
        polygonPoints[i++] = 0;

        polygonPoints[i++] = 200;
        polygonPoints[i++] = 0;

        polygonPoints[i++] = 100;
        polygonPoints[i++] = 0;

        polygonPoints[i++] = 0;
        polygonPoints[i++] = 0;

        polygonPoints[i++] = 0;
        polygonPoints[i++] = 100;

        polygonPoints[i++] = 0;
        polygonPoints[i++] = 250;

        polygonPoints[i++] = 0;
        polygonPoints[i++] = 400;
    }

    private void initTriangles() {
        ShortArray array = triangulator.computeTriangles(polygonPoints);
        polygonTriangles = array.toArray();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!(batch instanceof CpuPolygonSpriteBatch)) {
            throw new RuntimeException("ImageXT 需要使用 CpuPolygonSpriteBatch");
        }

        updateSpriteVertices(parentAlpha);

        ((CpuPolygonSpriteBatch) batch).draw(
                texture,
                spriteVertices,
                0,
                spriteVertices.length,
                polygonTriangles,
                0,
                polygonTriangles.length
        );
    }

    private void updateSpriteVertices(float parentAlpha) {
        Color colorObj = getColor();
        float color = Color.toFloatBits(
                colorObj.r,
                colorObj.g,
                colorObj.b,
                colorObj.a * parentAlpha
        );

        float minX = 0;
        float minY = 0;
        float maxX = 500;
        float maxY = 500;

        float originX = getOriginX();
        float originY = getOriginY();

        float skewX = (float) Math.tan(shearXDeg * MathUtils.degreesToRadians);
        float skewY = (float) Math.tan(shearYDeg * MathUtils.degreesToRadians);

        int vertexIndex = 0;

        for (int i = 0; i < 19; i++) {
            float localX = polygonPoints[i * 2];
            float localY = polygonPoints[i * 2 + 1];

            /**
             * 先以 origin 为中心，把点转成相对坐标。
             */
            float dx = localX - originX;
            float dy = localY - originY;

            /**
             * shear 变换。
             *
             * shearX:
             * y 越大，x 偏移越多。
             *
             * shearY:
             * x 越大，y 偏移越多。
             */
            float shearedX = originX + dx + skewX * dy;
            float shearedY = originY + dy + skewY * dx;

            /**
             * 加上 Actor 自己的位置。
             */
            float worldX = getX() + shearedX;
            float worldY = getY() + shearedY;

            /**
             * UV 不要跟着 shear 改。
             * 只根据原始 localX/localY 计算。
             */
            float u = (localX - minX) / (maxX - minX);
            float v = 1f - (localY - minY) / (maxY - minY);

            spriteVertices[vertexIndex++] = worldX;
            spriteVertices[vertexIndex++] = worldY;
            spriteVertices[vertexIndex++] = color;
            spriteVertices[vertexIndex++] = u;
            spriteVertices[vertexIndex++] = v;
        }
    }

    public void setShearXDeg(float shearXDeg) {
        this.shearXDeg = shearXDeg;
    }

    public void setShearYDeg(float shearYDeg) {
        this.shearYDeg = shearYDeg;
    }

    public float getShearXDeg() {
        return shearXDeg;
    }

    public float getShearYDeg() {
        return shearYDeg;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}