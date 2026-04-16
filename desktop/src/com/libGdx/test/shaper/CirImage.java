package com.libGdx.test.shaper;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.shape.CirGroup;

public class CirImage extends CirGroup {
    public CirImage(ShapeRenderer shapeRenderer){
        super(shapeRenderer);

        Image image = new Image(Asset.getAsset().getTexture("out3.png"));
        addActor(image);
        image.setSize(10,10);
        setStartModelTest(true);
        setQuf(true);
    }

    @Override
    protected void drawCir() {
        super.drawCir();
        sr.circle(0,00,5);
    }
}
