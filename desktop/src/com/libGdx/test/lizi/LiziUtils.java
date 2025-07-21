package com.libGdx.test.lizi;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.animation.effect.EffectTool;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class LiziUtils extends LibGdxTestMain {

    public static void main(String[] args) {
        LiziUtils liziUtils = new LiziUtils();
        liziUtils.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);



        ProcessGroup processGroup = new ProcessGroup(Constant.GAMEWIDTH);
        addActor(processGroup);
        processGroup.setY(200);


        Group group = new Group();
        addActor(group);
        group.setScale(0.4f);

        gr



        EffectTool tool = new EffectTool("lizi/dz");
        group.addActor(tool);
        tool.setY(500);
        tool.setLoop(true);
        tool.setX(Constant.GAMEWIDTH/2f,Align.center);

        ParticleEffect effect = tool.getEffect();
        Array<ParticleEmitter> emitters = effect.getEmitters();
        System.out.println(emitters);


        processGroup.setRunnable(new Runnable() {
            @Override
            public void run() {
                for (ParticleEmitter emitter : emitters) {
//            emitter.spawnWidthDiff
                    ParticleEmitter.ScaledNumericValue spawnWidth = emitter.getSpawnWidth();
                    spawnWidth.setLowMin(0);

                    float prcessValue = processGroup.getPrcessValue();
                    spawnWidth.setHigh(prcessValue);

                }

            }
        });

    }
}
