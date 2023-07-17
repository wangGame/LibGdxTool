package com.libGdx.test.other;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;

import kw.test.file.Bean;
import kw.test.file.ReadFileConfig;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/28 10:31
 */
public class Dekstop implements ApplicationListener {
    public static void main(String[]a) {
        ReadFileConfig readFileConfig = new ReadFileConfig();
        Bean value = readFileConfig.getValue();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title=value.getName();
        config.x = 1000;
        config.y = 0;
        config.height = (int) (1920 );
        config.width = (int) (1080  );
        Gdx.isJiami = true;
        Dekstop dekstop = new Dekstop();
        new LwjglApplication(dekstop, config);
    }
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Texture texture;
    private Bezier<Vector2> bezier;
    private Vector2 startPoint, endPoint, controlPoint1, controlPoint2;
    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        texture = new Texture(Gdx.files.internal("fangshier.png"));

        startPoint = new Vector2(100, 100);
        endPoint = new Vector2(500, 100);
        controlPoint1 = new Vector2(250, 300);
        controlPoint2 = new Vector2(350, 500);

        bezier = new Bezier<Vector2>(startPoint, controlPoint1, controlPoint2, endPoint);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        // 在贝塞尔曲线上绘制贴图
        for (float t = 0; t <= 1; t += 0.01f) {
            Vector2 point = bezier.valueAt(new Vector2(), t);
            batch.draw(texture, point.x, point.y);
        }
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // 绘制贝塞尔曲线的控制点和曲线
        shapeRenderer.curve(
                startPoint.x, startPoint.y,
                controlPoint1.x, controlPoint1.y,
                controlPoint2.x, controlPoint2.y,
                endPoint.x, endPoint.y,
                100
        );
        shapeRenderer.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
