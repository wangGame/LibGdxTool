package com.libGdx.test.arrow;
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

public class BezierFlyOutTextureActor extends Actor {
    // 是否根据曲线长度自动计算 flyDistance
    private boolean autoFlyDistance = true;

    // 飞出距离 = 曲线长度 * autoFlyDistanceScale
    private float autoFlyDistanceScale = 0.35F;

    // 曲线长度采样精度
    private int lengthSamples = 300;
    private final BUL1 curve;
    private final TextureRegion textureRegion;
    private final PolygonSpriteBatch polygonBatch;

    private Actor followActor;

    private float elapsed = 0F;
    public void setAutoFlyDistance(boolean autoFlyDistance) {
        this.autoFlyDistance = autoFlyDistance;
    }

    public void setAutoFlyDistanceScale(float autoFlyDistanceScale) {
        this.autoFlyDistanceScale = autoFlyDistanceScale;
    }

    public void setLengthSamples(int lengthSamples) {
        this.lengthSamples = lengthSamples;
    }
    /**
     * 第一阶段：曲线从 0 画到 1 的时间
     */
    private float drawDuration = 2F;

    /**
     * 第二阶段：尾部收缩，头部飞出去的时间
     */
    private float flyOutDuration = 0.8F;

    /**
     * flyOut 阶段头部飞出去的长度
     */
    private float flyDistance = 600F;

    /**
     * flyOut 结束后继续飞行速度，单位：像素 / 秒
     */
    private float continueFlySpeed = 900F;
    private float calculateCurveLength() {
        Vector2 prev = new Vector2();
        Vector2 curr = new Vector2();

        curve.valueAt(0F, prev);

        float length = 0F;

        int count = Math.max(2, lengthSamples);

        for (int i = 1; i <= count; i++) {
            float t = i / (float) count;

            curve.valueAt(t, curr);
            length += curr.dst(prev);

            prev.set(curr);
        }

        return length;
    }
    /**
     * 纹理曲线宽度
     */
    private float width = 30F;
    private float getRealFlyDistance() {
        if (!autoFlyDistance) {
            return flyDistance;
        }

        float curveLength = calculateCurveLength();
        return curveLength * autoFlyDistanceScale;
    }
    private int curveSamples = 160;
    private int straightSamples = 50;

    private final Array<Vector2> pathPoints = new Array<>();

    private final Vector2 endPoint = new Vector2();
    private final Vector2 prevEndPoint = new Vector2();
    private final Vector2 flyDir = new Vector2();
    private final Vector2 headPoint = new Vector2();

    private final Vector2 tangent = new Vector2();
    private final Vector2 normal = new Vector2();

    private final Vector2 tmp = new Vector2();

    private final Interpolation drawInterpolation = Interpolation.sineOut;
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
        polygonBatch.setColor(
                Color.WHITE.r,
                Color.WHITE.g,
                Color.WHITE.b,
                parentAlpha
        );
        polygonBatch.draw(region, 0F, 0F);
        polygonBatch.end();

        batch.begin();
    }

    /**
     * 根据当前时间构建整条显示路径。
     */
    private void buildPathPoints() {
        pathPoints.clear();

        float totalDuration = drawDuration + flyOutDuration;

        if (elapsed <= drawDuration) {
            buildDrawingCurve();
        } else if (elapsed <= totalDuration) {
            buildFlyOutCurve();
        } else {
            buildContinueFlyCurve();
        }
    }

    /**
     * 第一阶段：
     * 曲线从起点慢慢画出来。
     */
    private void buildDrawingCurve() {
        float rawProgress = drawDuration <= 0F ? 1F : elapsed / drawDuration;
        rawProgress = clamp01(rawProgress);

        float progress = drawInterpolation.apply(rawProgress);

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
     * 尾部从曲线起点往终点收缩；
     * 头部从曲线终点沿切线方向飞出去；
     * 于是弯曲部分越来越短，直线部分越来越长。
     */
    private void buildFlyOutCurve() {
        float rawProgress = flyOutDuration <= 0F
                ? 1F
                : (elapsed - drawDuration) / flyOutDuration;

        rawProgress = clamp01(rawProgress);

        float flyProgress = flyInterpolation.apply(rawProgress);

        calculateFlyDirection();

        curve.valueAt(1F, endPoint);

        float tailT = rawProgress;

//        headPoint.set(endPoint).mulAdd(flyDir, flyDistance * flyProgress);
        float realFlyDistance = getRealFlyDistance();
        headPoint.set(endPoint).mulAdd(flyDir, realFlyDistance * flyProgress);

        // 剩余曲线部分：tailT -> 1
        float remainCurve = 1F - tailT;
        int curveCount = Math.max(2, (int) (curveSamples * remainCurve) + 1);

        for (int i = 0; i < curveCount; i++) {
            float local = i / (float) (curveCount - 1);
            float t = tailT + remainCurve * local;

            Vector2 p = new Vector2();
            curve.valueAt(t, p);
            pathPoints.add(p);
        }

        // 飞出去的直线部分：endPoint -> headPoint
        int lineCount = Math.max(2, (int) (straightSamples * flyProgress) + 1);

        for (int i = 1; i < lineCount; i++) {
            float local = i / (float) (lineCount - 1);

            Vector2 p = new Vector2();
            p.set(endPoint).lerp(headPoint, local);
            pathPoints.add(p);
        }
    }

    /**
     * 第三阶段：
     * flyOut 结束后，整条直线继续往前飞，不停下来。
     */
    private void buildContinueFlyCurve() {
        calculateFlyDirection();

        curve.valueAt(1F, endPoint);

        float totalDuration = drawDuration + flyOutDuration;
        float extraTime = Math.max(0F, elapsed - totalDuration);
        float extraDistance = extraTime * continueFlySpeed;

        Vector2 lineStart = new Vector2();
        Vector2 lineEnd = new Vector2();

        // flyOut 结束时，线段是：
        // endPoint -> endPoint + flyDistance
        //
        // 之后整条线段整体继续往前平移：
        lineStart.set(endPoint).mulAdd(flyDir, extraDistance);
//        lineEnd.set(endPoint).mulAdd(flyDir, flyDistance + extraDistance);
        float realFlyDistance = getRealFlyDistance();
        lineEnd.set(endPoint).mulAdd(flyDir, realFlyDistance + extraDistance);
        int lineCount = Math.max(2, straightSamples);

        for (int i = 0; i < lineCount; i++) {
            float local = i / (float) (lineCount - 1);

            Vector2 p = new Vector2();
            p.set(lineStart).lerp(lineEnd, local);
            pathPoints.add(p);
        }
    }

    /**
     * 把 pathPoints 变成有宽度的带状多边形。
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
     * 计算贝塞尔曲线末端方向。
     * 飞出去的方向就是曲线最后一段的切线方向。
     */
    private void calculateFlyDirection() {
        curve.valueAt(1F, endPoint);

        float backT = 0.98F;
        curve.valueAt(backT, prevEndPoint);

        flyDir.set(endPoint).sub(prevEndPoint);

        if (flyDir.len2() <= 0.00001F) {
            for (int i = 1; i <= 10; i++) {
                backT = 1F - i * 0.05F;
                if (backT < 0F) {
                    backT = 0F;
                }

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

    /**
     * 如果你有箭头头部 Actor，可以让它跟着路径头部移动。
     */
    private void updateFollowActor() {
        if (followActor == null) {
            return;
        }

        float totalDuration = drawDuration + flyOutDuration;

        if (elapsed <= drawDuration) {
            float rawProgress = drawDuration <= 0F ? 1F : elapsed / drawDuration;
            rawProgress = clamp01(rawProgress);

            float progress = drawInterpolation.apply(rawProgress);

            curve.valueAt(progress, tmp);
            followActor.setPosition(tmp.x, tmp.y, Align.center);
            return;
        }

        calculateFlyDirection();
        curve.valueAt(1F, endPoint);

        if (elapsed <= totalDuration) {
            float rawProgress = flyOutDuration <= 0F
                    ? 1F
                    : (elapsed - drawDuration) / flyOutDuration;

            rawProgress = clamp01(rawProgress);

            float flyProgress = flyInterpolation.apply(rawProgress);

//            tmp.set(endPoint).mulAdd(flyDir, flyDistance * flyProgress);
            float realFlyDistance = getRealFlyDistance();
            tmp.set(endPoint).mulAdd(flyDir, realFlyDistance * flyProgress);
            followActor.setPosition(tmp.x, tmp.y, Align.center);
            return;
        }

        float extraTime = elapsed - totalDuration;
        float extraDistance = extraTime * continueFlySpeed;
        float realFlyDistance = getRealFlyDistance();
        tmp.set(endPoint).mulAdd(flyDir, realFlyDistance + extraDistance);
//        tmp.set(endPoint).mulAdd(flyDir, flyDistance + extraDistance);
        followActor.setPosition(tmp.x, tmp.y, Align.center);
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