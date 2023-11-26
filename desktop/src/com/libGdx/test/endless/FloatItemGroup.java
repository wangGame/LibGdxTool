package com.libGdx.test.endless;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.cocosload.CocosResource;

/**
 * @Auther jian xian si qi
 * @Date 2023/10/23 22:36
 */
public class FloatItemGroup extends Group {
    protected Actor rootGroup;
    protected float offsetY;
    protected float baseGroupX;
    public FloatItemGroup(int index){
        rootGroup = new Image(Asset.getAsset().getTexture("pic/"+index+".jpg"));
        addActor(rootGroup);
        setSize(rootGroup.getWidth(),rootGroup.getHeight()+30);
        rootGroup.setY(25);
    }

    public void updateBaseGroupX(){
        baseGroupX = getX();
    }


    protected void init(){


    }

    protected void playLevel() {

    }

    protected void playLevelRest(){

    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    protected float offsetX;
    public void setOffsetX(float scrollX) {
        this.offsetX = scrollX;
    }

    public void dragMoveX(float xxx){
        float v = baseGroupX - xxx;
        setX(v);
    }


    public void updateX() {
        setOffsetX(getX());
    }

    public void userResume() {

    }

    public void updateFloatGroup() {

    }
}
