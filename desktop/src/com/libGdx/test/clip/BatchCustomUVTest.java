package com.libGdx.test.clip;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BatchCustomUVTest extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture texture;

    /**
     * 4 个顶点，每个顶点 5 个 float:
     *
     * x, y, color, u, v
     *
     * 所以一共 4 * 5 = 20 个 float
     */
    private final float[] vertices = new float[20];

    @Override
    public void create() {
        batch = new SpriteBatch();

        texture = new Texture(Gdx.files.internal("test.png"));

        // 让放大时更容易看出 UV 效果
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.12f, 0.12f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        /*
         * 在屏幕上绘制一个 300x300 的矩形
         * 但只显示纹理中间区域
         */
        drawTextureWithUV(
                batch,
                texture,
                100, 100,
                300, 300,
                0.25f, 0.25f,
                0.75f, 0.75f
        );

        batch.end();
    }

    private void drawTextureWithUV(
            SpriteBatch batch,
            Texture texture,
            float x,
            float y,
            float width,
            float height,
            float u1,
            float v1,
            float u2,
            float v2
    ) {
        float x1 = x;
        float y1 = y;
        float x2 = x + width;
        float y2 = y + height;

        /*
         * SpriteBatch 里面的 color 是 packed float
         * 白色表示不改变纹理原色
         */
        float color = batch.getColor().toFloatBits();

        /*
         * 顶点顺序：
         *
         * 左下
         * 左上
         * 右上
         * 右下
         *
         * 每个顶点:
         * x, y, color, u, v
         */

        int idx = 0;

        // 左下
        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = color;
        vertices[idx++] = u1;
        vertices[idx++] = v2;

        // 左上
        vertices[idx++] = x1;
        vertices[idx++] = y2;
        vertices[idx++] = color;
        vertices[idx++] = u1;
        vertices[idx++] = v1;

        // 右上
        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v1;

        // 右下
        vertices[idx++] = x2;
        vertices[idx++] = y1;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        batch.draw(texture, vertices, 0, vertices.length);
    }

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }
}