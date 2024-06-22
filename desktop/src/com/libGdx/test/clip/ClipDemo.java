package com.libGdx.test.clip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.clip.ClipGroup;
import com.kw.gdx.clip.ClippingAttachment;
import com.kw.gdx.clip.TextureRegionActor;

/**
 *
 * 画圆   clip的简单使用
 * @Auther jian xian si qi
 * @Date 2023/12/22 10:15
 */
public class ClipDemo extends ClipGroup {
//    public ClipDemo(){
//        clippingAttachment = new ClippingAttachment();
//        float vertices[] ={
//                0f,         304.22f,
//                150f,         600,
//                302.38f,    302.04f,
//                300.8f,     0,
//                0,          0f
//        };
//        clippingAttachment.setVerties(vertices);
//        Texture texture = Asset.getAsset().getTexture("test001.png");
//        TextureRegionActor textureRegionActor = new TextureRegionActor(getX() / 2.0f, getY() / 2.0f,texture);
//        textureRegionActor.setColor(Color.RED);
//        addActor(textureRegionActor);
//    }

    public ClipDemo(){
        Texture texture = Asset.getAsset().getTexture("000.png");
        TextureRegionActor textureRegionActor = new TextureRegionActor(texture);
        addActor(textureRegionActor);
        setSize(texture.getWidth(),texture.getHeight());
        clippingAttachment = new ClippingAttachment();


//        float vertices[] ={
//                100,400,
//                200,300,
//                300,400,
//                400,200,
//                200,0,
//                0,200
//
//                //                0f,         304.22f,
////                150f,         600,
////                302.38f,    302.04f,
////                602.38f,    102.04f,
////                300.8f,     0,
////                30,          40f
//        };
    }


    private float target;

    @Override
    public void act(float delta) {
        super.act(delta);
        target += delta * 10;
        updateProcess((int) target);
    }

    float convertToRadians(float angleInDegrees) {
        float angleInRadians = angleInDegrees * 0.0174532925f;
        return angleInRadians;
    }

    private void updateProcess(int num) {
        float centerX = 300;
        float centerY = 300;
//        int num = 360;
        int length = (num+1) * 2;
        float vertices[] = new float[length+2];
        int indx = 0 ;
        vertices[indx++] = centerX;
        vertices[indx++] = centerY;

        for (int angle = 0; angle <= num; angle++) {
            float dy = (float) (Math.sin(angle * 0.0174532925f) * 300);
            float dx = (float) (Math.cos(angle * 0.0174532925f) * 300);
            vertices [indx++] = centerX + dx;
            vertices [indx++] = centerY + dy;
        }
        clippingAttachment.setVerties(vertices);
    }
}
