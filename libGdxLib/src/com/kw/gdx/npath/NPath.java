//package com.kw.gdx.npath;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.scenes.scene2d.Group;
//import com.kw.gdx.asset.Asset;
//
///**
// * 可以直接切，然后拼起来
// *
// * 思路完成，不写了
// */
//public class NPath extends Group {
//    private TextureRegion region;
//    private TextureRegion[] textureRegions;
//    private float[] vertices;
//    private Texture texture;
//
//    public NPath(int i){
//        textureRegions = new TextureRegion[i];
//        this.texture = Asset.getAsset().getTexture("assets/0_1_41_512.jpg");
//        this.region = new TextureRegion(texture);
//        int i2 = region.getRegionWidth() / i;
//        int regionHeight = region.getRegionHeight();
//        for (int i1 = 0; i1 < i; i1++) {
//            textureRegions[i1] = new TextureRegion(region,i1*i2,0,i2,regionHeight);
//        }
//        int idx = 0;
//        vertices = new float[4 * 5 * i];
//        float xx = 100;
//        float yy = 100;
//
//        for (int i1 = 0; i1 < 3; i1++) {
//            TextureRegion textureRegion = textureRegions[i1];
//            if (i1 == 0) {
//                xx = 0;
//                yy = 0;
//            }else if (i1 == 1){
//                TextureRegion textureRegionx = textureRegions[i1-1];
//                xx = textureRegionx.getRegionWidth();
//                yy = 0;
//            }else {
//                TextureRegion textureRegionx = textureRegions[i1-1];
//                xx = textureRegionx.getRegionWidth() * 2;
//                yy = 0;
//            }
//
//            vertices[idx] = 0 + xx;
//            vertices[idx + 1] = textureRegion.getRegionHeight() + yy;
//            vertices[idx + 2] = color.toFloatBits();
//            vertices[idx + 3] = textureRegion.getU();
//            vertices[idx + 4] = textureRegion.getV();
//
//            vertices[idx + 5] = 0 + xx;
//            vertices[idx + 6] = 0 + yy;
//            vertices[idx + 7] = color.toFloatBits();
//            vertices[idx + 8] = textureRegion.getU();
//            vertices[idx + 9] = textureRegion.getV2();
//
//            vertices[idx + 10] = textureRegion.getRegionWidth() + xx;
//            vertices[idx + 11] = 0 + yy;
//            vertices[idx + 12] = color.toFloatBits();
//            vertices[idx + 13] = textureRegion.getU2();
//            vertices[idx + 14] = textureRegion.getV2();
//
//            vertices[idx + 15] = textureRegion.getRegionWidth() + xx;
//            vertices[idx + 16] = textureRegion.getRegionHeight() + yy;
//            vertices[idx + 17] = color.toFloatBits();
//            vertices[idx + 18] = textureRegion.getU2();
//            vertices[idx + 19] = textureRegion.getV();
//            idx+=20;
//        }
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        batch.draw(texture,vertices,0,vertices.length);
//    }
//}
