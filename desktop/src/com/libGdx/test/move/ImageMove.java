package com.libGdx.test.move;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/1 16:35
 */
class ImageMove extends LibGdxTestMain {
    public static void main(String[] args) {
        ImageMove move = new ImageMove();
        move.start(move)

    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Image image = new Image(Asset.getAsset().getTexture("hand1.png"));
        addActor(image);
        image.setOrigin(10,10);
        image.addAction(Actions.scaleTo(4,4,0.5f));
    }
}
