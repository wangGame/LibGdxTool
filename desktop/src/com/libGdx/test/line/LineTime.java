package com.libGdx.test.line;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kw.gdx.action.NumAction;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class LineTime extends LibGdxTestMain {
    public static void main(String[] args) {
        LineTime lineTime = new LineTime();
        lineTime.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Texture texture = Asset.getAsset().getTexture("assets/fangshier.png");
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion region = new TextureRegion(texture);
        Image image = new Image(region);
        addActor(image);

        NumAction action = new NumAction(0,1000){
            @Override
            public boolean act(float delta) {
                region.setRegionWidth((int) getValue());
                image.setWidth(region.getRegionWidth());
                return super.act(delta);
            }

            @Override
            protected void end() {
                super.end();
                region.setRegionWidth((int) getValue());

                image.setWidth(region.getRegionWidth());
            }
        };
        action.setDuration(3);
        image.addAction(
                action
        );

    }
}
