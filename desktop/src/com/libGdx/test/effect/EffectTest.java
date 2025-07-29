package com.libGdx.test.effect;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.action.NumAction;
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
//            EffectTool tool = new EffectTool("lizi/tubiao_lizi");
//            tool.setLoop(true);
//            stage.addActor(tool);
//            tool.setPosition(600, 100);
//            tool.getColor().a = 0.7f;
//            tool.setFlipX();
//            tool.boundbox();

        }

        {
            EffectTool tool = new EffectTool("lizi/ttt");
            tool.setLoop(true);
            stage.addActor(tool);
            tool.setPosition(0, 600);
            tool.getColor().a = 0.7f;
            tool.setFlipX();
            tool.boundbox();

            SequenceAction sAction = new SequenceAction();
            NumAction action = new NumAction();
            action.setLoop(true);
            action.setStart(0);
            action.setEnd(1000);
            action.setDuration(3);
            sAction.addAction(action);
            NumAction actionM = new NumAction();
            actionM.setStart(1000);
            actionM.setEnd(0);
            actionM.setDuration(3);
            actionM.setLoop(true);
            sAction.addAction(actionM);

            stage.addAction(Actions.forever(sAction));


            action.setUpdateRunnable(new Runnable() {
                @Override
                public void run() {

                    System.out.println((float) action.getValue());
                    Array<ParticleEmitter> emitters = tool.getEffect().getEmitters();
                    for (ParticleEmitter emitter : emitters) {
                        ParticleEmitter.ScaledNumericValue spawnWidth = emitter.getSpawnWidth();
                        emitter.setSpawnWidth((float) action.getValue());
                        spawnWidth.setHigh((float) action.getValue());
                        spawnWidth.setHighMax((float) action.getValue());
                        spawnWidth.setHighMin((float) action.getValue());
                    }
                }
            });

            actionM.setUpdateRunnable(new Runnable() {
                @Override
                public void run() {
                    System.out.println((float) actionM.getValue());
                    Array<ParticleEmitter> emitters = tool.getEffect().getEmitters();
                    for (ParticleEmitter emitter : emitters) {
                        ParticleEmitter.ScaledNumericValue spawnWidth = emitter.getSpawnWidth();
                        emitter.setSpawnWidth((float) actionM.getValue());
                        spawnWidth.setHigh((float) actionM.getValue());
                        spawnWidth.setHighMax((float) actionM.getValue());
                        spawnWidth.setHighMin((float) actionM.getValue());
                    }
                }
            });
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
