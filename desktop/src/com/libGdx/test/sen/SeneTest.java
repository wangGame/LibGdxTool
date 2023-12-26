package com.libGdx.test.sen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.spine.loader.SpineActor;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/26 7:26
 */
public class SeneTest extends Group {
    private Image image1;
    private SpineActor spineActor;
    private Cir cir;
    public SeneTest(){
        image1 = new Image(new Texture("assets/3_34_24.png"));
        addActor(image1);
//
//        addActor(image2);

        cir = new Cir(300,300,200);
        addActor(cir);
        spineActor = new SpineActor("assets/xx/finish");
        addActor(spineActor);
        spineActor.setPosition(20,100, Align.center);
        spineActor.setAnimation("1",true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();
        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_REPLACE);//第一次绘制的像素的模版值 0+1 = 1
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF);
        cir.draw(batch,parentAlpha);
        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 0x1, 0xFF);//等于1 通过测试 ,就是上次绘制的图 的范围 才通过测试。
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);//没有通过测试的，保留原来的，也就是保留上一次的值。
        spineActor.draw(batch,parentAlpha);
        batch.flush();
        Gdx.gl.glDisable(Gdx.gl.GL_STENCIL_TEST);
    }
}