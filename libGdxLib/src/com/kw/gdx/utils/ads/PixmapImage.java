package com.kw.gdx.utils.ads;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class PixmapImage {
    private Texture texture;
    public PixmapImage(int bannerWidth, int bannerHight){
        Pixmap pixmap = new Pixmap(bannerWidth,bannerHight,Pixmap.Format.RGBA8888);
        for (int i = 0; i < bannerWidth; i++) {
            for (int i1 = 0; i1 < bannerHight; i1++) {
                pixmap.drawPixel(i,i1, Color.WHITE.toIntBits());
            }
        }
        texture = new Texture(pixmap);
    }

    public Texture getPixmap() {
        return texture;
    }
}
