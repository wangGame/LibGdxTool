package com.tony.dominoes.curve;

import com.badlogic.gdx.utils.Array;

public class FloatTimeline {
    private final Array<FloatKeyframe> frames = new Array<FloatKeyframe>();

    public void add(FloatKeyframe frame) {
        frames.add(frame);
        frames.sort((left, right) -> Float.compare(left.getTime(), right.getTime()));
    }

    public float evaluate(float time) {
        if (frames.size == 0) {
            return 0.0f;
        }
        FloatKeyframe first = frames.first();
        if (time <= first.getTime()) {
            return first.getValue();
        }

        FloatKeyframe last = frames.peek();
        if (time >= last.getTime()) {
            return last.getValue();
        }

        for (int i = 0; i < frames.size - 1; i++) {
            FloatKeyframe from = frames.get(i);
            FloatKeyframe to = frames.get(i + 1);
            if (time <= to.getTime()) {
                float local = (time - from.getTime()) / (to.getTime() - from.getTime());
                float alpha = from.getOutgoingCurve().map(local);
                return from.getValue() + (to.getValue() - from.getValue()) * alpha;
            }
        }
        return last.getValue();
    }
}
