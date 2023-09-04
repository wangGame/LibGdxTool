package com.libGdx.test.move;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/1 16:35
 *
 * 修改缩放中心
 */
class ImageMove extends LibGdxTestMain {
    public static void main(String[] args) {
        ImageMove move = new ImageMove();
        move.start(move);

    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        {
            Image image = new Image(Asset.getAsset().getTexture("hand1.png"));
            addActor(image);
            image.setScale(4,4);
            image.setDebug(true);
        }
        {
            float offsetX = 50;
            float offsetY = 50;
            Image image = new Image(Asset.getAsset().getTexture("hand1.png"));
            addActor(image);
            image.setOrigin(offsetX, offsetY);
            image.addAction(Actions.scaleTo(4, 4, 0.5f));
            image.setDebug(true);
            image.setPosition(offsetX*3,offsetX*3);
        }
    }
}
