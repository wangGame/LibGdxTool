package com.libGdx.test.pet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;

import org.lwjgl.openal.AL;

public class PetGroup extends Group {
    private ShaderProgram program;
    Image image;
    public PetGroup(){
//        program = new ShaderProgram(Gdx.files.internal("shader/out/PlainVertex.glsl"),Gdx.files.internal("shader/out/PlainFragment.glsl"));
        program = new ShaderProgram(Gdx.files.internal("shader/out/PlainVertex.glsl"),Gdx.files.internal("shader/out/ComplexFragment.glsl"));
        image = new Image(Asset.getAsset().getTexture("dog_xuanguan.png"));
//        addActor(image);
        setPosition(Constant.GAMEWIDTH/2f,Constant.HIGHT/2f, Align.center);
        image.setOrigin(Align.center);


        SpineActor smallPeople = new SpineActor("spine/idle2","spine/idle.atlas");
        addActor(smallPeople);
//        actor.setAnimation("animation",true);
        smallPeople.setAnimation("5", false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (false){
            super.draw(batch, parentAlpha);
        }else {
            batch.setShader(program);
//        program.setUniformf("u_alpha", 1.0f);
            program.setUniformf("u_outlineColor", 1, 0, 0, 1);
            program.setUniformf("u_outlineWidth", 1);
            program.setUniformf("u_outlineAlpha", 0.8f);
            program.setUniformf("u_shadowColor", 1, 1, 1, 1);
            program.setUniformi("u_textureSize", (int) image.getWidth(), (int) image.getHeight());
            program.setUniformf("u_alpha", 1);
            super.draw(batch, parentAlpha);
            batch.setShader(null);
        }
    }
}
