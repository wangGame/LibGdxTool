package com.libGdx.test.shader;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.bullet.BulletFlow;

public class WaterShader extends LibGdxTestMain {
    public static void main(String[] args) {
        WaterShader shader = new WaterShader();
        shader.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        HuiDuZhuanC zhuanC = new HuiDuZhuanC();
//        addActor(zhuanC);
//        zhuanC.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f,Align.center);
//        zhuanC.setScale(3);


//        WaterGroup waterGroup = new WaterGroup();
//        addActor(waterGroup);
//        waterGroup.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f, Align.center);
//        waterGroup.setScale(0.5f);


        BulletFlow bulletFlow = new BulletFlow();
        addActor(bulletFlow);
        bulletFlow.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f,Align.center);
    }
}
