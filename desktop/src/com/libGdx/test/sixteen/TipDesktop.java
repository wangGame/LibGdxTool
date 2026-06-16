package com.libGdx.test.sixteen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;


public class TipDesktop extends LibGdxTestMain {
    public static void main(String[] args) {
        TipDesktop tipDesktop = new TipDesktop();
        tipDesktop.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        //std
        {
            Image sting = new Image(Asset.getAsset().getTexture("ggg/setting/frm_guidance_1.png"));
            addActor(sting);
            sting.setX(Constant.GAMEWIDTH/2f, Align.center);
            sting.setDebug(true);
            System.out.println(sting.getWidth()+"   "+ sting.getHeight());
        }
        {
            TipBg tipBg = new TipBg(new TextureRegion(Asset.getAsset().getTexture("frm_guidance_1.png")),80,3,3,80,23,55);
            tipBg.setSize(896, 436);
            tipBg.setLeftWp(635);
            addActor(tipBg);
            tipBg.setY(500);
            tipBg.setDebug(true);
            tipBg.setX(Constant.GAMEWIDTH/2f, Align.center);
        }

        addActor(new Table(){{
            ProcessGroup timelongProcess = new ProcessGroup(600);
            add(timelongProcess).pad(50);
            timelongProcess.setName("timelongProcess");
            row();
            timelongProcess.setMin(1);
            timelongProcess.setMax(200);
            timelongProcess.setRunnable(()->{

            });
            ProcessGroup amplitude = new ProcessGroup(600);
            add(amplitude).pad(50);
            amplitude.setName("amplitude");
            amplitude.setMin(1);
            amplitude.setMax(255);
            row();

            BtnGroup ok = new BtnGroup("Ok");
            add(ok).pad(50);
            row();
            pack();
            setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f, Align.center);
        }});




//        TipBg tipBg = new TipBg(new TextureRegion(Asset.getAsset().getTexture("frm_guidance_1.png")),700,500,100);
//        addActor(tipBg);
//        tipBg.setY(600);

    }
}
