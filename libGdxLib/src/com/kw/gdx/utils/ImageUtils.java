package com.kw.gdx.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class ImageUtils {
    public static void changeImageAtlas(Image image, TextureRegion atlas){
        Drawable drawable = image.getDrawable();
        if (drawable instanceof TextureRegionDrawable) {
            ((TextureRegionDrawable)drawable).setRegion(new TextureRegion(atlas));
        }else if (drawable instanceof SpriteDrawable){
            ((SpriteDrawable)drawable).setSprite(new Sprite(atlas));
        }
    }

    public static void changeImageTexture(Image image, Texture texture){
        Drawable drawable = image.getDrawable();
        float baseX = image.getX(Align.center);
        float baseY = image.getY(Align.center);

        if (drawable instanceof TextureRegionDrawable) {
            ((TextureRegionDrawable)drawable).setRegion(new TextureRegion(texture));
            image.setSize(texture.getWidth(),texture.getHeight());
        }else if (drawable instanceof SpriteDrawable){
            ((SpriteDrawable)drawable).setSprite(new Sprite(texture));
            image.setSize(texture.getWidth(),texture.getHeight());
        }
        image.setPosition(baseX,baseY,Align.center);
    }

    public static void changeImageNinePatchTexture(Image image, Texture texture,int left,int right,int top,int bottom){
        Drawable drawable = image.getDrawable();
        if (drawable instanceof NinePatchDrawable){
            ((NinePatchDrawable)drawable).setPatch(new NinePatch(texture,left,right,top,bottom));
        }
    }
}
