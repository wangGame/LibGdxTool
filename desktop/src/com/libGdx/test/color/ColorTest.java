package com.libGdx.test.color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/14 18:33
 */
class ColorTest extends LibGdxTestMain {
    public static void main(String[] args) {
        ColorTest test = new ColorTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        oneLineColor(stage);
        xLineColor(stage);
    }

    private void xLineColor(Stage stage) {
        Color startLeft = Color.valueOf("7ECBC9");
        Color  startRight = Color.valueOf("F6E561");
        Color  endLeft  = Color.valueOf("0837DC");
        Color endLeftRight = Color.valueOf("F77259");
        Group group = new Group();
        group.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        Table t = new Table(){{
            for (int i1 = 0; i1 < 10; i1++) {
                for (int i = 0; i < 10; i++) {
                    Image image = new Image(Asset.getAsset().getTexture("assets/white.png"));
                    add(image);
                    image.setSize(450,150);
                    Color interpolatedColor = interpolateColor(startLeft, endLeft, startRight, endLeftRight, i1/10.0f, i/10.0f);
                    image.setColor(interpolatedColor);
                }
                row();
            }
            pack();
        }};
        group.addActor(t);
        stage.addActor(group);
        group.setScale(3);
    }


    public static Color interpolateColor(Color topLeftColor, Color topRightColor, Color bottomLeftColor,
                                         Color bottomRightColor, float x, float y) {
        // 计算水平方向的插值
        Color topColor = interpolateColor(topLeftColor, topRightColor, x);
        Color bottomColor = interpolateColor(bottomLeftColor, bottomRightColor, x);
        // 计算垂直方向的插值
        return interpolateColor(topColor, bottomColor, y);
    }

    public static Color interpolateColor(Color startColor, Color endColor, float t) {
        float interpolatedRed = (startColor.r + t * (endColor.r - startColor.r));
        float interpolatedGreen = (startColor.g + t * (endColor.g - startColor.g));
        float interpolatedBlue = (startColor.b + t * (endColor.b - startColor.b));
        Color color = new Color();
        return color.set(interpolatedRed, interpolatedGreen, interpolatedBlue,1.0f);
    }

//    public static void main(String[] args) {
////        Color topLeftColor = new Color(255, 0, 0);          // 左上角颜色为红色
////        Color topRightColor = new Color(0, 255, 0);         // 右上角颜色为绿色
////        Color bottomLeftColor = new Color(0, 0, 255);       // 左下角颜色为蓝色
////        Color bottomRightColor = new Color(255, 255, 0);    // 右下角颜色为黄色
////        double x = 0.5;  // 在 x 轴上的相对位置（0.0 到 1.0）
////        double y = 0.5;  // 在 y 轴上的相对位置（0.0 到 1.0）
////
////        System.out.println("Interpolated Color: " + interpolatedColor);
//    }

    private void oneLineColor(Stage stage) {
        Color start = Color.valueOf("FFE93A");
        Color end = Color.valueOf("e84340");
        Color tempColor = new Color();
        Group group = new Group();
        group.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        Table t = new Table(){{
            for (int i = 0; i < 10; i++) {
                Image image = new Image(Asset.getAsset().getTexture("assets/ball.png"));
                add(image);
                tempColor.set(start);
                tempColor.lerp(end,i/10.0f);
                image.setColor(tempColor);
            }
            pack();
        }};
        group.addActor(t);
        stage.addActor(group);
        group.setScale(0.5f);
    }
}
