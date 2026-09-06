package com.libGdx.test.skew;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.action.NumAction;
import com.kw.gdx.action.NumActionListener;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class SkewApp extends LibGdxTestMain {
    public static void main(String[] args) {
        SkewApp skewApp = new SkewApp();
        skewApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        SkewGroup skewGroup = new SkewGroup();
        addActor(skewGroup);

        Image image = new Image(Asset.getAsset().getTexture("assets/000.png"));
        skewGroup.addActor(image);
        image.setSize(500,500);


        NumAction action = new NumAction();
        action.setStart(0);
        action.setEnd(30);
        action.setDuration(1);

        action.setNumActionListener(new NumActionListener() {
            @Override
            public void update(float value) {
                skewGroup.setSkew(0, value);
            }
        });

        NumAction action1 = new NumAction();
        action1.setStart(30);
        action1.setEnd(0);
        action1.setDuration(1);

        action1.setNumActionListener(new NumActionListener() {
            @Override
            public void update(float value) {
                skewGroup.setSkew(0, value);
            }
        });
        image.addAction(
                Actions.sequence(
                        action,
                        action1
                )
        );

        skewGroup.setSize(image.getWidth(),image.getHeight());
        skewGroup.setOrigin(Align.center);
        skewGroup.addAction(Actions.sequence(
                Actions.scaleTo(0, 1,1),
                Actions.scaleTo(1, 1,1)
        ));
        skewGroup.setDebug(true);
    }
}
