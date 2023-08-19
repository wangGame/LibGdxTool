package com.libGdx.test.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.constant.Constant;

public class ShaderGroup extends Group {
    private ShaderProgram program ;

    private float bigR = 0;
    private float smallR = bigR-10;


    public ShaderGroup(){
        program = new ShaderProgram(
                Gdx.files.internal("shader/cir/grayScale.vert"),
                Gdx.files.internal("shader/cir/grayScale.glsl"));

        setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();
        batch.setShader(program);
        super.draw(batch, parentAlpha);
        batch.flush();
        batch.setShader(null);
    }
}
