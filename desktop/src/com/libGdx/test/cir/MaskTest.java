package com.libGdx.test.cir;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @Auther jian xian si qi
 * @Date 2023/11/26 11:24
 */
public class MaskTest extends Group {
    private Image img;
    private Image mask;
    public MaskTest(){
        mask = new Image(new Texture("ad_progress.png"));
        img = new Image(new Texture(Gdx.files.internal("shuoming.png")));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        drawAlphaMask(batch);
        //画前景色
        drawForeground(batch, 0, 0, (int) mask.getWidth()/2, (int)mask.getHeight()/2);
    }

    private void drawForeground(Batch batchPara, int clipX, int clipY ,int clipWidth ,int clipHeight) {
        Gdx.gl.glColorMask(true, true, true, true);
        batchPara.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(clipX, clipY, clipWidth, clipHeight);
        img.draw(batchPara,1);
        batchPara.flush();
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }
    private void drawAlphaMask(Batch batch) {
        Gdx.gl.glColorMask(false, false, false, true);
        mask.draw(batch,1);
        batch.flush();
    }
}