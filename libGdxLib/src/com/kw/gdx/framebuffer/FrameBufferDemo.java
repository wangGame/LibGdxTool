package com.kw.gdx.framebuffer;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Group;

public class FrameBufferDemo extends Group{
    public FrameBuffer frameBuffer;
    public Group group;
    public FrameBufferDemo(Group group){
        this.group = group;
        frameBuffer = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                (int) group.getWidth(),
                (int) group.getHeight(),
                false);
        addActor(group);
    }

    public Texture getTexture(){
        return frameBuffer.getColorBufferTexture();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        frameBuffer.begin();
        group.draw(batch,parentAlpha);
        frameBuffer.end();
    }
}
