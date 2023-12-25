package com.kw.gdx.clip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.CpuPolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.NumberUtils;
import com.badlogic.gdx.utils.ShortArray;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/21 18:57
 */
public class TextureRegionActor extends Actor {
    private Texture texture;
    private RegionAttachment region1;
    FloatArray vertices = new FloatArray(32);
    FloatArray vertices11 = new FloatArray();
    final short[] quadTriangles = {0, 1, 2, 2, 3, 0};
    private int verticesLength;
    private float[] vertices12;
    private  float []uvs;
    private Color color = new Color(1,1,1,1f);

    public TextureRegionActor(Texture texture){
        //创建Region
        this.texture = texture;
        region1 = new RegionAttachment();
        TextureRegion region = new TextureRegion(texture);
        region1.setRegion(region);
        setSize(region.getRegionWidth(),region.getRegionHeight());
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        updateVus();
    }

    private void updateVus() {
        region1.setWidth(getWidth());
        region1.setHeight(getHeight());
        region1.setX(getX()+ region1.getWidth()/4.0F);
        region1.setY(getY() + region1.getHeight()/4.0F);
        region1.updateOffset(); //裁剪 所以为2
        region1.computeWorldVertices(vertices.items, 0, 2);
        verticesLength = 2 << 2;
        vertices12 = vertices11.items;
        region1.computeWorldVertices(vertices12, 0, 2);
        uvs = region1.getUVs();
    }

    public void setSize(float width, float height){
        super.setSize(width,height);
        region1.setWidth(width);
        region1.setHeight(height);
        updateVus();
    }

    public short[] getTriangles() {
        return quadTriangles;
    }

    public int getVerticesLength() {
        return verticesLength;
    }

    public float[] getVertices12() {
        return vertices12;
    }

    public float[] getUvs() {
        return uvs;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(Batch batch, SkeletonClipping clipper, float parentAlpha) {
        float alpha = parentAlpha;
        float c = NumberUtils.intToFloatColor(((int)(color.a * alpha * 255.0f) << 24) //
                | ((int)(color.b * 255) << 16) //
                | ((int)(color.g * 255) << 8) //
                | (int) (color.r * 255));

        clipper.clipTriangles(getVertices12(),
                getTriangles(), getTriangles().length,
                getUvs(), c, 0, false);
        FloatArray clippedVertices = clipper.getClippedVertices();
        ShortArray clippedTriangles = clipper.getClippedTriangles();
        CpuPolygonSpriteBatch batch1 = (CpuPolygonSpriteBatch) (batch);
        batch1.draw(getTexture(),
                clippedVertices.items,
                0,
                clippedVertices.size,
                clippedTriangles.items, 0,
                clippedTriangles.size);
    }
}
