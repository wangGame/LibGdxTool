package com.libGdx.test.arrow;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.base.LibGdxTestMain;

public class B extends LibGdxTestMain {

    private Texture lineTexture;
    private BezierFlyOutTextureActor trail;

    public static void main(String[] args) {
        B b = new B();
        b.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        Array<Vector2> points = new Array<>();

        points.add(new Vector2(0, 0));
        points.add(new Vector2(0, 0));
        points.add(new Vector2(300, 0));
        points.add(new Vector2(600, 900));
        points.add(new Vector2(400, 0));
        points.add(new Vector2(600, 0));
        points.add(new Vector2(100, 0));
        points.add(new Vector2(110, 1200));
        points.add(new Vector2(700, 0));
        points.add(new Vector2(180, 1200));

        BUL1 bezier = new BUL1(points);

        // 这里换成你的纹理
        lineTexture = new Texture("assets/7.png");

        trail = new BezierFlyOutTextureActor(lineTexture, bezier);

        // 第一段：曲线画出来
        trail.setDrawDuration(0.2F);

        // 第二段：尾部收缩，头部飞出去
        trail.setFlyOutDuration(.2F);
        trail.setAutoFlyDistance(true);

// 飞出距离 = 曲线长度 * 0.35
        trail.setAutoFlyDistanceScale(0.5F);
        // 第二段飞出的距离
//        trail.setFlyDistance(600F);

        // 第二段结束后继续飞出去的速度
        trail.setContinueFlySpeed(1300F);

        // 曲线宽度
        trail.setWidth(30F);

        // 曲线采样
        trail.setCurveSamples(160);
        trail.setStraightSamples(50);

        addActor(trail);

        // 可选：如果你有箭头头部 Actor，可以让它跟着头部走
        // 这里先用普通 Actor 演示，你实际可以换成 Image。
        Actor head = new Actor();
        head.setSize(40, 40);
        trail.setFollowActor(head);
        addActor(head);
    }

    @Override
    public void dispose() {
        super.dispose();

        if (trail != null) {
            trail.dispose();
        }

        if (lineTexture != null) {
            lineTexture.dispose();
        }
    }
}