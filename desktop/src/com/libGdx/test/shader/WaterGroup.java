package com.libGdx.test.shader;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;

public class WaterGroup extends BaseGroup {
    private float touch[] = new float[2];
    private float time;
    Image image;
    public WaterGroup() {
        super("shader/wave/wave.vert","shader/wave/wave.glsl");
        image = new Image(Asset.getAsset().getTexture("hmbb.jpg"));
        addActor(image);
        image.setPosition(0,0, Align.center);
        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touch[0] = (x / image.getWidth()) + 0.5f;
                touch[1] = 1.0f - (y / image.getHeight()) - 0.5f;
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void setPar() {
        super.setPar();
        program.setUniformf("time",time);
        program.setUniformf("wave_offset",0.2f);
        program.setUniform2fv("center",touch,0,2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta * 10;
    }
}