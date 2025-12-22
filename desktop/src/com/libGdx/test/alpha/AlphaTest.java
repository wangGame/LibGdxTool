package com.libGdx.test.alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

//多通道渲染：你可能只想更新某些颜色通道，而保留其他通道不变。
//
//深度/模板缓冲优化：在只更新深度或模板缓冲而不修改颜色时，可以将颜色写入屏蔽掉，提高性能。
//
//后期效果/混合：在特定效果下，只渲染部分颜色通道，比如红色通道高光。
public class AlphaTest extends Group {
    private Image image;

    public AlphaTest() {
        image = new Image(new Texture("assets/3_34_24.png"));
        addActor(image);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gdx.gl.glEnable(GL20.GL_ALPHA);
        Gdx.gl.glColorMask(false, false, false, true);
        super.draw(batch, parentAlpha);
        Gdx.gl.glDisable(GL20.GL_ALPHA);
    }
}