package com.kw.gdx.utils.framebuffer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class FbDemo extends Group {
    public FbDemo(){
        Image image1 = new Image(new Texture("fanye.png"));
        Group group = new Group();
        group.addActor(image1);
        FrameBufferDemo demo = new FrameBufferDemo(group);
        Texture texture = demo.getTexture();
        Image image = new Image(texture);
        addActor(image);
    }
}
