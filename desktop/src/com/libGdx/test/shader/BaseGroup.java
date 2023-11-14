package com.libGdx.test.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.constant.Constant;

public class BaseGroup extends Group {
    protected ShaderProgram program ;
    public BaseGroup(String vert,String frag){
        program = new ShaderProgram(
                Gdx.files.internal(vert),
                Gdx.files.internal(frag));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.flush();
        batch.setShader(program);
        setPar();
        super.draw(batch, parentAlpha);
        batch.flush();
        batch.setShader(null);
    }


    public void setPar(){}
}
