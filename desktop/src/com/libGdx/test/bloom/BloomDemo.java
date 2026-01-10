package com.libGdx.test.bloom;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BloomDemo extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture spriteTexture;
    private Bloom bloom;

    @Override
    public void create() {
        batch = new SpriteBatch();
        spriteTexture = new Texture(Gdx.files.internal("000.png")); // 替换你的图片

        // 初始化 Bloom
        bloom = new Bloom(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                true,  // alpha mask
                true,  // blending
                true   // RGBA
        );

        // 设置 Bloom 参数
        bloom.setBloomIntesity(2.5f);      // 高亮强度
        bloom.setOriginalIntesity(0.8f);   // 原画面强度
        bloom.setTreshold(0.5f);           // 阈值
        bloom.setClearColor(0, 0, 0, 1);   // 背景黑色
    }

    @Override
    public void render() {
        // -------------------- 1. 开始捕获 --------------------
        bloom.capture();

        // -------------------- 2. 绘制场景 --------------------
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // 绘制几个精灵
        batch.draw(spriteTexture, 100, 100);
        batch.draw(spriteTexture, 300, 200);
        batch.draw(spriteTexture, 500, 150);
        batch.end();

        // -------------------- 3. 停止捕获 --------------------
        bloom.capturePause();

        // -------------------- 4. Bloom 后处理 --------------------
        bloom.render();
    }

    @Override
    public void resize(int width, int height) {
        bloom.resume(); // 更新内部 FBO 和 Shader
    }

    @Override
    public void dispose() {
        batch.dispose();
        spriteTexture.dispose();
        bloom.dispose();
    }
}