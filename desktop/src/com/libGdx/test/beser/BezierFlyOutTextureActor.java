package com.libGdx.test.beser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

/**
 * 启示：
 *  坐标可以随意写， 写完之后进行归一化处理
 */
public class BezierFlyOutTextureActor extends Actor {

    private final BUL1 curve;
    private final TextureRegion textureRegion;
    private final PolygonSpriteBatch polygonBatch;

    private Actor followActor;

    private float elapsed = 0F;

    // 第一段：曲线画出来的时间
    private float drawDuration = 2F;

    // 第二段：尾部收缩 + 头部飞出去的时间
    private float flyOutDuration = 0.8F;

    // 飞出去的直线长度
    private float flyDistance = 500F;

    // 曲线带宽
    private float width = 30F;

    private int curveSamples = 160;
    private int straightSamples = 40;

    private final Array<Vector2> pathPoints = new Array<>();

    private final Vector2 tmp = new Vector2();
    private final Vector2 tmp2 = new Vector2();
    private final Vector2 endPoint = new Vector2();
    private final Vector2 prevEndPoint = new Vector2();
    private final Vector2 flyDir = new Vector2();
    private final Vector2 headPoint = new Vector2();

    private final Vector2 tangent = new Vector2();
    private final Vector2 normal = new Vector2();

    private final Interpolation flyInterpolation = Interpolation.sineOut;

    public BezierFlyOutTextureActor(Texture texture, BUL1 curve) {
        this(new TextureRegion(texture), curve);
    }

    public BezierFlyOutTextureActor(TextureRegion textureRegion, BUL1 curve) {
        this.textureRegion = textureRegion;
        this.curve = curve;
        this.polygonBatch = new PolygonSpriteBatch();
    }

    public void setFollowActor(Actor followActor) {
        this.followActor = followActor;
    }

    public void setDrawDuration(float drawDuration) {
        this.drawDuration = drawDuration;
    }

    public void setFlyOutDuration(float flyOutDuration) {
        this.flyOutDuration = flyOutDuration;
    }

    public void setFlyDistance(float flyDistance) {
        this.flyDistance = flyDistance;
    }
    // flyOut 结束后继续飞出去的速度，单位：像素 / 秒
    private float continueFlySpeed = 700F;

    public void setContinueFlySpeed(float continueFlySpeed) {
        this.continueFlySpeed = continueFlySpeed;
    }
    public void setWidth(float width) {
        this.width = width;
    }

    public void setCurveSamples(int curveSamples) {
        this.curveSamples = curveSamples;
    }

    public void setStraightSamples(int straightSamples) {
        this.straightSamples = straightSamples;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        elapsed += delta;

        updateFollowActor();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        buildPathPoints();

        if (pathPoints.size < 2) {
            return;
        }

        float[] vertices = new float[pathPoints.size * 4];
        short[] triangles = new short[(pathPoints.size - 1) * 6];

        buildStrip(vertices, triangles);

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

    private void buildPathPoints() {
        pathPoints.clear();

        float totalDuration = drawDuration + flyOutDuration;

        if (elapsed <= drawDuration) {
            buildDrawingCurve();
        } else if (elapsed <= totalDuration) {
            buildFlyOutCurve();
        } else {
            buildFinalFlyOutCurve();
        }
    }

    /**
     * 第一阶段：
     * 从 t = 0 慢慢画到当前 t
     */
    private void buildDrawingCurve() {
        float progress = drawDuration <= 0F ? 1F : elapsed / drawDuration;
        progress = clamp01(progress);

        int count = Math.max(2, (int) (curveSamples * progress) + 1);

        for (int i = 0; i < count; i++) {
            float t = progress * i / (count - 1);

            Vector2 p = new Vector2();
            curve.valueAt(t, p);
            pathPoints.add(p);
        }
    }

    /**
     * 第二阶段：
     * 尾部沿曲线收缩，头部沿曲线末端切线飞出去
     */
    private void buildFlyOutCurve() {
        float rawProgress = flyOutDuration <= 0F
                ? 1F
                : (elapsed - drawDuration) / flyOutDuration;

        rawProgress = clamp01(rawProgress);

        float flyProgress = flyInterpolation.apply(rawProgress);

        calculateFlyDirection();

        // 尾巴从曲线起点逐渐移动到曲线终点
        float tailT = rawProgress;

        // 头部沿末端切线方向飞出去
        curve.valueAt(1F, endPoint);
        headPoint.set(endPoint).mulAdd(flyDir, flyDistance * flyProgress);

        // 1. 剩余的曲线部分：tailT -> 1
        int curveCount = Math.max(2, (int) (curveSamples * (1F - tailT)) + 1);

        for (int i = 0; i < curveCount; i++) {
            float local = curveCount == 1 ? 1F : i / (float) (curveCount - 1);
            float t = tailT + (1F - tailT) * local;

            Vector2 p = new Vector2();
            curve.valueAt(t, p);
            pathPoints.add(p);
        }

        // 2. 直线飞出去部分：曲线终点 -> headPoint
        int lineCount = Math.max(2, (int) (straightSamples * flyProgress) + 1);

        for (int i = 1; i < lineCount; i++) {
            float local = i / (float) (lineCount - 1);

            Vector2 p = new Vector2();
            p.set(endPoint).lerp(headPoint, local);
            pathPoints.add(p);
        }
    }

    /**
     * 动画彻底结束后保留最后一帧。
     * 如果你想结束后消失，可以这里 pathPoints.clear();
     */
    private void buildFinalFlyOutCurve() {
        calculateFlyDirection();

        curve.valueAt(1F, endPoint);
        headPoint.set(endPoint).mulAdd(flyDir, flyDistance);

        int lineCount = Math.max(2, straightSamples);

        for (int i = 0; i < lineCount; i++) {
            float local = i / (float) (lineCount - 1);

            Vector2 p = new Vector2();
            p.set(endPoint).lerp(headPoint, local);
            pathPoints.add(p);
        }
    }

    /**
     * 根据 pathPoints 生成一条有宽度的带状 PolygonRegion 顶点
     */
    private void buildStrip(float[] vertices, short[] triangles) {
        float halfWidth = width * 0.5F;

        for (int i = 0; i < pathPoints.size; i++) {
            Vector2 curr = pathPoints.get(i);

            if (i == 0) {
                Vector2 next = pathPoints.get(i + 1);
                tangent.set(next).sub(curr);
            } else if (i == pathPoints.size - 1) {
                Vector2 prev = pathPoints.get(i - 1);
                tangent.set(curr).sub(prev);
            } else {
                Vector2 prev = pathPoints.get(i - 1);
                Vector2 next = pathPoints.get(i + 1);
                tangent.set(next).sub(prev);
            }

            if (tangent.len2() <= 0.00001F) {
                normal.set(0F, 1F);
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

        for (int i = 0; i < pathPoints.size - 1; i++) {
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

    /**
     * 计算贝塞尔末端方向。
     * 也就是头部飞出去的方向。
     */
    private void calculateFlyDirection() {
        curve.valueAt(1F, endPoint);

        float backT = 0.98F;
        curve.valueAt(backT, prevEndPoint);

        flyDir.set(endPoint).sub(prevEndPoint);

        if (flyDir.len2() <= 0.00001F) {
            // 如果最后两个点重合，继续往前找
            for (int i = 1; i <= 10; i++) {
                backT = 1F - i * 0.05F;
                if (backT < 0F) backT = 0F;

                curve.valueAt(backT, prevEndPoint);
                flyDir.set(endPoint).sub(prevEndPoint);

                if (flyDir.len2() > 0.00001F) {
                    break;
                }
            }
        }

        if (flyDir.len2() <= 0.00001F) {
            flyDir.set(1F, 0F);
        } else {
            flyDir.nor();
        }
    }

    private void updateFollowActor() {
        if (followActor == null) {
            return;
        }

        float totalDuration = drawDuration + flyOutDuration;

        if (elapsed <= drawDuration) {
            float progress = drawDuration <= 0F ? 1F : elapsed / drawDuration;
            progress = clamp01(progress);

            curve.valueAt(progress, tmp);
            followActor.setPosition(tmp.x, tmp.y, Align.center);
        } else {
            float rawProgress = flyOutDuration <= 0F
                    ? 1F
                    : (elapsed - drawDuration) / flyOutDuration;

            rawProgress = clamp01(rawProgress);
            float flyProgress = flyInterpolation.apply(rawProgress);

            calculateFlyDirection();

            curve.valueAt(1F, endPoint);
            tmp.set(endPoint).mulAdd(flyDir, flyDistance * flyProgress);

            followActor.setPosition(tmp.x, tmp.y, Align.center);
        }

        if (elapsed > totalDuration) {
            calculateFlyDirection();

            curve.valueAt(1F, endPoint);
            tmp.set(endPoint).mulAdd(flyDir, flyDistance);

            followActor.setPosition(tmp.x, tmp.y, Align.center);
        }
    }

    private float clamp01(float v) {
        if (v < 0F) return 0F;
        if (v > 1F) return 1F;
        return v;
    }

    public void dispose() {
        polygonBatch.dispose();
    }
}