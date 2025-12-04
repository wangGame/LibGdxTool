package com.libGdx.test.cir;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.shader.BaseGroup;

public class CirActor1 extends BaseGroup {
    private float touch[] = new float[2];
    public CirActor1() {
        super("shader/energy/wave.vert", "shader/energy/wave.glsl");
        Image image = new Image(Asset.getAsset().getTexture("hmbb.jpg"));
        addActor(image);
        setSize(image.getWidth(),image.getHeight());
        image.setPosition(getWidth()/2.f,getHeight()/2.f, Align.center);

        setDebug(true);

        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touch[0] = (x / image.getWidth()) + 0.5f;
                touch[1] = 1.0f - (y / image.getHeight()) - 0.5f;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                touch[0] = (x / image.getWidth());
                touch[1] = 1.0f - (y / image.getHeight());

            }
        });
    }

    @Override
    public void setPar() {
        super.setPar();
        program.setUniform2fv("u_point",touch,0,2);
    }
}
