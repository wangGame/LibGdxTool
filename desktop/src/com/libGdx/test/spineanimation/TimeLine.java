package com.libGdx.test.spineanimation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class TimeLine extends LibGdxTestMain {
    public static void main(String[] args) {
        TimeLine timeLine = new TimeLine();
        timeLine.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Image image = new Image(Asset.getAsset().getTexture("assets/fangshiyi.png")){
            @Override
            protected void positionChanged() {
                super.positionChanged();
            }
        };
        addActor(image);
        MoveTimeLine action = new MoveTimeLine();
        action.setBaseX(1);
        action.setBaseY(1);
        action.setStartX(0);
        action.setStartY(0);
        action.setEndX(0);
        action.setEndY(1512.31f);
        action.setDuration(0.233333f);
        action.setCurvesX(0.067f,0,0.2f,0);
        action.setCurvesY(0.067f,1512.31F,0.2f,0);
        image.addAction(Actions.delay(2,action));




//
//        NewTimeLine timeLine = new NewTimeLine();
//        timeLine.setStartX(0);
//        timeLine.setStartY(409.66f);
//        timeLine.setEndX(0);
//        timeLine.setEndY(0);
//        timeLine.setDuration(0.233333f);
//        timeLine.setCurvesX(0.078f,0,0.233333f,0);
//        timeLine.setCurvesY(0.078f,409.66f,0.233333f,0);
////        image.addAction(Actions.delay(2,timeLine));
//
//
//        NewTimeLine timeLine2 = new NewTimeLine();
//        timeLine2.setStartX(0);
//        timeLine2.setStartY(409.66f);
//        timeLine2.setEndX(0);
//        timeLine2.setEndY(0);
//        timeLine2.setDuration(0.233333f);
//        timeLine2.setCurvesX(0f,0,0.156f,0);
//        timeLine2.setCurvesY(0,409.66f,0.156f,0);
//        image.addAction(Actions.delay(2,timeLine2));

    }

    @Override
    public void render() {
        super.render();

    }
}
