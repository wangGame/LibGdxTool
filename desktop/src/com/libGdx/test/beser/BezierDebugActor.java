package com.libGdx.test.beser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BezierDebugActor extends Actor {

    private final ShapeRenderer renderer;
    private final BUL1 action;

    private final Vector2 prev = new Vector2();
    private final Vector2 curr = new Vector2();

    private int samples = 300;

    /**
     * true  ：debug 曲线进度和物体位置一致，受 Interpolation 影响
     * false ：debug 曲线按真实时间线性增长
     */
    private boolean useMoveT = true;

    public BezierDebugActor(ShapeRenderer renderer, BUL1 action) {
        this.renderer = renderer;
        this.action = action;
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

        if (endT <= 0) {
            return;
        }

        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Line);

        renderer.setColor(Color.WHITE);

        action.valueAt(0, prev);

        int drawSamples = Math.max(2, (int) (samples * endT));

        for (int i = 1; i <= drawSamples; i++) {
            float t = endT * i / drawSamples;

            action.valueAt(t, curr);
            renderer.line(prev.x, prev.y, curr.x, curr.y);

            prev.set(curr);
        }

        renderer.end();

        batch.begin();
    }
}