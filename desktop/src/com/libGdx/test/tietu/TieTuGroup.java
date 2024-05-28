package com.libGdx.test.tietu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

public class TieTuGroup extends Group {
    protected ShaderProgram shaderProgram;
    private Texture model;
    public TieTuGroup(){
        this.model = Asset.getAsset().getTexture("bg_2.png");
        shaderProgram = new ShaderProgram(
                Gdx.files.internal("learn/txt.vert"),
                Gdx.files.internal("learn/txt.frag"));
        Texture texture = Asset.getAsset().getTexture("L_Shaped.png");
        Image bb = new Image(texture){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.flush();
                batch.setShader(shaderProgram);
                Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
                model.bind();
                shaderProgram.setUniformf("ratw",getWidth()/model.getWidth());
                shaderProgram.setUniformf("rath",getHeight()/model.getHeight());

                shaderProgram.setUniformi("u_texture1",1);
                Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                super.draw(batch, parentAlpha);
                batch.setShader(null);
            }
        };

        addActor(bb);
        setSize(bb.getWidth(),bb.getHeight());

    }
}
