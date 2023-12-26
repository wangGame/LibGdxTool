package com.libGdx.test.clip;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/27 10:16
 */
public class Test1 extends LibGdxTestMain {
    public static void main(String[] args) {
        Test1 test1 = new Test1();
        test1.start(test1);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        ClipImage image = new ClipImage();
//        Image image1 = new Image(Asset.getAsset().getTexture("shuoming.png"));
//        stage.addActor(image);
//        image.addActor(image1);


//        Group group = new Group(){
//            @Override
//            public void draw(Batch batch, float parentAlpha) {
//                if (clipBegin(0,0,10,10)){
//                    super.draw(batch, parentAlpha);
//                    clipEnd();
//                }
//            }
//        };
//        stage.addActor(group);
//        Group g = new Group();
//        g.setSize(1000,1000);
//        Image image1 = new Image(Asset.getAsset().getTexture("shuoming.png"));
//        group.addActor(g);
//        g.addActor(image1);
    }
}
