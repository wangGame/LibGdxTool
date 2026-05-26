package com.libGdx.test.beser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BezierTextureDebugActor extends Actor {

    private final BUL1 action;
    private final TextureRegion textureRegion;

    private final PolygonSpriteBatch polygonBatch;

    private final Vector2 prev = new Vector2();
    private final Vector2 curr = new Vector2();
    private final Vector2 next = new Vector2();
    private final Vector2 tangent = new Vector2();
    private final Vector2 normal = new Vector2();

    private float width = 20F;
    private int samples = 120;

    /**
     * true  ：曲线绘制进度和物体运动位置一致，受 Interpolation 影响
     * false ：曲线按真实时间线性增长
     */
    private boolean useMoveT = true;

    public BezierTextureDebugActor(Texture texture, BUL1 action) {
        this(new TextureRegion(texture), action);
    }

    public BezierTextureDebugActor(TextureRegion textureRegion, BUL1 action) {
        this.textureRegion = textureRegion;
        this.action = action;
        this.polygonBatch = new PolygonSpriteBatch();
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setSamples(int samples) {
        this.samples = samples;
    }

    public void setUseMoveT(boolean useMoveT) {
        this.useMoveT = useMoveT;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float endT = useMoveT ? action.getMoveT() : action.getRawT();

        if (endT <= 0.001F) {
            return;
        }

        int pointCount = Math.max(2, (int) (samples * endT) + 1);

        float[] vertices = new float[pointCount * 2 * 2];
        short[] triangles = new short[(pointCount - 1) * 6];

        buildStrip(endT, pointCount, vertices, triangles);

        PolygonRegion region = new PolygonRegion(
                textureRegion,
                vertices,
                triangles
        );

        batch.end();

        polygonBatch.setProjectionMatrix(batch.getProjectionMatrix());
        polygonBatch.setTransformMatrix(batch.getTransformMatrix());

        polygonBatch.begin();
        polygonBatch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, parentAlpha);
        polygonBatch.draw(region, 0, 0);
        polygonBatch.end();

        batch.begin();
    }

    private void buildStrip(float endT, int pointCount, float[] vertices, short[] triangles) {
        float halfWidth = width * 0.5F;

        for (int i = 0; i < pointCount; i++) {
            float t = endT * i / (pointCount - 1);

            action.valueAt(t, curr);

            if (i == 0) {
                float nextT = endT * (i + 1) / (pointCount - 1);
                action.valueAt(nextT, next);
                tangent.set(next).sub(curr);
            } else if (i == pointCount - 1) {
                float prevT = endT * (i - 1) / (pointCount - 1);
                action.valueAt(prevT, prev);
                tangent.set(curr).sub(prev);
            } else {
                float prevT = endT * (i - 1) / (pointCount - 1);
                float nextT = endT * (i + 1) / (pointCount - 1);

                action.valueAt(prevT, prev);
                action.valueAt(nextT, next);

                tangent.set(next).sub(prev);
            }

            if (tangent.len2() == 0) {
                normal.set(0, 1);
            } else {
                tangent.nor();
                normal.set(-tangent.y, tangent.x);
            }

            float leftX = curr.x + normal.x * halfWidth;
            float leftY = curr.y + normal.y * halfWidth;

            float rightX = curr.x - normal.x * halfWidth;
            float rightY = curr.y - normal.y * halfWidth;

            int vi = i * 4;

            vertices[vi] = leftX;
            vertices[vi + 1] = leftY;

            vertices[vi + 2] = rightX;
            vertices[vi + 3] = rightY;
        }

        for (int i = 0; i < pointCount - 1; i++) {
            short left0 = (short) (i * 2);
            short right0 = (short) (i * 2 + 1);
            short left1 = (short) ((i + 1) * 2);
            short right1 = (short) ((i + 1) * 2 + 1);

            int ti = i * 6;

            triangles[ti] = left0;
            triangles[ti + 1] = right0;
            triangles[ti + 2] = left1;

            triangles[ti + 3] = left1;
            triangles[ti + 4] = right0;
            triangles[ti + 5] = right1;
        }
    }

    public void dispose() {
        polygonBatch.dispose();
    }
}