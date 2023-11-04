package com.libGdx.test.freecenterscale;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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

    /**
     * 计算
     * 1.(x1,y1) move (x2 ,y2)
     * 2.
     * 2.1.start position center  x1 y1
     * 2.2.target scale center (scx2,scy2) // scale center
     * 2.3.offsetX = (scx2 - scx1)
     *     offsetY = (scy2 -scy1)
     *
     * 3. x1,y1 - offsetX,offsetY
     *
     * 4.scale
     *  4.1 x1,y1 - offsetX,offsetY + (offsetX,offsetY) * scale
     *   target pos x2 y2 - (1 - scale) * (offsetX offsetY)
     * @param stage
     */

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


            //0,0  -- > 0,0
            //scale center change  -(1-scale) * (offsetX,offsetY)
        }
    }
}
