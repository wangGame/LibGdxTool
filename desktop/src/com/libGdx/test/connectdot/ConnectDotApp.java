package com.libGdx.test.connectdot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.base.LibGdxTestMain;


public class ConnectDotApp extends LibGdxTestMain {

    private static final float DOT_RADIUS = 3.0f;
    private final Array<Vector2> dots = Dots.dots();
    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;
    private float[] floatDots;
    private ShapeRenderer shapeRenderer;

    public static void main(String[] args) {
        ConnectDotApp connectDotApp = new ConnectDotApp();
        connectDotApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        spriteBatch = new SpriteBatch();
        bitmapFont = new BitmapFont(Gdx.files.internal("assets/font/Cali_75.fnt"));
        floatDots = vector2ArrayToFloatArray(dots);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        super.render();

        if (shapeRenderer==null)return;

        // Draw the dots
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Vector2 dot : dots) {
            shapeRenderer.circle(dot.x, dot.y, DOT_RADIUS);
        }
        shapeRenderer.end();

        // Draw the numbers
        spriteBatch.begin();
        Integer i = 1;
        for (Vector2 dot : dots) {
            bitmapFont.draw(spriteBatch, i.toString(), dot.x + DOT_RADIUS, dot.y - DOT_RADIUS);
            i++;
        }
        spriteBatch.end();

    }

    private float[] vector2ArrayToFloatArray(Array<Vector2> dots) {

        float[] floatDots = new float[dots.size * 2];

        return floatDots;
    }

}
