package com.libGdx.test.touch;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ImageActor extends Image {
    private Rectangle rectangle = new Rectangle();
    public Rectangle getRectangle(){
        return rectangle.set(getX(),getY(),getWidth(),getHeight());
    }
}
