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
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        for (int i = 0; i < 100; i++) {
            for (int i1 = 0; i1 < 100; i1++) {
                CirImage cirImage = new CirImage(shapeRenderer);
                addActor(cirImage);
                cirImage.setPosition(20*i,20*i1);

//                CirImage2 depthGroup = new CirImage2(shapeRenderer);
//                addActor(depthGroup);
//                depthGroup.setPosition(20*i,20*i1);
            }
        }
    }
    private float startAngle = 90;  // 0度，水平向右
    private float sweepAngle = 360;
    @Override
    public void render() {
        long l = System.currentTimeMillis();
        super.render();
        System.out.println(System.currentTimeMillis()-l);
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
