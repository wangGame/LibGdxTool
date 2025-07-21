package com.libGdx.test.lizi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.listener.OrdinaryButtonListener;

public class ProcessGroup extends Group {
    private  Image proBar;
    private Image pro;
    private float min = 0;
    private float max = 1000;
    private Label prcess;
    private float prcessValue;
    private Runnable runnable;
    public ProcessGroup(float width){
        Image processBg = new Image(
            new NinePatch(Asset.getAsset().getTexture("pic/bar_progress_1.png"),20,20,17,17)
        );
        addActor(processBg);
        setSize(width,processBg.getHeight());

        processBg.setWidth(width);

        pro = new Image(
                new NinePatch(Asset.getAsset().getTexture("pic/bar_progress_2.png"),20,20,17,17)
        );
        addActor(pro);
        pro.setWidth(width);
        pro.setPosition(getWidth()/2f,getHeight()/2f, Align.center);


        prcess = new Label("0",new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("font/Manrope-ExtraBold_60_1.fnt");
        }});
        addActor(prcess);
        prcess.setPosition(getWidth(),getHeight()/2f,Align.left);


        proBar = new Image(Asset.getAsset().getTexture("pic/button_switch_1.png"));
        addActor(proBar);
        proBar.setY(getHeight()/2f, Align.center);

        addListener(new OrdinaryButtonListener(1){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                setProceeValue(x);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                setProceeValue(x);
            }
        });

        Gdx.app.postRunnable(()->{
            setProceeValue(0);
        });
    }

    private void setProceeValue(float x) {
        proBar.setX(x,Align.center);
        pro.setWidth(x);
        prcessValue = min + x / getWidth() * (max - min);
        System.out.println(getName()+" => "+x);
        prcess.setText((int) (prcessValue));

        if (runnable!=null) {
            runnable.run();
        }
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public float getPrcessValue() {
        return prcessValue;
    }
}
