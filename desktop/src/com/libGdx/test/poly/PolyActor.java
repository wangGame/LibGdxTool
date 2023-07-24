package com.libGdx.test.poly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ShortArray;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/24 14:16
 */
public class PolyActor extends Actor {
    private PolygonSprite poly;
    public PolyActor(){
        Texture texture = new Texture("fangshiyi.png");
        TextureRegion region = new TextureRegion(texture);
        float rwidth = region.getRegionWidth();
        float rheight = region.getRegionHeight();
        float fv[] = {
                rwidth/2,rheight/2,
                rwidth/2,rheight,
                rwidth,rheight,

        };
        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray shortArray = triangulator.computeTriangles(fv);
        PolygonRegion polyReg = new PolygonRegion(region, fv, shortArray.toArray());
        poly = new PolygonSprite(polyReg);
        poly.setScale(1,1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        poly.draw((PolygonSpriteBatch) batch);
    }
}
