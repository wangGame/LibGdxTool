package com.kw.gdx.listener;


import com.kw.gdx.sound.AudioProcess;
import com.kw.gdx.sound.AudioType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class OrdinaryButtonListener extends ButtonListener {
    private String audioName;

    public OrdinaryButtonListener(Actor target){
        super(target);
    }

    public OrdinaryButtonListener(float scale){
        super(scale);
    }

    public OrdinaryButtonListener(String audioName){
        this.audioName = audioName;
    }

    public OrdinaryButtonListener(){
        super();
    }

    @Override
    public void clickEffect() {
        super.clickEffect();
        if (audioName == null) {
            AudioProcess.playSound(AudioType.click);
        }else if (!"".equals(audioName)){
            AudioProcess.playSound(audioName);
        }
    }

    @Override
    public void touchDownEffect() {

    }

    @Override
    protected void releaseEffect() {
        super.releaseEffect();
    }
}
