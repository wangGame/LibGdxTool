package kw.artpuzzle.dialog;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.sound.AudioProcess;
import com.kw.gdx.sound.AudioType;
import com.kw.gdx.utils.basier.BseInterpolation;
import com.kw.gdx.view.dialog.base.BaseDialog;

import kw.artpuzzle.flurry.FlurryUtils;
import kw.artpuzzle.pref.ArtPuzzlePreferece;
import kw.artpuzzle.screen.Rate01Screen;

@ScreenResource("cocos/SettingDialog.json")
public class SettingDialog extends BaseDialog {
    @Override
    public void show() {
        super.show();
        Actor setting_close = findActor("setting_close");
        setting_close.setTouchable(Touchable.enabled);
        setting_close.setOrigin(Align.center);
        setting_close.addListener(new OrdinaryButtonListener(0.8f){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                dialogManager.closeDialog(SettingDialog.this);
            }
        });
//        create("setting_soundSwitch",new SpineActor("spine/dianjishengyin"),1);
//        create("setting_musicSwitch",new SpineActor("spine/1_3_setting"),2);
//        create("setting_hapticSwitch",new SpineActor("spine/zhendong"),3);
        switchTool("setting_soundSwitch");
        switchTool("setting_hapticSwitch");
        settingTool("setting_soundSwitch",1);
        settingTool("setting_hapticSwitch",2);
        findActor("setting_rateSwitch").setOrigin(Align.center);
        findActor("setting_rateSwitch").addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                dialogManager.showDialog(new Rate01Screen(new Runnable() {
                    @Override
                    public void run() {

                    }
                }));
            }
        });
    }

//    public void create(String name,SpineActor spineActor,int index){
//        Group actor = findActor(name);
//        Actor switch_lv = actor.findActor("switch_lv");
//        actor.addActor(spineActor);
//        if (index == 1) {
//            spineActor.setPosition(switch_lv.getX(Align.center) - 9, switch_lv.getY(Align.center));
//        }else if (index == 2){
//            spineActor.setPosition(switch_lv.getX(Align.center)+ 8, switch_lv.getY(Align.center)+3);
//        }else if (index == 3){
//            spineActor.setPosition(switch_lv.getX(Align.center) +1, switch_lv.getY(Align.center)+1);
//        }
//
//        spineActor.setName("switch_lv");
//        switch_lv.remove();
//        Actor switch_huang = actor.findActor("switch_huang");
//        SpineActor kuang = new SpineActor("spine/kaiqiguanbi");
//        actor.addActor(kuang);
//        kuang.setPosition(switch_huang.getX(Align.center),switch_lv.getY(Align.center)+3);
//        switch_huang.remove();
//        kuang.setName("switch_huang");
//    }


    private void settingTool(String key,int index) {
        Group setting_soundSwitch = findActor(key);
        Group openclose = setting_soundSwitch.findActor("openclose");
        openclose.setTouchable(Touchable.enabled);
        openclose.setOrigin(Align.center);
        setting_soundSwitch.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioProcess.playSound(AudioType.clickA);
                super.clicked(event, x, y);
                ArtPuzzlePreferece.getInstance().updateSwitch(key);
                switchTool(key,index);
                if (key.equals("setting_soundSwitch")){
                    ArtPuzzlePreferece.getInstance().isSound();
                    if (Constant.isSound){
                        FlurryUtils.uiSoundOn();
                    }else {
                        FlurryUtils.uiSoundOff();
                    }
                }else if (key.equals("setting_hapticSwitch")){
                    ArtPuzzlePreferece.getInstance().isHaptic();
                    if (Constant.isSound){
                        FlurryUtils.uiHapicOn();
                    }else {
                        FlurryUtils.uiHapicOff();
                    }
                }
            }
        });

//        Group setting_soundSwitch = findActor(key);
//        Actor switch_bg = setting_soundSwitch.findActor("switch_bg");
//        Actor switch_circle_8 = setting_soundSwitch.findActor("switch_circle_8");
//        if (ArtPuzzlePreferece.getInstance().isSound(key)){
//            switch_bg.setColor(Color.valueOf("#37966F"));
//            switch_circle_8.setX(450.00f,Align.center);
//        }else {
//            switch_bg.setColor(Color.valueOf("#D5D3C8"));
//            switch_circle_8.setX(410.00f,Align.center);
//        }
    }

    private void switchTool(String key){
        Group setting_soundSwitch = findActor(key);
        Actor switch_bg = setting_soundSwitch.findActor("switch_bg");
        Actor switch_circle_8 = setting_soundSwitch.findActor("switch_circle_8");
        if (ArtPuzzlePreferece.getInstance().isEffect(key)){
            switch_circle_8.setX(67.00f,Align.center);
            switch_bg.setColor(Color.valueOf("#509E6A"));
        }else {
            switch_bg.setColor(Color.valueOf("#D8D2B5"));
            switch_circle_8.setX(30.00f,Align.center);
        }
    }

    private void switchTool(String key,int index) {
        Group setting_soundSwitch = findActor(key);
        Actor switch_bg = setting_soundSwitch.findActor("switch_bg");
        Actor switch_circle_8 = setting_soundSwitch.findActor("switch_circle_8");
        if (ArtPuzzlePreferece.getInstance().isEffect(key)){
//            switch_bg.setColor(Color.valueOf("#37966F"));
            switch_bg.addAction(Actions.color(Color.valueOf("#509E6A"),0.1667f,new BseInterpolation(0.25f,0.0f,1.0f,1.0f)));
//            switch_circle_8.setX(67.00f,Align.center);
            float y = switch_circle_8.getY(Align.center);
            switch_circle_8.addAction(
                    Actions.sequence(
                            Actions.moveToAligned(67.0f + 3.15f,y,Align.center,0.16667f,
                                    new BseInterpolation(0.0f,0.01f,0.75f,1.0f)),
                            Actions.moveToAligned(67.0f,y,Align.center,0.1f,
                                    new BseInterpolation(0.25f,0.0f,1.0f,1.0f))
                    ));
        }else {
//            switch_bg.setColor(Color.valueOf("#D5D3C8"));
//            switch_circle_8.setX(30.00f,Align.center);
            float y = switch_circle_8.getY(Align.center);
            switch_bg.addAction(Actions.color(Color.valueOf("#D8D2B5"),0.1667f,new BseInterpolation(0.25f,0.0f,1.0f,1.0f)));
            switch_circle_8.addAction(
                    Actions.sequence(
                            Actions.moveToAligned(30.0f - 3.15f,y,Align.center,0.16667f,
                                    new BseInterpolation(0.0f,0.0f,0.75f,1.0f)),
                            Actions.moveToAligned(30.0f,y,Align.center,0.1f,
                                    new BseInterpolation(0.25f,0,1.0f,1.0f))
                    ));
        }
    }
}
