package com.libGdx.test;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.spine.SpineActor;
import com.libGdx.test.base.LibGdxTestMain;

public class AppC extends LibGdxTestMain {
    public static void main(String[] args) {
        AppC appC = new AppC();
        appC.start();
    }

    private float timex = 0;

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

//        NewBa baX = new NewBa();
//        baX.setBezier(
//                0,
//                0, //开始位置
//                0.533f,
//                0,
//                0.579f,
//                108.22f,
//                0.5333f,
//                160.38f
//        );
//
//        NewBa baY = new NewBa();
//        baY.setBezier(
//                0.5333f,
//                0, //开始位置
//                0.533f,
//                0,
//                0.579f,
//                88.15f,
//                0.6667F,
//                163.22f
//        );
//        Image image = new Image(Asset.getAsset().getTexture("assets/7.png")){
//            @Override
//            public void act(float delta) {
//                super.act(delta);
//                timex += delta;
//                float bezierValue = baX.getBezierValue(timex);
//                float bezierValue1 = baY.getBezierValue(timex);
//                setPosition(bezierValue,bezierValue1);
//
//            }
//        };
//        stage.addActor(image);


//        TextureAtlas atlas = new TextureAtlas("assets/doub/bx_ckqx.atlas");
        Asset.getAsset().getAssetManager().load("assets/doub/bx_ckqx.atlas", TextureAtlas.class);
        Asset.getAsset().getAssetManager().finishLoading();
        SpineActor actor = new SpineActor("assets/doub/bx_ckqx");
        stage.addActor(actor);
        actor.setAnimation("bx_xiao",false);
        actor.setPosition(500,500);
    }


}
