package com.libGdx.test.effect;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.BaseGame;
import com.kw.gdx.animation.effect.EffectTool;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.screen.BaseScreen;
import com.libGdx.test.base.LibGdxTestBase;
import com.libGdx.test.base.LinGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/21 14:30
 *
 */
@GameInfo(width = 2080,height = 1920,batch = Constant.COUPOLYGONBATCH,viewportType = Constant.EXTENDVIEWPORT)
public class EffectTest extends LinGdxTestMain {
    public static void main(String[] args) {
        EffectTest test = new EffectTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        //x 镜像
//        {
//            EffectTool tool = new EffectTool("lizi/1");
//            tool.setLoop(true);
//            stage.addActor(tool);
//            tool.setPosition(600, 600);
//            tool.setFlipX();
//        }
//        {
//            EffectTool tool = new EffectTool("lizi/1");
//            tool.setLoop(true);
//            stage.addActor(tool);
//            tool.setPosition(600,600);
//        }
        //裁剪
        {
            EffectTool tool = new EffectTool("lizi/1");
            tool.setLoop(true);
            stage.addActor(tool);
            tool.setPosition(600,900);
            tool.setClip(true);
            tool.setW(1400);
            tool.setH(1400);
        }
    }
}
