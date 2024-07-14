package com.libGdx.test.effect;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.animation.effect.EffectTool;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.GameInfo;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/21 14:30
 *
 */
@GameInfo(width = 2080,height = 1920,batch = Constant.COUPOLYGONBATCH,viewportType = Constant.EXTENDVIEWPORT)
public class EffectTest extends LibGdxTestMain {
    public static void main(String[] args) {
        EffectTest test = new EffectTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        //x 镜像
        {
            EffectTool tool = new EffectTool("lizi/1");
            tool.setLoop(true);
            stage.addActor(tool);
            tool.setPosition(600, 600);
            tool.setFlipX();
            tool.getColor().a = 0.7f;

        }
//        {
//            EffectTool tool = new EffectTool("lizi/1");
//            tool.setLoop(true);
//            stage.addActor(tool);
//            tool.setPosition(600,600);
//        }
//        //裁剪
//        {
//            EffectTool tool = new EffectTool("lizi/1");
//            tool.setLoop(true);
//            stage.addActor(tool);
//            tool.setPosition(600,900);
//            tool.setClip(true);
//            tool.setClipW(1400);
//            tool.setClipH(1400);
//        }
    }
}
