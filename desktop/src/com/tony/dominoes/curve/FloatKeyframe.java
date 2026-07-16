package com.tony.dominoes.curve;

public class FloatKeyframe {
    private final float time;
    private final float value;
    private final CurveSegment outgoingCurve;

    public FloatKeyframe(float time, float value) {
        this(time, value, new CurveSegment());
    }

    public FloatKeyframe(float time, float value, CurveSegment outgoingCurve) {
        this.time = time;
        this.value = value;
        this.outgoingCurve = outgoingCurve;
    }

    public float getTime() {
        return time;
    }

    public float getValue() {
        return value;
    }

    public CurveSegment getOutgoingCurve() {
        return outgoingCurve;
    }
}
