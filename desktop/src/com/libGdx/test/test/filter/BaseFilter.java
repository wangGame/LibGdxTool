package com.libGdx.test.test.filter;


import com.badlogic.gdx.graphics.GL20;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class BaseFilter {
    protected float color[];
    protected float triangleCoords[];
    protected String fragmentShaderCode;
    protected String vertexShaderCode;

    protected FloatBuffer vertexBuffer;
    protected FloatBuffer colorBuffer;
    protected int mProgram ;

    //几个点    点乘以字符占用
    protected int COORDS_PER_VERTEX = 3;
    protected int vertexStride = COORDS_PER_VERTEX * 4;
    public GL20 gl10;
    protected int texture;
    public BaseFilter(){}


    public void create(){

    }




    public abstract void dispose();

    public void resume() {

    }

    public void setGL(GL20 gl10){
        this.gl10 = gl10;

    }


    public int getTexture(){
        return texture;
    }


    public void setTexture(int texture) {
        this.texture = texture;
    }
}
