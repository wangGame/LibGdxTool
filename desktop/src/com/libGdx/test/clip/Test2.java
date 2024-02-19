package com.libGdx.test.clip;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
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


    private float widthx = 0;

    private Test02 test02;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        Image image = new Image(Asset.getAsset().getTexture("7.png"));
        test02 = new Test02(image){
            @Override
            public void act(float delta) {
                super.act(delta);
                widthx += delta*10;
                test02.setClipWidth(widthx);
            }
        };
        addActor(test02);
//        ImageXT xt = new ImageXT();
//        stage.addActor(xt);

    }
}
