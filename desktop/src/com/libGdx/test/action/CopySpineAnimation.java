package com.libGdx.test.action;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.libGdx.test.base.LibGdxTestMain;

public class CopySpineAnimation extends LibGdxTestMain {
    public static void main(String[] args) {
        CopySpineAnimation copySpineAnimation = new CopySpineAnimation();
        copySpineAnimation.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Image image = new Image();
        image.addAction(Actions.sequence(
                Actions.delay(1)

        ));
    }
}
