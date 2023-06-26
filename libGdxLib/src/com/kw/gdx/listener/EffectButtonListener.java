package com.kw.gdx.listener;

import com.kw.gdx.sound.AudioProcess;
import com.kw.gdx.sound.AudioType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class EffectButtonListener extends ButtonListener {
    private String audioName;
    private Color targetColor = Color.valueOf("969696ff");

    public EffectButtonListener(){

    }

    public EffectButtonListener(Actor target,Color targetColor){
        super(target);
        this.targetColor = targetColor;
    }

    public EffectButtonListener(Actor target){
        super(target);
    }

    public EffectButtonListener(Actor target,String audio){
        super(target);
        this.audioName = audio;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public EffectButtonListener(float scale){
        super(scale);
    }

    public EffectButtonListener(String audioName){
        this.audioName = audioName;
    }

    public EffectButtonListener(Group group, Color black, float v){
        this(group,black);
        targetScale = v;
    }

    public EffectButtonListener(Group group, Color black, float v, Action old){
        this(group,black);
        targetScale = v;
        this.old = old;
    }

    @Override
    public void clickEffect() {
        super.clickEffect();
        if (audioName == null) {
//            AudioProcess.playSound(AudioType.BUTTONPRESS);
        }else if (!"".equals(audioName)){
            AudioProcess.playSound(audioName);
        }
    }

    @Override
    public void touchDownEffect() {
        if (target!=null) {
            if (target instanceof Group) {
                Group target = (Group) this.target;
                for (Actor child : target.getChildren()) {
//                    child.addAction(Actions.sequence(Actions.color(Color.valueOf("d2d2d2ff"),0.20000005F,
//                            new BseInterpolation(0.631F,0.59F,1.0F,0.96F)),
//                            Actions.color(Color.valueOf("ffffffff"),0.1667F)));
                    child.addAction(Actions.color(targetColor,0.1F));
                }
            }else {
//                target.addAction(Actions.sequence(Actions.color(Color.valueOf("d2d2d2ff"),0.20000005F,
//                        new BseInterpolation(0.631F,0.59F,1.0F,0.96F)),
//                        Actions.color(Color.valueOf("ffffffff"),0.1667F)));
                target.addAction(Actions.color(targetColor,0.1F));
            }
        }
    }

    @Override
    protected void releaseEffect() {
        super.releaseEffect();
        if (target!=null) {
            if (target instanceof Group) {
                Group target = (Group) this.target;
                for (Actor child : target.getChildren()) {
//                    child.addAction(Actions.sequence(Actions.color(Color.valueOf("d2d2d2ff"),0.20000005F,
//                            new BseInterpolation(0.631F,0.59F,1.0F,0.96F)),
//                            Actions.color(Color.valueOf("ffffffff"),0.1667F)));
                    child.addAction(Actions.sequence(
                            Actions.color(Color.valueOf("FFFFFFFF"),0.1F),
                            Actions.run(()->{
                                afterEffect();
                            })));
                }
            }else {
//                target.addAction(Actions.sequence(Actions.color(Color.valueOf("d2d2d2ff"),0.20000005F,
//                        new BseInterpolation(0.631F,0.59F,1.0F,0.96F)),
//                        Actions.color(Color.valueOf("ffffffff"),0.1667F)));
                target.addAction(Actions.sequence(
                        Actions.color(Color.valueOf("FFFFFFFF"),0.1F),
                        Actions.run(()->{
                            afterEffect();
                        })));
            }
        }
    }

    public void afterEffect(){

    }
}
