package com.libGdx.test.scrollpanel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/5 18:21
 */
class DemoGroup extends Group {
    private Image image;
    private float offsetX = 200;
    private int index;
    public DemoGroup(int i){
        this.index = i;
        image = new Image(Asset.getAsset().getTexture("hand1.png"));
        image.setDebug(true);
        addActor(image);
        setSize(image.getWidth(),image.getHeight());
        image.setSize(image.getWidth(),image.getHeight());
        image.setPosition(getWidth()/2,getHeight()/2, Align.center);
        setOrigin(Align.center);
    }

    private Vector2 temp = new Vector2();
    @Override
    public void act(float delta) {
        super.act(delta);
        temp.set(getX(Align.center),getY(Align.center));
        this.getParent().localToStageCoordinates(temp);
        if (temp.x > Constant.GAMEWIDTH/2.0f - offsetX && temp.x<Constant.GAMEWIDTH/2.0f + offsetX){
            float v = Math.abs(temp.x - Constant.GAMEWIDTH / 2.0f);

            setScale(1.0f+0.4f*(Math.abs(v - offsetX)/offsetX));
        }else{
            setScale(1.0f);
        }
    }
}
