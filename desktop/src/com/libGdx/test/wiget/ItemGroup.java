package com.libGdx.test.wiget;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

public class ItemGroup extends Group {
    private Image image;
    public ItemGroup() {
        setSize(200, 200);
        setDebug(true);
        image = new Image(Asset.getAsset().getTexture("assets/000.png"));
        image.setSize(200, 200);
        addActor(image);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (image!=null)
        image.setSize(getWidth(), getHeight());
    }
}
