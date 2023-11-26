package com.libGdx.test.movetest;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

import kw.artpuzzle.screen.MainScreen;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/15 19:37
 */
class MoveTest extends LibGdxTestMain {
    public static void main(String[] args) {
        MoveTest test = new MoveTest();
        test.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        {
            Image image = new Image(Asset.getAsset().getTexture("assets/white.png"));
            image.setSize(300,300);
            image.setName("img_1");
            image.setPosition(0, 0, Align.bottomLeft);
            stage.addActor(image);
        }
        {
            Image image = new Image(Asset.getAsset().getTexture("assets/white.png"));
            image.setSize(300,300);
            image.setPosition(0, Constant.GAMEHIGHT, Align.topLeft);
            stage.addActor(image);
            image.setName("img_2");
        }
        {
            Image image = new Image(Asset.getAsset().getTexture("assets/white.png"));
            image.setSize(300,300);
            image.setPosition(0, Constant.GAMEHIGHT/2, Align.left);
            stage.addActor(image);
            image.setName("img_21");
        }
        {
            Image image = new Image(Asset.getAsset().getTexture("assets/white.png"));
            image.setSize(300,300);
            image.setPosition(Constant.GAMEWIDTH,0, Align.bottomRight);
            stage.addActor(image);
            image.setName("img_3");
        }
        {
            Image image = new Image(Asset.getAsset().getTexture("assets/white.png"));
            image.setSize(300,300);
            image.setPosition(Constant.GAMEWIDTH,Constant.GAMEHIGHT, Align.topRight);
            stage.addActor(image);
            image.setName("img_4");
        }
        {
            Image image = new Image(Asset.getAsset().getTexture("assets/white.png"));
            image.setSize(600,600);
            image.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
            stage.addActor(image);
            image.setOrigin(Align.bottomRight);
            Actor img_1 = stage.getRoot().findActor("img_2");
            img_1.setColor(Color.RED);

            float offsetY = image.getY(Align.center)-img_1.getY(Align.center);
            float originX = 0;
            float originY = image.getHeight()/2 - offsetY;
            image.setOrigin(originX,originY);
            image.setColor(Color.valueOf("#444444"));
            float scaletemp = 0.3f;
            float targetX = img_1.getX(Align.left);
            float targetY = img_1.getY(Align.center)+offsetY*0.7f;

            image.addAction(Actions.parallel(
                    Actions.moveToAligned(
                            targetX,
                            targetY, Align.left,1.5f),
                    Actions.scaleTo(scaletemp,scaletemp,1.5f)
            ));
        }

    }
}
