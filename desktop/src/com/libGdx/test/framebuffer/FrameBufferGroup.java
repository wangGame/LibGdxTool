package com.libGdx.test.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;

public class FrameBufferGroup extends Group {
    private FrameBuffer frameBuffer;
    public FrameBufferGroup(){
        frameBuffer = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                (int) Constant.GAMEWIDTH,
                (int) Constant.GAMEHIGHT,
                false);

        setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();

        frameBuffer.begin();
        Gdx.gl.glClearColor(245.0f/255.0f,238.0f/255.0f,215.0f/255.0f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        super.draw(batch, parentAlpha);
        batch.flush();
        frameBuffer.end();



    }

    public TextureRegion getBufferTexture(float globalScale){
        Texture colorBufferTexture = frameBuffer.getColorBufferTexture();
        TextureRegion region = new TextureRegion(colorBufferTexture);
        region.flip(false,true);
        return region;
    }
}
