package com.libGdx.test.moni;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;


public class Appppp extends LibGdxTestMain {
    public static void main(String[] args) {
        Appppp appppp = new Appppp();
        appppp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        test01();
        Image i = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        addActor(i);

        Actor point = new Image(new NinePatch(Asset.getAsset().getTexture("white.png")));
        addActor(point);
        point.setPosition(400,400,Align.center);

        Actor image = new Image(new NinePatch(Asset.getAsset().getTexture("white.png"))){
            @Override
            protected void positionChanged() {
                super.positionChanged();
                i.setPosition(getX(),getY(),Align.center);
                i.setOrigin(Align.center);
                i.setRotation(getRotation());
            }
        };
        addActor(image);
        image.setPosition(400,400);
        point.addAction(new Action() {

            private float time = 0;
            private float target = 100;
            @Override
            public boolean act(float delta) {
                float xx = 400;
                float yy = 400;
                time += delta * 10;
                if (time >= target){
                    time = target;
                }
                float sin = (float) (Math.sin(Math.toRadians(time)) * 100);
                float cos = (float) (Math.cos(Math.toRadians(time)) * 100);
                image.setPosition(sin+xx - image.getHeight()/2.0f,cos+yy - image.getHeight()/2.0f);
                image.setOrigin(image.getHeight()/2.0f,image.getHeight()/2.0f);
                image.setRotation(-time);

                return false;
            }
        });

    }
    private void test01() {
        Image i = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg")){
            @Override
            public void act(float delta) {
                super.act(delta);
                System.out.println(getX()+"     "+getY());
            }
        };
        i.setPosition(400,400, Align.center);
        i.setOrigin(Align.center);
        i.setScale(0.1f);
//        Image image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
//        addActor(image);
//        Actor image = new Image(new NinePatch(Asset.getAsset().getTexture("white.png")));
        Actor image = new Group();
        addActor(image);
        addActor(i);
        image.setHeight(20);
        image.setWidth(500);
        image.setPosition(400,400,Align.left);
        image.setDebug(true);

        i.addAction(new Action() {
            private float time = 0;
            private float target = 100;
            @Override
            public boolean act(float delta) {
                float xx = 400;
                float yy = 400;
                time += delta * 10;
                if (time >= target){
                    time = target;
                }
                float sin = (float) (Math.sin(Math.toRadians(time)));
                float cos = (float) (Math.cos(Math.toRadians(time)));
                image.setPosition(sin+xx - image.getWidth()/2.0f,cos+yy - image.getHeight()/2.0f);
                image.setRotation(-time);
//                image.setPosition(sin+xx,yy+cos);
//                image.setRotation(-time);
                return false;
            }
        });
    }
}
