package com.libGdx.test.mult;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

import jdk.nashorn.internal.ir.CallNode;

public class TestGroup extends Group {
    public TestGroup(){
        Image image = new Image(Asset.getAsset().getTexture("assets/0_1_41_512.jpg"));
        addActor(image);
        Image image1 = new Image(Asset.getAsset().getTexture("assets/zpdd/ty3.png")){
//        Image image1 = new Image(Asset.getAsset().getTexture("assets/bg_2.png")){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                int src = batch.getBlendSrcFunc();
                int dst = batch.getBlendDstFunc();
                Gdx.gl.glEnable(GL20.GL_BLEND);
//                multiply(GL20.GL_DST_COLOR, GL20.GL_DST_COLOR, GL20.GL_ONE_MINUS_SRC_ALPHA)
                batch.setBlendFunction(GL20.GL_DST_COLOR , GL20.GL_ONE_MINUS_SRC_ALPHA);
                super.draw(batch, parentAlpha);
                batch.setBlendFunction(src,dst);
                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
        };
        addActor(image1);
    }
}
