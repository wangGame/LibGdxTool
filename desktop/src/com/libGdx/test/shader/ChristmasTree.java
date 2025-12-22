package com.libGdx.test.shader;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.GameInfo;
import com.libGdx.test.base.LibGdxTestMain;

public class ChristmasTree extends LibGdxTestMain {
    public static void main(String[] args) {
        screenWidth = 2060;
        screenHight = 1600;
        ChristmasTree christmasTree = new ChristmasTree();
        christmasTree.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        TreeGroup treeGroup = new TreeGroup();
//        addActor(treeGroup);
//
//        OpenAiGroup openAiGroup = new OpenAiGroup();
//        addActor(openAiGroup);


//        Image image = new Image(Asset.getAsset().getTexture("white.png"));
//        addActor(image);
//
//        image.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
//        image.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f,Align.center);
        ShaderImage shaderImage = new ShaderImage();
        addActor(shaderImage);
        shaderImage.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f, Align.center);
    }
}
