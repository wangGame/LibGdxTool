package com.kw.gdx.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.resource.annotation.AnnotationInfo;
import com.kw.gdx.resource.annotation.ShaderResource;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/26 19:09
 *
 * 注解ShaderGroup
 */
@ShaderResource()
public class ShaderGroup extends Group {
    protected ShaderProgram program;
    public ShaderGroup(){
        ShaderResource annotation = AnnotationInfo.checkClassAnnotation(this,ShaderResource.class);
        if (annotation!=null){
            String vertexValue = annotation.vertexValue();
            String fragmentValue = annotation.fragmentValue();
            program = new ShaderProgram(Gdx.files.internal(vertexValue),Gdx.files.internal(fragmentValue));
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
