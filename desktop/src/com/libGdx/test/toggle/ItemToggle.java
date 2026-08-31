package com.libGdx.test.toggle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.toggle.ToggleBase;

public class ItemToggle extends ToggleBase {
    private Image image;
    public ItemToggle() {
        super();
        setSize(200,200);
        setDebug(true);
        image = new Image(Asset.getAsset().getTexture("assets/7.png"));
        addActor(image);
        image.setPosition(getWidth()/2f,getHeight()/2f, Align.center);
    }

    @Override
    public void select() {
        super.select();
        image.setColor(Color.RED);
    }

    @Override
    public void unSelect() {
        super.unSelect();
        image.setColor(Color.WHITE);
    }
}
