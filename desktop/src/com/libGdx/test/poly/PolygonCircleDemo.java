package com.libGdx.test.poly;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class PolygonCircleDemo extends LibGdxTestMain {
    private PolygonSpriteBatch batch;
    private PolygonRegion circleRegion;
    private Texture texture;

    public static void main(String[] args) {
        PolygonCircleDemo polygonCircleDemo = new PolygonCircleDemo();
        polygonCircleDemo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        batch = new PolygonSpriteBatch();
        // 白色纹理
        texture = new Texture("assets/7.png");
        circleRegion = createCircleRegion(
                new TextureRegion(texture),
                200,   // 半径
                160     // 边数（越大越圆）
        );
    }

    @Override
    public void render() {
        super.render();

        if (batch!=null) {
            batch.begin();
            batch.draw(circleRegion, 0, 100);
            batch.end();
        }
    }

    /**
     * 创建圆形 PolygonRegion
     */
    private PolygonRegion createCircleRegion(TextureRegion region,
                                             float radius,
                                             int segments) {

        // =========================
        // 顶点
        // =========================

        // 中心点 + 圆周点
        float[] vertices = new float[(segments + 1) * 2];

        // 中心点
        vertices[0] = radius;
        vertices[1] = radius;

        // 圆周
        for (int i = 0; i < segments; i++) {

            float angle = (float)(Math.PI * 2 * i / segments);

            float x = radius + (float)Math.cos(angle) * radius;
            float y = radius + (float)Math.sin(angle) * radius;

            vertices[(i + 1) * 2] = x;
            vertices[(i + 1) * 2 + 1] = y;
        }

        // =========================
        // UV
        // =========================

        float[] texCoords = new float[(segments + 1) * 2];

        for (int i = 0; i < vertices.length / 2; i++) {

            float x = vertices[i * 2];
            float y = vertices[i * 2 + 1];

            texCoords[i * 2] = x / (radius * 2f);
            texCoords[i * 2 + 1] = y / (radius * 2f);
        }

        // =========================
        // 三角形索引
        // =========================

        short[] triangles = new short[segments * 3];

        for (int i = 0; i < segments; i++) {

            triangles[i * 3] = 0;

            triangles[i * 3 + 1] = (short)(i + 1);

            triangles[i * 3 + 2] =
                    (short)((i + 2 > segments) ? 1 : (i + 2));
        }

        return new PolygonRegion(
                region,
                vertices,
                triangles
        ) {
            @Override
            public float[] getTextureCoords() {
                return texCoords;
            }
        };
    }


}
