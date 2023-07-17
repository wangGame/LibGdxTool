package com.libGdx.test.other;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import kw.test.file.Bean;
import kw.test.file.ReadFileConfig;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/28 10:45
 */
class Desk implements ApplicationListener {
    public static void main(String[]a) {
        ReadFileConfig readFileConfig = new ReadFileConfig();
        Bean value = readFileConfig.getValue();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title=value.getName();
        config.x = 1000;
        config.y = 0;
        config.height = (int) (1920);
        config.width = (int) (1080);
        Gdx.isJiami = true;
        Desk dekstop = new Desk();
        new LwjglApplication(dekstop, config);
    }
    SpriteBatch batch;
    ShaderProgram shader;
    Texture texture;
    float controlPointX;
    float controlPointY;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // 加载纹理
        texture = new Texture(Gdx.files.internal("banner.png"));

        // 编译着色器程序
        String vertexShaderCode = Gdx.files.internal("vvv.glsl").readString();
        String fragmentShaderCode = Gdx.files.internal("fff.glsl").readString();
        shader = new ShaderProgram(vertexShaderCode, fragmentShaderCode);
        if (!shader.isCompiled()) {
            Gdx.app.error("Shader", "Shader compilation failed:\n" + shader.getLog());
            return;
        }

        // 设置曲线控制点坐标
        controlPointX = Gdx.graphics.getWidth() / 2;
        controlPointY = Gdx.graphics.getHeight() / 4;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 应用着色器程序
        batch.setShader(shader);

        // 绘制纹理
        batch.begin();
        batch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // 取消着色器程序的应用
        batch.setShader(null);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
        shader.dispose();
    }
}
