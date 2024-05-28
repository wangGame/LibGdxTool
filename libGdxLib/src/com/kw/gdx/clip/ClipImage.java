package com.kw.gdx.clip;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/27 10:14
 */
public class ClipImage extends Group {
    public ClipImage(){
        setSize(600,600);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();
        if (clipBegin(0,0,570,getHeight()/2)) {
            super.draw(batch, parentAlpha);
            batch.flush();
            clipEnd();
        }
        batch.flush();
    }
}
