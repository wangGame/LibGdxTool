package com.libGdx.test.shader;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class ShaderTest extends LibGdxTestMain {
    public static void main(String[] args) {
        ShaderTest test = new ShaderTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Image image = new Image(Asset.getAsset().getTexture("common/white_bg.png"));
        image.setSize(1000,1000);
//        demo01(stage,image);
        demo02(stage,image);
    }

    public void demo02(Stage stage,Image image){
        ColorCirGroup shaderGroup = new ColorCirGroup();
        stage.addActor(shaderGroup);
        shaderGroup.addActor(image);
        shaderGroup.setSize(image.getWidth(),image.getHeight());
    }

    public void demo01(Stage stage,Image image){
        ShaderGroup shaderGroup = new ShaderGroup();
        stage.addActor(shaderGroup);
        shaderGroup.addActor(image);
        shaderGroup.setSize(image.getWidth(),image.getHeight());
    }
}
