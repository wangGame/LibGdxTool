package com.joker.domos.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.joker.domos.audio.Cue;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.event.simpleevent.EventManager;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.sign.SignListener;

public class TextButton2 extends Group {
    private SignListener signListener;
    private Cue cue;
    public TextButton2(String text) {
        super();
        Image btnBg = new Image(Asset.getAsset().getTexture("white.png"));
        addActor(btnBg);
        Label font = new Label(text, new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("font/Cali_75.fnt");
        }});
        addActor(font);
        btnBg.setSize(font.getPrefWidth() + 20, font.getPrefHeight() + 10);
        setSize(btnBg.getWidth(), btnBg.getHeight());
        font.setPosition(getWidth()/2f,getHeight()/2f, Align.center);
        font.setColor(Color.BLACK);

        addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(signListener!=null){
                    signListener.sign(cue);
                }
            }
        });
    }

    public void setEventName(Cue cue) {
        this.cue = cue;
    }

    public void setSignListener(SignListener<Cue> signListener) {
        this.signListener = signListener;
    }
}
