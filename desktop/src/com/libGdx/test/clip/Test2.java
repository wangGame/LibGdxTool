package com.libGdx.test.clip;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.clip.Imxx;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/10/26 15:48
 */
public class Test2 extends LibGdxTestMain {
    public static void main(String[] args) {
        Test2 test2 = new Test2();
        test2.start(test2);
    }


    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        Image image = new Image(Asset.getAsset().getTexture("7.png"));
        Test02 test02 = new Test02(image);
        addActor(test02);
        test02.setClipWidth(130);

//        ImageXT xt = new ImageXT();
//        stage.addActor(xt);

    }
}
