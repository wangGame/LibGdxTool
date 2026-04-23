package com.libGdx.test.color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ImageColor extends Group {
    public Color color;
    public ImageColor(int index){
        Pixmap pixMap = getPixMap();
        Color color = new Color();
        color.fromHsv(index, 0.7F, 0.8F);
        Image image = new Image(new Texture(pixMap));
        color.a = 1;
        addActor(image);
        image.setColor(color);
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("touch color "+color);
            }
        });
        setSize(50,50);
    }


    public Pixmap getPixMap(){
        Pixmap pixmap = new Pixmap(40,40,Pixmap.Format.RGBA8888);
        for (int i = 0; i < 40; i++) {
            for (int i1 = 0; i1 < 40; i1++) {
                pixmap.drawPixel(i,i1,Color.WHITE.toIntBits());
            }
        }
        return pixmap;
    }

}
