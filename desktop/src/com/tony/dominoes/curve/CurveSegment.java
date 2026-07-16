package com.tony.dominoes.curve;

import com.badlogic.gdx.math.MathUtils;

public class CurveSegment {
    private static final int SAMPLE_COUNT = 10;

    private final float[] sampleX = new float[SAMPLE_COUNT];
    private final float[] sampleY = new float[SAMPLE_COUNT];

    private CurveType type = CurveType.LINEAR;
    private float x1 = 0.25f;
    private float y1 = 0.0f;
    private float x2 = 0.75f;
    private float y2 = 1.0f;

    public CurveSegment() {
        rebuildSamples();
    }

    public CurveType getType() {
        return type;
    }

    public float getX1() {
        return x1;
    }

    public float getY1() {
        return y1;
    }

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }

    public void setLinear() {
        type = CurveType.LINEAR;
    }

    public void setStepped() {
        type = CurveType.STEPPED;
    }

    public void setBezier(float x1, float y1, float x2, float y2) {
        type = CurveType.BEZIER;
        this.x1 = MathUtils.clamp(x1, 0.0f, 1.0f);
        this.y1 = y1;
        this.x2 = MathUtils.clamp(x2, 0.0f, 1.0f);
        this.y2 = y2;
        rebuildSamples();
    }

    public float map(float percent) {
        percent = MathUtils.clamp(percent, 0.0f, 1.0f);
        if (type == CurveType.STEPPED) {
            return 0.0f;
        }
        if (type == CurveType.LINEAR) {
            return percent;
        }

        float previousX = 0.0f;
        float previousY = 0.0f;
        for (int i = 0; i < SAMPLE_COUNT; i++) {
            float currentX = sampleX[i];
            float currentY = sampleY[i];
            if (currentX >= percent) {
                float width = currentX - previousX;
                if (width <= 0.0001f) {
                    return currentY;
                }
                float alpha = (percent - previousX) / width;
                return previousY + (currentY - previousY) * alpha;
            }
            previousX = currentX;
            previousY = currentY;
        }
        return 1.0f;
    }

    public float sampleBezierX(float t) {
        return cubic(t, x1, x2);
    }

    public float sampleBezierY(float t) {
        return cubic(t, y1, y2);
    }

    private void rebuildSamples() {
        for (int i = 0; i < SAMPLE_COUNT; i++) {
            float t = (i + 1) / (float) SAMPLE_COUNT;
            sampleX[i] = sampleBezierX(t);
            sampleY[i] = sampleBezierY(t);
        }
    }

    private static float cubic(float t, float p1, float p2) {
        float inv = 1.0f - t;
        return 3.0f * inv * inv * t * p1
                + 3.0f * inv * t * t * p2
                + t * t * t;
    }
}
