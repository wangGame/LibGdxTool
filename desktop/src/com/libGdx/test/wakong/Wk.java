package com.libGdx.test.wakong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

public class Wk extends Group {
    public Wk(){
        ShaderProgram program = new ShaderProgram(
                Gdx.files.internal("wk/hui.vert"),
                Gdx.files.internal("wk/hui.frag")
                );


        Image image= new Image(Asset.getAsset().getTexture("0_1_41_512.jpg")){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.flush();
                batch.setShader(program);
                program.setUniformf("posx",-0.8f);
                program.setUniformf("posy",-0.8f);
                program.setUniformf("rr",0.05f);
                super.draw(batch, parentAlpha);
                batch.flush();
                batch.setShader(null);
            }
        };
        addActor(image);
        setSize(image.getWidth(),image.getHeight());
    }
}
