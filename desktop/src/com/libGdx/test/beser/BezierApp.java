package com.libGdx.test.beser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.base.LibGdxTestMain;

public class BezierApp  extends LibGdxTestMain {
    private ShapeRenderer renderer;
    private Vector2 lastV2 = new Vector2();
    private boolean inited = false;
    public static void main(String[] args) {
        BezierApp b = new BezierApp();
        b.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        Actor image = new Actor();
        addActor(image);

        Array<Vector2> a = new Array<>();
        a.add(new Vector2(0, 0));
        a.add(new Vector2(0, 0));
        a.add(new Vector2(300, 0));
        a.add(new Vector2(600, 900));
        a.add(new Vector2(400, 0));
        a.add(new Vector2(600, 0));
        a.add(new Vector2(100, 0));
        a.add(new Vector2(10, 1200));
        a.add(new Vector2(700, 0));
        a.add(new Vector2(580, 200));

        BUL1 bu = new BUL1(a);

        Texture texture = new Texture("assets/7.png");

        BezierFlyOutTextureActor trail = new BezierFlyOutTextureActor(texture, bu);

        // 曲线画出来的时间
        trail.setDrawDuration(2F);

        // 结束后尾部收缩 + 头部飞出的时间
        trail.setFlyOutDuration(18F);

        // 飞出去的直线长度
        trail.setFlyDistance(600F);

        // 纹理曲线宽度
        trail.setWidth(30F);

        // 让 image 跟着头部走
        trail.setFollowActor(image);

        addActor(trail);
    }
//        super.useShow(stage);
//
//        Actor image = new Actor();
//        addActor(image);
//
//        Array<Vector2> a = new Array<>();
//        a.add(new Vector2(0, 0));
//        a.add(new Vector2(0, 0));
//        a.add(new Vector2(300, 0));
//        a.add(new Vector2(600, 900));
//        a.add(new Vector2(400, 0));
//        a.add(new Vector2(600, 0));
//        a.add(new Vector2(100, 0));
//        a.add(new Vector2(10, 1200));
//        a.add(new Vector2(700, 0));
//        a.add(new Vector2(80, 200));
//
//        BUL1 bu = new BUL1(a);
//        bu.setDuration(2);
//        bu.setInterpolation(Interpolation.sineOut);
//
//        image.addAction(bu);
//
//        Texture texture = new Texture("assets/7.png");
//        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
//        BezierTextureDebugActor debugActor = new BezierTextureDebugActor(texture, bu);
//        debugActor.setWidth(30F);
//        debugActor.setSamples(160);
//        debugActor.setUseMoveT(true);
//
//        addActor(debugActor);
//
//    }
//        super.useShow(stage);
//
//        renderer = new ShapeRenderer();
//
//        Actor image = new Actor();
//        addActor(image);
//
//        Array<Vector2> a = new Array<>();
//        a.add(new Vector2(0, 0));
//        a.add(new Vector2(0, 0));
//        a.add(new Vector2(300, 0));
//        a.add(new Vector2(600, 900));
//        a.add(new Vector2(400, 0));
//        a.add(new Vector2(600, 0));
//        a.add(new Vector2(100, 0));
//        a.add(new Vector2(10, 1200));
//        a.add(new Vector2(700, 0));
//        a.add(new Vector2(80, 200));
//
//        BUL1 bu = new BUL1(a);
//        bu.setDuration(2);
//        bu.setInterpolation(Interpolation.sineOut);
//
//        image.addAction(bu);
//
//        BezierDebugActor debugActor = new BezierDebugActor(renderer, bu);
//
//        // true：曲线绘制进度和 image 当前运动位置一致
//        debugActor.setUseMoveT(false);
//
//
//
//        // 采样精度
//        debugActor.setSamples(100);
//
//        addActor(debugActor);
//    }
}
