package com.tony.dominoes.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.kw.gdx.BaseGame;
import com.kw.gdx.screen.BaseScreen;
import com.tony.dominoes.curve.CurveSegment;
import com.tony.dominoes.curve.CurveType;
import com.tony.dominoes.curve.FloatKeyframe;
import com.tony.dominoes.curve.FloatTimeline;

public class CurveDemoScreen extends BaseScreen {
    private static final float DURATION = 2.0f;

    private final CurveSegment curve = new CurveSegment();
    private final FloatTimeline timeline = new FloatTimeline();
    private ShapeRenderer shapes;
    private BitmapFont font;
    private float time;
    private boolean playing = true;

    public CurveDemoScreen(BaseGame game) {
        super(game);
    }

    @Override
    protected void initData() {
        curve.setBezier(0.25f, 0.0f, 0.75f, 1.0f);
        timeline.add(new FloatKeyframe(0.0f, 80.0f, curve));
        timeline.add(new FloatKeyframe(DURATION, 320.0f));
    }

    @Override
    public void initView() {
        shapes = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        if (playing) {
            time += delta;
            while (time > DURATION) {
                time -= DURATION;
            }
        }

        stage.act(delta);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapes.setProjectionMatrix(stage.getCamera().combined);
        Batch batch = game.getBatch();
        batch.setProjectionMatrix(stage.getCamera().combined);

        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        float chartSize = Math.min(width - 96.0f, height * 0.42f);
        float chartX = (width - chartSize) * 0.5f;
        float chartY = height * 0.45f;
        float value = timeline.evaluate(time);
        float percent = time / DURATION;
        float mapped = curve.map(percent);

        drawBackground(width, height);
        drawCurveChart(chartX, chartY, chartSize, percent, mapped);
        drawValueTrack(width, height, value);
        drawText(batch, width, height, value, percent, mapped);
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            playing = !playing;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            time = 0.0f;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            curve.setLinear();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            curve.setBezier(curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            curve.setStepped();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playing = false;
            time = MathUtils.clamp(time - delta, 0.0f, DURATION);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playing = false;
            time = MathUtils.clamp(time + delta, 0.0f, DURATION);
        }

        if (curve.getType() == CurveType.BEZIER) {
            float speed = delta * 0.65f;
            float x1 = curve.getX1();
            float y1 = curve.getY1();
            float x2 = curve.getX2();
            float y2 = curve.getY2();
            if (Gdx.input.isKeyPressed(Input.Keys.A)) x1 -= speed;
            if (Gdx.input.isKeyPressed(Input.Keys.Z)) x1 += speed;
            if (Gdx.input.isKeyPressed(Input.Keys.S)) y1 -= speed;
            if (Gdx.input.isKeyPressed(Input.Keys.X)) y1 += speed;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) x2 -= speed;
            if (Gdx.input.isKeyPressed(Input.Keys.C)) x2 += speed;
            if (Gdx.input.isKeyPressed(Input.Keys.F)) y2 -= speed;
            if (Gdx.input.isKeyPressed(Input.Keys.V)) y2 += speed;
            curve.setBezier(x1, y1, x2, y2);
        }
    }

    private void drawBackground(float width, float height) {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.06f, 0.07f, 0.08f, 1.0f);
        shapes.rect(0.0f, 0.0f, width, height);
        shapes.setColor(0.12f, 0.13f, 0.14f, 1.0f);
        shapes.rect(0.0f, 0.0f, width, height * 0.18f);
        shapes.end();
    }

    private void drawCurveChart(float x, float y, float size, float percent, float mapped) {
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(0.42f, 0.46f, 0.50f, 1.0f);
        shapes.rect(x, y, size, size);
        for (int i = 1; i < 4; i++) {
            float p = i / 4.0f;
            shapes.line(x + size * p, y, x + size * p, y + size);
            shapes.line(x, y + size * p, x + size, y + size * p);
        }

        shapes.setColor(0.30f, 0.56f, 0.95f, 1.0f);
        shapes.line(x, y, x + size, y + size);

        shapes.setColor(0.96f, 0.72f, 0.26f, 1.0f);
        float lastX = x;
        float lastY = y + sampleDisplayCurve(0.0f) * size;
        for (int i = 1; i <= 80; i++) {
            float p = i / 80.0f;
            float sx = x + p * size;
            float sy = y + sampleDisplayCurve(p) * size;
            shapes.line(lastX, lastY, sx, sy);
            lastX = sx;
            lastY = sy;
        }

        if (curve.getType() == CurveType.BEZIER) {
            shapes.setColor(0.32f, 0.82f, 0.58f, 1.0f);
            shapes.line(x, y, x + curve.getX1() * size, y + curve.getY1() * size);
            shapes.line(x + curve.getX2() * size, y + curve.getY2() * size, x + size, y + size);
        }
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.95f, 0.22f, 0.28f, 1.0f);
        shapes.circle(x + percent * size, y + mapped * size, 8.0f);
        if (curve.getType() == CurveType.BEZIER) {
            shapes.setColor(0.32f, 0.82f, 0.58f, 1.0f);
            shapes.circle(x + curve.getX1() * size, y + curve.getY1() * size, 6.0f);
            shapes.circle(x + curve.getX2() * size, y + curve.getY2() * size, 6.0f);
        }
        shapes.end();
    }

    private float sampleDisplayCurve(float percent) {
        if (curve.getType() == CurveType.STEPPED) {
            return percent >= 1.0f ? 1.0f : 0.0f;
        }
        return curve.map(percent);
    }

    private void drawValueTrack(float width, float height, float value) {
        float left = 64.0f;
        float right = width - 64.0f;
        float y = height * 0.28f;
        float alpha = (value - 80.0f) / (320.0f - 80.0f);

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(0.62f, 0.65f, 0.68f, 1.0f);
        shapes.line(left, y, right, y);
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.30f, 0.56f, 0.95f, 1.0f);
        shapes.circle(left, y, 7.0f);
        shapes.circle(right, y, 7.0f);
        shapes.setColor(0.95f, 0.22f, 0.28f, 1.0f);
        shapes.circle(left + (right - left) * alpha, y, 12.0f);
        shapes.end();
    }

    private void drawText(Batch batch, float width, float height, float value, float percent, float mapped) {
        batch.begin();
        font.draw(batch, "Curve timeline demo", 48.0f, height - 48.0f);
        font.draw(batch, "1 linear   2 bezier   3 stepped   space pause   R reset   arrows scrub", 48.0f, height - 82.0f);
        font.draw(batch, "Bezier edit: A/Z x1  S/X y1  D/C x2  F/V y2", 48.0f, height - 116.0f);
        font.draw(batch, "type=" + curve.getType()
                + "  time=" + round(time)
                + "  raw=" + round(percent)
                + "  mapped=" + round(mapped)
                + "  value=" + round(value), 48.0f, height * 0.22f);
        font.draw(batch, "controls=(" + round(curve.getX1()) + ", " + round(curve.getY1()) + ")  ("
                + round(curve.getX2()) + ", " + round(curve.getY2()) + ")", 48.0f, height * 0.22f - 34.0f);
        batch.end();
    }

    private static String round(float value) {
        return String.format("%.2f", value);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (shapes != null) {
            shapes.dispose();
        }
        if (font != null) {
            font.dispose();
        }
    }
}
