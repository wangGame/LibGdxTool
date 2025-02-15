package com.libGdx.test.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.asset.Asset;

public class SpriteGroup extends Group {
    private Sprite sprite;
    public SpriteGroup(){
        sprite = Asset.getAsset().getSprite("assets/fangshier.png");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        sprite.setPosition(0,0);
        sprite.draw(batch);
        sprite.setPosition(100,100);
        sprite.draw(batch);
    }
}
