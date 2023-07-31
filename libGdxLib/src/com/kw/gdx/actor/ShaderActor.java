package com.kw.gdx.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.AnnotationInfo;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.resource.annotation.ShaderResource;
import com.kw.gdx.resource.cocosload.CocosResource;

import java.lang.annotation.Annotation;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/26 19:09
 */
public class ShaderActor extends Actor {
    private ShaderProgram program;
    public ShaderActor(){
        ShaderResource annotation = AnnotationInfo.checkClassAnnotation(this,ShaderResource.class);
        if (annotation!=null){
            String value = annotation.value();
            program = new ShaderProgram(Gdx.files.internal(value),Gdx.files.internal(value));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (program!=null){
            batch.setShader(program);
            super.draw(batch, parentAlpha);
            batch.setShader(null);
        }else {
            super.draw(batch, parentAlpha);
        }
    }
}
