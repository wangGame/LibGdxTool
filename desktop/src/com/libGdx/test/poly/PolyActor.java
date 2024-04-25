package com.libGdx.test.poly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ShortArray;
import com.kw.gdx.asset.Asset;

/**
 *
 *裁剪   点变为图形
 * @Auther jian xian si qi
 * @Date 2023/7/24 14:16
 */
public class PolyActor extends Actor {
    private PolygonSprite poly;

//    Image image1 = new Image(Asset.getAsset().getTexture("assets/hand1.png"));
    public PolyActor(){
        setSize(500,500);
        setDebug(true);
        Texture texture = new Texture("assets/hand1.png");
        TextureRegion region = new TextureRegion(texture);
//        image = new Image(texture);
        float rwidth = region.getRegionWidth();
        float rheight = region.getRegionHeight();
        float fv[] = {
                rwidth/2,rheight/2,
                rwidth/2,rheight,
                rwidth,rheight,
                rwidth,0,
                0,0,
                0,rheight,
                rwidth/3,rheight
        };
        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray shortArray = triangulator.computeTriangles(fv);
        PolygonRegion polyReg = new PolygonRegion(region, fv, shortArray.toArray());
        poly = new PolygonSprite(polyReg);
        poly.setScale(1,1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
//        image1.draw(batch,parentAlpha);
        poly.draw((PolygonSpriteBatch) batch);
    }
}
