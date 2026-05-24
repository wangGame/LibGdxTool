package com.libGdx.test.action;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.action.NewActions;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class ActionDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        ActionDemo demo = new ActionDemo();
        demo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Image image = new Image(Asset.getAsset().getTexture("assets/7.png"));
        addActor(image);

        image.addAction(NewActions.moveXTo(100,2, Interpolation.bounce));
        image.addAction(NewActions.moveYTo(500,2, Interpolation.sine));



    }
}
