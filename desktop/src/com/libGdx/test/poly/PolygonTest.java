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
    private boolean init = false;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
/*        stage.addActor(new PolyActor());*/

        /*PolygonRegion polygonRegion = new PolygonRegion();*/
        polygon = new Polygon();
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


        polygon = new Polygon(vertices);
        init = true;
    }

    @Override
    public void render() {
        super.render();
        if (!init)return;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);  // 设置绘制颜色为红色

        // 绘制多边形
        shapeRenderer.polygon(polygon.getVertices());

        shapeRenderer.end();
    }
}
