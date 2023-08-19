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
        ShaderGroup shaderGroup = new ShaderGroup();
        stage.addActor(shaderGroup);
        Image image = new Image(Asset.getAsset().getTexture("common/white_bg.png"));
        shaderGroup.addActor(image);
        image.setSize(1000,1000);
    }
}
