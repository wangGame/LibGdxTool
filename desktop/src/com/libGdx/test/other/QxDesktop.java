package com.libGdx.test.other;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/28 13:52
 */
class QxDesktop implements ApplicationListener {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.y = 0;
        config.height = (int) (1920/3.0f);
        config.width = (int) (1080/3.0f);
        QxDesktop dekstop = new QxDesktop();
        new LwjglApplication(dekstop, config);
    }

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private int start = 0;
    private int end = 100;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {

    }

    private float time  = 0;

    @Override
    public void render() {
        time += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        int segments = 1000;

        if (end >100) {
            start++;
        }
            end++;
            if (end >= segments) {
                end = segments;
            }
            if (start>=segments){
                start = 0;
                end = 0;
            }

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // 设置起始点、控制点和结束点
        Vector2 startPoint = new Vector2(100, 100);
        Vector2 controlPoint = new Vector2(200, 300);
        Vector2 endPoint = new Vector2(300, 100);

        // 设置曲线细分数


        // 绘制二次贝塞尔曲线
        shapeRenderer.setColor(1, 1, 1, 1);
        for (int i = start; i <= end; i++) {
            float t = (float) i / segments;
            float x = quadraticBezier(startPoint.x, controlPoint.x, endPoint.x, t);
            float y = quadraticBezier(startPoint.y, controlPoint.y, endPoint.y, t);
            shapeRenderer.point(x, y, 0);
        }

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
        shapeRenderer.dispose();
    }

    // 计算二次贝塞尔曲线上的点
    private float quadraticBezier(float start, float control, float end, float t) {
        float u = 1 - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;
        return (uuu * start) + (2 * uu * t * control) + (ttt * end);
    }
}
