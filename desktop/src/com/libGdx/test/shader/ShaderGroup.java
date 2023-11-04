package com.libGdx.test.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.constant.Constant;

public class ShaderGroup extends BaseGroup {
    public ShaderGroup(){
        super("shader/cir/grayScale.vert","shader/cir/commoncir.glsl");
    }
}
