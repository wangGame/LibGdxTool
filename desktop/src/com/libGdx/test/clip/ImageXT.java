package com.libGdx.test.clip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.CpuPolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kw.gdx.asset.Asset;

/**
 * @Auther jian xian si qi
 * @Date 2023/10/26 15:51
 */
public class ImageXT extends Actor {
    private Texture texture;
    private float [] spriteVertices;
    private short [] polygonTriangles;
    public ImageXT(){
        texture = Asset.getAsset().getTexture("assets/shuoming.png");
        spriteVertices = new float[25];
        polygonTriangles = new short[25];
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
//        spriteVertices[0] = 30;
//        spriteVertices[1] = 30;
//        spriteVertices[2] = Color.RED.toFloatBits();
//        spriteVertices[3] = 0;
//        spriteVertices[4] = 1;
//
//
//        spriteVertices[5] = 1000;
//        spriteVertices[6] = 30;
//        spriteVertices[7] = Color.RED.toFloatBits();
//        spriteVertices[8] = 1;
//        spriteVertices[9] = 1;
//
//
//        spriteVertices[10] = 1000;
//        spriteVertices[11] = 1000;
//        spriteVertices[12] = Color.RED.toFloatBits();
//        spriteVertices[13] = 1;
//        spriteVertices[14] = 0;
//
//        spriteVertices[15] = 30;
//        spriteVertices[16] = 800;
//        spriteVertices[17] = Color.RED.toFloatBits();
//        spriteVertices[18] = 0;
//        spriteVertices[19] = 0.2f;
        spriteVertices[0] = 0;
        spriteVertices[1] = 500;
        spriteVertices[2] = Color.RED.toFloatBits();
        spriteVertices[3] = 0;
        spriteVertices[4] = 0.5f;


        spriteVertices[5] = 500;
        spriteVertices[6] = 30;
        spriteVertices[7] = Color.RED.toFloatBits();
        spriteVertices[8] = 0.5f;
        spriteVertices[9] = 1;


        spriteVertices[10] = 1000;
        spriteVertices[11] = 500;
        spriteVertices[12] = Color.RED.toFloatBits();
        spriteVertices[13] = 1;
        spriteVertices[14] = 0.5f;

        spriteVertices[15] = 500;
        spriteVertices[16] = 1000;
        spriteVertices[17] = Color.RED.toFloatBits();
        spriteVertices[18] = 0.5f;
        spriteVertices[19] = 0.f;

        spriteVertices[20] = 1000;
        spriteVertices[21] = 750;
        spriteVertices[22] = Color.RED.toFloatBits();
        spriteVertices[23] = 1.0f;
        spriteVertices[24] = 0.25f;

        polygonTriangles[0] = 0;
        polygonTriangles[1] = 1;
        polygonTriangles[2] = 2;
        polygonTriangles[3] = 2;
        polygonTriangles[4] = 3;
        polygonTriangles[5] = 0;
        polygonTriangles[6] = 0;
        polygonTriangles[7] = 3;
        polygonTriangles[8] = 4;



        ((CpuPolygonSpriteBatch)batch).draw(texture,
                spriteVertices,0,spriteVertices.length,
                polygonTriangles,0,9);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
