package com.libGdx.test.poly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ShortArray;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.GameInfo;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/24 13:56
 */
@GameInfo(width = 720,height = 1280,batch = Constant.COUPOLYGONBATCH)
public class PolygonTest extends LibGdxTestMain {
    public static void main(String[] args) {
        PolygonTest polygonTest = new PolygonTest();
        polygonTest.start(polygonTest);
    }

    private ShapeRenderer shapeRenderer;
    private Polygon polygon;
    private Polygon polygon1;
    private Polygon polygon2;
    private Polygon polygon3;
    private Polygon polygon4;

    private boolean init = false;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
/*        stage.addActor(new PolyActor());*/

        /*PolygonRegion polygonRegion = new PolygonRegion();*/


        shapeRenderer = new ShapeRenderer();


        FileHandle internal = Gdx.files.internal("assets/124/out.file");
        String content = internal.readString();
        String[] split = content.split("\n");
        float[] vertices = new float[split.length*2];
        int x = 0;
        for (String s : split) {
            s = s.replace("(","");
            s = s.replace(")","");
            String[] split1 = s.split(",");
            vertices[x++] = Float.parseFloat(split1[0])* 10 + 300;
            vertices[x++] = Float.parseFloat(split1[1])*10 + 300;
        }


        double xx[] = new double[]{
                2.13F, 42.1F,       3.83F, 40.99F,      16.7F, 20.39F,      37.28F, 16.03F,
                39.58F, 14.75F,     40.95F, 12.96F,     41.03F, 10.57F,     40.01F, 8.69F,
                25.72F, -7.39F,     27.77F, -30.44F,    26.57F, -32.74F,    24.52F, -34.28F,
                22.22F, -34.79F,    20.09F, -34.11F,    1.33F, -25.66F,     -17.54F, -34.02F,
                -20.4F, -34.79F,    -23.09F, -34.36F,   -24.63F, -33.08F,   -25.56F, -31.29F,
                -23.77F, -7.06F,    -31.69F, 1.74F,     -38.07F, 8.89F,     -38.95F, 10.98F,
                -38.62F, 12.96F,    -37.41F, 14.61F,    -35.65F, 15.38F,    -13.75F, 21.27F,
                -2.4F, 40.22F,      -1.63F, 41.42F,     0.08F, 43.1F
        };
        float wakongV[] = new float[xx.length];
        float wakongV1[] = new float[xx.length];
        float wakongV2[] = new float[xx.length];
        float wakongV3[] = new float[xx.length];
        float wakongV4[] = new float[xx.length];
        for (int i = 0; i < xx.length; i++) {
            wakongV[i] = (float) xx[i]+400;
        }

        polygon = new Polygon(vertices);
        polygon.setVertices(wakongV);
        for (int i = 0; i < xx.length; i++) {
            wakongV1[i] = (float) xx[i] ;
        }
        polygon1 = new Polygon(wakongV1);
        for (int i = 0; i < xx.length; i++) {
            wakongV2[i] = (float) xx[i] ;
        }
        polygon2 = new Polygon(wakongV2);
        for (int i = 0; i < xx.length; i++) {
            wakongV3[i] = (float) xx[i] ;
        }
        polygon3 = new Polygon(wakongV3);
        for (int i = 0; i < xx.length; i++) {
            wakongV4[i] = (float) xx[i] ;
        }
        polygon4 = new Polygon(wakongV4);
        polygon1.setScale(0.4f,0.4f);
        polygon2.setScale(0.3f,0.3f);
        polygon3.setScale(0.4f,0.4f);
        polygon4.setScale(0.5f,0.5f);
        polygon.setScale(1,1);

        init = true;
    }

    @Override
    public void render() {
        super.render();
        if (!init)return;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);  // 设置绘制颜色为红色

        // 绘制多边形
        shapeRenderer.filledPolygon(polygon.getTransformedVertices());
        shapeRenderer.filledPolygon(polygon1.getTransformedVertices());
        shapeRenderer.filledPolygon(polygon2.getTransformedVertices());
        shapeRenderer.filledPolygon(polygon3.getTransformedVertices());
        shapeRenderer.filledPolygon(polygon4.getTransformedVertices());


        shapeRenderer.end();
    }
}
