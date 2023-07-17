package com.libGdx.test.other;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/25 17:59
 */
public class LineTes extends LibGdxTestMain {
    public static void main(String[] args) {
        LineTes tes = new LineTes();
        tes.start(tes);
    }
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        float xx = 0;
        for (int i = 0; i < 10; i++) {
            Image image = new Image(Asset.getAsset().getTexture("assets/heng.png"));
            image.setX(xx);
            xx+=image.getWidth();
            stage.addActor(image);
        }
    }
}
