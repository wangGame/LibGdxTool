package com.libGdx.test.vertices;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kw.gdx.asset.Asset;

public class VertextGroup extends Actor {
    private Texture texture;
    private Color temp = Color.valueOf("#FFFFFF");
    private float[] vertices = new float[4 * 5];

    public VertextGroup(){
        texture = Asset.getAsset().getTexture("assets/0_1_41_512.jpg");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int idx = 0;
        float color = temp.toFloatBits();
        final float[] vertices = this.vertices;
        vertices[idx] = 0;
        vertices[idx + 1] = 1000;
        vertices[idx + 2] = color;
        vertices[idx + 3] = 0;
        vertices[idx + 4] = 0;

        vertices[idx + 5] = 0;
        vertices[idx + 6] = 0;
        vertices[idx + 7] = color;
        vertices[idx + 8] = 0;
        vertices[idx + 9] = 1;

        vertices[idx + 10] = 1000;
        vertices[idx + 11] = 0;
        vertices[idx + 12] = color;
        vertices[idx + 13] = 1;
        vertices[idx + 14] = 1;

        vertices[idx + 15] = 1000;
        vertices[idx + 16] = 1000;
        vertices[idx + 17] = color;
        vertices[idx + 18] = 1;
        vertices[idx + 19] = 0;

        batch.draw(texture,vertices,0,20);
    }
}
