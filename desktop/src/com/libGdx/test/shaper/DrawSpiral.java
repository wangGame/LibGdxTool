package com.libGdx.test.shaper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class DrawSpiral extends LibGdxTestMain {
    public static void main(String[] args) {
        DrawSpiral drawSpiral = new DrawSpiral();
        drawSpiral.start();
    }
    private static final int COILS = 50;
    ShapeRenderer shapeRenderer;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        super.render();

        if (shapeRenderer == null)return;

        draw4();
//        draw3();


//        draw1();
//        draw2();

    }

    public void draw4(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rectLine(100, 0, 100, 300, 20);

        // TODO: Draw two leaves on the stem

        // TODO: Set the active color to yellow

        // TODO: Use a loop to draw 20 of these petals in a circle

        float petalAngle = 45.0f;
        shapeRenderer.rect(100, 300, 0, 0, 40, 40, 1, 1, petalAngle);

        shapeRenderer.end();
    }

    public void draw3(){

        // Rectangles can be drawn with either ShapeType.Filled or ShapeType.Line
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);

        // Let's just draw a simple square to start
        shapeRenderer.rect(10, 10, 90, 90);

        // We can do even more interesting things with colors, like specifying a color for each corner!
        shapeRenderer.rect(110, 10, 90, 90, Color.BLUE, Color.BLACK, Color.GREEN, Color.MAGENTA);
        shapeRenderer.rect(10, 110, 90, 90, Color.RED, Color.RED, Color.BLACK, Color.BLACK);

        // What happens when we draw two filled in shapes where they overlap?
        shapeRenderer.rect(210, 10, 90, 90, Color.RED, Color.RED, Color.RED, Color.RED);
        shapeRenderer.rect(230, 30, 90, 90, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);

        // We can also rotate and scale rectangles!
        // We can put the rotation origin on the corner
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(10, 300, 50, 50, 100, 100, 0.5f, 1, 45);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(10, 300, 50, 50, 100, 100, 0.5f, 1, 135);

        // Or we can put the rotation origin in the center
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(200, 300, 0, 0, 100, 100, 0.5f, 1, 45);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(200, 300, 0, 0, 100, 100, 0.5f, 1, 225);

        // Let's try making a thick line
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.rectLine(0, 200, 200, 250, 10);

        // Alright, time for some silliness. Let's make a rainbow flower
        final int steps = 25;
        Color rgbColor = new Color();
        for (int i = 0; i < steps; i++) {
//             This mess converts from a position on the rainbow to an RGB color
            Color.argb8888ToColor(rgbColor, HSBtoRGB(1.0f * i / steps, 1, 1));

            // Each rectangle is a little bit rotated from the previous one
            shapeRenderer.rect(300, 300, 50, 50, 100, 100, 1, 1, i * 90 / steps, rgbColor, rgbColor, rgbColor, rgbColor);
        }
        shapeRenderer.end();
    }

    private void draw1() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        int xStep = screenWidth / 2 / COILS;
        int yStep = screenHeight / 2 / COILS;

        for (int i = 0; i < COILS; i++) {

            int xOffset = xStep * i;
            int yOffset = yStep * i;

            // TODO: Make this coil reach back to the outer coil
            Vector2 point1 = new Vector2(xOffset, yOffset);
            Vector2 point2 = new Vector2(screenWidth - xOffset, yOffset);
            Vector2 point3 = new Vector2(screenWidth - xOffset, screenHeight - yOffset);
            Vector2 point4 = new Vector2(xOffset, screenHeight - yOffset);

            // TODO: Make this coil stop before connecting back to itself
            Vector2 point5 = new Vector2(xOffset, yOffset);

            shapeRenderer.line(point1, point2);
            shapeRenderer.line(point2, point3);
            shapeRenderer.line(point3, point4);
            shapeRenderer.line(point4, point5);
        }
        shapeRenderer.end();
    }

    private void draw2() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        int xStep = screenWidth / 2 / COILS;
        int yStep = screenHeight / 2 / COILS;

        for (int i = 0; i < COILS; i++){

            int xOffset = xStep * i;
            int yOffset = yStep * i;

            // TODO: Make this coil reach back to the outer coil
            Vector2 point1 = new Vector2(xOffset - xStep, yOffset);
            Vector2 point2 = new Vector2(screenWidth - xOffset, yOffset);
            Vector2 point3 = new Vector2(screenWidth - xOffset, screenHeight - yOffset);
            Vector2 point4 = new Vector2(xOffset, screenHeight - yOffset);
            // TODO: Make this coil stop before connecting back to itself
            Vector2 point5 = new Vector2(xOffset, yOffset + yStep);

            shapeRenderer.line(point1, point2);
            shapeRenderer.line(point2, point3);
            shapeRenderer.line(point3, point4);
            shapeRenderer.line(point4, point5);

        }
        shapeRenderer.end();
    }

    // Stolen from java.awt.Color
    public static int HSBtoRGB(float var0, float var1, float var2) {
        int var3 = 0;
        int var4 = 0;
        int var5 = 0;
        if(var1 == 0.0F) {
            var3 = var4 = var5 = (int)(var2 * 255.0F + 0.5F);
        } else {
            float var6 = (var0 - (float)Math.floor((double)var0)) * 6.0F;
            float var7 = var6 - (float)Math.floor((double)var6);
            float var8 = var2 * (1.0F - var1);
            float var9 = var2 * (1.0F - var1 * var7);
            float var10 = var2 * (1.0F - var1 * (1.0F - var7));
            switch((int)var6) {
                case 0:
                    var3 = (int)(var2 * 255.0F + 0.5F);
                    var4 = (int)(var10 * 255.0F + 0.5F);
                    var5 = (int)(var8 * 255.0F + 0.5F);
                    break;
                case 1:
                    var3 = (int)(var9 * 255.0F + 0.5F);
                    var4 = (int)(var2 * 255.0F + 0.5F);
                    var5 = (int)(var8 * 255.0F + 0.5F);
                    break;
                case 2:
                    var3 = (int)(var8 * 255.0F + 0.5F);
                    var4 = (int)(var2 * 255.0F + 0.5F);
                    var5 = (int)(var10 * 255.0F + 0.5F);
                    break;
                case 3:
                    var3 = (int)(var8 * 255.0F + 0.5F);
                    var4 = (int)(var9 * 255.0F + 0.5F);
                    var5 = (int)(var2 * 255.0F + 0.5F);
                    break;
                case 4:
                    var3 = (int)(var10 * 255.0F + 0.5F);
                    var4 = (int)(var8 * 255.0F + 0.5F);
                    var5 = (int)(var2 * 255.0F + 0.5F);
                    break;
                case 5:
                    var3 = (int)(var2 * 255.0F + 0.5F);
                    var4 = (int)(var8 * 255.0F + 0.5F);
                    var5 = (int)(var9 * 255.0F + 0.5F);
            }
        }

        return -16777216 | var3 << 16 | var4 << 8 | var5 << 0;
    }
}