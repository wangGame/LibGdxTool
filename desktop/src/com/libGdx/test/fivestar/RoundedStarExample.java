package com.libGdx.test.fivestar;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;



public class RoundedStarExample extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    // 星形的半径和角度
    private float radius = 100;
    private float cornerRadius = 20; // 圆角半径

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        // 清除屏幕
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.YELLOW);

        // 画带圆角的五角星
        drawRoundedStar(400, 300, radius, cornerRadius);

        shapeRenderer.end();
    }

    // 绘制带圆角的五角星
    private void drawRoundedStar(float x, float y, float radius, float cornerRadius) {
        Vector2[] starVertices = new Vector2[10];
        float angle = 72f; // 角度增量

        // 计算五角星的顶点
        for (int i = 0; i < 10; i++) {
            float currentAngle = angle * i;
            float currentRadius = (i % 2 == 0) ? radius : radius / 2; // 交替内外顶点
            starVertices[i] = new Vector2(
                    x + currentRadius * (float) Math.cos(Math.toRadians(currentAngle)),
                    y + currentRadius * (float) Math.sin(Math.toRadians(currentAngle))
            );
        }

        // 画出带圆角的五角星
        for (int i = 0; i < 10; i++) {
            Vector2 start = starVertices[i];
            Vector2 end = starVertices[(i + 1) % 10];

            // 用圆角绘制边缘
            shapeRenderer.arc(start.x, start.y, cornerRadius, 0, 90); // 画圆角

            shapeRenderer.line(start.x, start.y, end.x, end.y); // 画直线
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.stencil=8;
        config.y = 0;
        config.height = (int) (1920 * 0.5f);
        config.width = (int) (1080 * 0.5f);
        RoundedStarExample example = new RoundedStarExample();
        new LwjglApplication(example, config);
    }

}