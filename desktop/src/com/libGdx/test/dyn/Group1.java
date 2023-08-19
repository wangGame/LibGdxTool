package com.libGdx.test.dyn;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.kw.gdx.asset.Asset;

/**
 * @Auther jian xian si qi
 * @Date 2023/8/4 12:26
 */
public class Group1 extends Group {
    private Image image;
    private Label l;
    private int index;
    public Group1(int index){
        this.index = index;
        image = new Image(Asset.getAsset().getTexture("fangshiyi.png"));
        addActor(image);
        setSize(image.getWidth(),image.getHeight());
        l = new Label(index+"",new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("frmb-40.fnt");
        }});
        addActor(l);
        l.setColor(Color.BLACK);
        System.out.println(index);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}