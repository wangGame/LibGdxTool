package com.libGdx.test.shaper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class RenderGame extends LibGdxTestMain {
    public static void main(String[] args) {
        RenderGame renderGame = new RenderGame();
        renderGame.start();
    }


    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        shapeRenderer = new ShapeRenderer();
        CirImage cirImage = new CirImage();
        addActor(cirImage);
    }
    private float startAngle = 90;  // 0度，水平向右
    private float sweepAngle = 360;
    @Override
    public void render() {
        super.render();
//        if (shapeRenderer != null) {
//            sweepAngle -= 0.4f;  // 每次减小角度，表示逆向绘制圆弧
//            if (sweepAngle <= 0) {
//                sweepAngle = 0; // 防止角度小于0
//            }
//
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);  // 使用填充模式
//            shapeRenderer.setColor(Color.BLACK);
//
//            // 圆心坐标
//            float centerX = 400;
//            float centerY = 300;
//
//            // 圆的半径
//            float radius = 100;
//
//            // 绘制逆向圆弧
//            shapeRenderer.arc(centerX, centerY, radius, startAngle, sweepAngle);
//            double v = Math.toRadians(sweepAngle + 90);
//
//            shapeRenderer.setColor(Color.WHITE);
//            shapeRenderer.rect(centerX+MathUtils.cos((float) v) * radius,
//                    centerY+MathUtils.sin((float)v) * radius,3,3);
//            shapeRenderer.end();
//        }
    }
}
