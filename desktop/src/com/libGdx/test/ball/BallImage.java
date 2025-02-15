package com.libGdx.test.ball;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class BallImage extends Image {
    private float TABLE_HEIGHT = 1610.0f;
    private float TABLE_WIDTH = 2870.0f;
    private  float BALL_RADIUS = 3*47.15f;
    public final Matrix4 matrix4;
    Vector3 ballPos = new Vector3();
    Vector3 lightPos = new Vector3();
    private static final ShaderProgram shaderProgram
            = new ShaderProgram(
            Gdx.files.internal("ball.vert"),
            Gdx.files.internal("ball.frag"));
    private static final Cubemap cubemap = new Cubemap(
            Gdx.files.internal("cube/C_left.png"),
            Gdx.files.internal("cube/C_right.png"),
            Gdx.files.internal("cube/C_top2.png"),
            Gdx.files.internal("cube/C_bottom.png"),
            Gdx.files.internal("cube/C_front.png"),
            Gdx.files.internal("cube/C_back.png"), false);

    static {
        Gdx.gl.glBindTexture(GL20.GL_TEXTURE_CUBE_MAP, cubemap.getTextureObjectHandle());
        Gdx.gl.glGenerateMipmap(GL20.GL_TEXTURE_CUBE_MAP);
        cubemap.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
    }

    public BallImage (Texture texture) {
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(texture);
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);
        setDrawable(textureRegionDrawable);
        matrix4 = new Matrix4();
        setSize(BALL_RADIUS * 2, BALL_RADIUS * 2);
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.setShader(shaderProgram);
        Gdx.gl.glEnable(GL20.GL_TEXTURE_CUBE_MAP);
        shaderProgram.setUniformMatrix("u_ballMatrix", matrix4);
        cubemap.bind(1);
        shaderProgram.setUniformi("u_environmentCubemap", 1);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        ballPos.x = getParent().getX(Align.center);
        ballPos.y = TABLE_HEIGHT;
        shaderProgram.setUniformf("u_ballPos", ballPos);
        lightPos.x = TABLE_WIDTH / 2;
        lightPos.y = TABLE_HEIGHT / 2;
        lightPos.z = 1500;
        shaderProgram.setUniformf("u_lightPos", lightPos);
        lightPos.z = 2000;
        shaderProgram.setUniformf("u_viewPos", lightPos);
        super.draw(batch, parentAlpha);
        batch.setShader(null);
    }

}
