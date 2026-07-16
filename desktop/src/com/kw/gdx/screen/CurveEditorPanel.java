package com.kw.gdx.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.tony.dominoes.curve.CurveSegment;
import com.tony.dominoes.curve.CurveType;

import java.util.ArrayList;
import java.util.List;

/**
 * Standalone reconstruction of Spine's Graph/Curve panel.
 *
 * Reference mapping:
 * - TQ.java: UI panel with Linear, Bezier, Stepped, Match and Presets.
 * - Gp_1.java: curve type enum: linear, curve, stepped.
 * - Gg_1.java: keyframe stores curve type plus Bezier c1/c2/c3/c4.
 * - sZ_3.java: exports curve as "stepped" or curve/c2/c3/c4.
 */
final class CurveEditorPanel {
    private static final int NONE = 0;
    private static final int HANDLE_1 = 1;
    private static final int HANDLE_2 = 2;
    private static final float GRAPH_MIN_Y = -1.0f;
    private static final float GRAPH_MAX_Y = 2.0f;

    private final CurveSegment curve = new CurveSegment();
    private final List<GraphPreset> presets = new ArrayList<GraphPreset>();

    private final Rectangle panel = new Rectangle();
    private final Rectangle toolbar = new Rectangle();
    private final Rectangle graph = new Rectangle();
    private final Rectangle presetsBox = new Rectangle();
    private final Rectangle linearButton = new Rectangle();
    private final Rectangle bezierButton = new Rectangle();
    private final Rectangle steppedButton = new Rectangle();
    private final Rectangle matchButton = new Rectangle();
    private final Rectangle plusButton = new Rectangle();
    private final Rectangle minusButton = new Rectangle();

    private int selectedPreset;
    private int dragHandle;
    private float time;

    CurveEditorPanel() {
        curve.setBezier(0.25f, 0.0f, 0.75f, 1.0f);
        presets.add(new GraphPreset("Default", CurveType.BEZIER, 0.25f, 0.0f, 0.75f, 1.0f));
        presets.add(new GraphPreset("Ease in", CurveType.BEZIER, 0.45f, 0.0f, 0.85f, 1.0f));
        presets.add(new GraphPreset("Ease out", CurveType.BEZIER, 0.15f, 0.0f, 0.55f, 1.0f));
        presets.add(new GraphPreset("Stepped", CurveType.STEPPED, 0.25f, 0.0f, 0.75f, 1.0f));
    }

    void layout(float width, float height) {
        float w = Math.min(width - 80f, 1060f);
        float h = Math.min(height - 120f, 780f);
        panel.set((width - w) * 0.5f, (height - h) * 0.5f, w, h);
        toolbar.set(panel.x + 18f, panel.y + panel.height - 88f, panel.width - 36f, 60f);

        float availableGraphW = panel.width - 370f;
        float availableGraphH = panel.height - 210f;
        float graphValueRange = GRAPH_MAX_Y - GRAPH_MIN_Y;
        float unitSize = Math.min(availableGraphW, availableGraphH / graphValueRange);
        graph.set(panel.x + 36f, panel.y + 92f, unitSize, unitSize * graphValueRange);
        presetsBox.set(graph.x + graph.width + 28f, graph.y, 270f, graph.height);

        linearButton.set(toolbar.x + 92f, toolbar.y + 12f, 54f, 36f);
        bezierButton.set(linearButton.x + 62f, linearButton.y, 54f, 36f);
        steppedButton.set(bezierButton.x + 62f, linearButton.y, 54f, 36f);
        matchButton.set(steppedButton.x + 82f, linearButton.y, 100f, 36f);
        plusButton.set(presetsBox.x + presetsBox.width - 72f, presetsBox.y + 14f, 28f, 28f);
        minusButton.set(presetsBox.x + presetsBox.width - 36f, presetsBox.y + 14f, 28f, 28f);
    }

    void update(float delta) {
        time += delta * 0.35f;
        if (time > 1f) time -= 1f;
    }

    boolean touchDown(float x, float y) {
        if (linearButton.contains(x, y)) {
            curve.setLinear();
            return true;
        }
        if (bezierButton.contains(x, y)) {
            curve.setBezier(curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2());
            return true;
        }
        if (steppedButton.contains(x, y)) {
            curve.setStepped();
            return true;
        }
        if (matchButton.contains(x, y)) {
            applyPreset(selectedPreset);
            return true;
        }
        if (plusButton.contains(x, y)) {
            presets.add(new GraphPreset("Preset " + presets.size(), curve.getType(),
                    curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2()));
            selectedPreset = presets.size() - 1;
            return true;
        }
        if (minusButton.contains(x, y) && selectedPreset > 0 && presets.size() > 1) {
            presets.remove(selectedPreset);
            selectedPreset = Math.max(0, selectedPreset - 1);
            return true;
        }
        if (presetsBox.contains(x, y)) {
            int index = (int)((presetsBox.y + presetsBox.height - 72f - y) / 38f);
            if (index >= 0 && index < presets.size()) {
                selectedPreset = index;
                applyPreset(index);
                return true;
            }
        }
        if (curve.getType() == CurveType.BEZIER) {
            float h1x = graph.x + curve.getX1() * graph.width;
            float h1y = graphY(curve.getY1());
            float h2x = graph.x + curve.getX2() * graph.width;
            float h2y = graphY(curve.getY2());
            if (distance2(x, y, h1x, h1y) < 420f) {
                dragHandle = HANDLE_1;
                return true;
            }
            if (distance2(x, y, h2x, h2y) < 420f) {
                dragHandle = HANDLE_2;
                return true;
            }
        }
        return false;
    }

    void touchDragged(float x, float y) {
        if (dragHandle == NONE) return;
        float nx = MathUtils.clamp((x - graph.x) / graph.width, 0f, 1f);
        float ny = graphValue(y);
        if (dragHandle == HANDLE_1) {
            curve.setBezier(nx, ny, curve.getX2(), curve.getY2());
        } else if (dragHandle == HANDLE_2) {
            curve.setBezier(curve.getX1(), curve.getY1(), nx, ny);
        }
    }

    void touchUp() {
        dragHandle = NONE;
    }

    void draw(Batch batch, ShapeRenderer shapes, BitmapFont font, Texture pixel) {
        drawBackground(batch, pixel);
        drawGraph(shapes);
        drawLabels(batch, font, pixel);
    }

    private void applyPreset(int index) {
        GraphPreset preset = presets.get(index);
        if (preset.type == CurveType.LINEAR) {
            curve.setLinear();
        } else if (preset.type == CurveType.STEPPED) {
            curve.setStepped();
        } else {
            curve.setBezier(preset.x1, preset.y1, preset.x2, preset.y2);
        }
    }

    private void drawBackground(Batch batch, Texture pixel) {
        batch.begin();
        rect(batch, pixel, panel, 0.082f, 0.090f, 0.100f, 1f);
        rect(batch, pixel, toolbar, 0.12f, 0.13f, 0.145f, 1f);
        rect(batch, pixel, graph.x, graph.y, graph.width, graph.height, 0.055f, 0.060f, 0.068f, 1f);
        rect(batch, pixel, presetsBox, 0.11f, 0.12f, 0.13f, 1f);
        drawButton(batch, pixel, linearButton, curve.getType() == CurveType.LINEAR);
        drawButton(batch, pixel, bezierButton, curve.getType() == CurveType.BEZIER);
        drawButton(batch, pixel, steppedButton, curve.getType() == CurveType.STEPPED);
        drawButton(batch, pixel, matchButton, false);
        drawButton(batch, pixel, plusButton, false);
        drawButton(batch, pixel, minusButton, false);

        for (int i = 0; i < presets.size(); i++) {
            float y = presetsBox.y + presetsBox.height - 88f - i * 38f;
            float r = i == selectedPreset ? 0.20f : 0.14f;
            rect(batch, pixel, presetsBox.x + 12f, y, presetsBox.width - 24f, 30f, r, r + 0.01f, r + 0.025f, 1f);
        }
        batch.end();
    }

    private void drawGraph(ShapeRenderer shapes) {
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(0.30f, 0.32f, 0.35f, 1f);
        shapes.rect(graph.x, graph.y, graph.width, graph.height);
        for (int i = 1; i < 4; i++) {
            float p = i / 4f;
            shapes.line(graph.x + graph.width * p, graph.y, graph.x + graph.width * p, graph.y + graph.height);
            shapes.line(graph.x, graph.y + graph.height * p, graph.x + graph.width, graph.y + graph.height * p);
        }
        shapes.setColor(0.22f, 0.25f, 0.28f, 1f);
        shapes.line(graph.x, graphY(0f), graph.x + graph.width, graphY(0f));
        shapes.line(graph.x, graphY(1f), graph.x + graph.width, graphY(1f));

        shapes.setColor(0.25f, 0.50f, 0.95f, 1f);
        shapes.line(graph.x, graphY(0f), graph.x + graph.width, graphY(1f));

        shapes.setColor(0.98f, 0.70f, 0.20f, 1f);
        float lastX = graph.x;
        float lastY = graphY(displayValue(0f));
        for (int i = 1; i <= 96; i++) {
            float p = i / 96f;
            float x = graph.x + p * graph.width;
            float y = graphY(displayValue(p));
            shapes.line(lastX, lastY, x, y);
            lastX = x;
            lastY = y;
        }

        if (curve.getType() == CurveType.BEZIER) {
            float x1 = graph.x + curve.getX1() * graph.width;
            float y1 = graphY(curve.getY1());
            float x2 = graph.x + curve.getX2() * graph.width;
            float y2 = graphY(curve.getY2());
            shapes.setColor(0.32f, 0.82f, 0.58f, 1f);
            shapes.line(graph.x, graphY(0f), x1, y1);
            shapes.line(x2, y2, graph.x + graph.width, graphY(1f));
        }
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        float tx = graph.x + time * graph.width;
        float ty = graphY(displayValue(time));
        shapes.setColor(0.95f, 0.22f, 0.28f, 1f);
        shapes.circle(tx, ty, 8f);
        if (curve.getType() == CurveType.BEZIER) {
            shapes.setColor(0.32f, 0.82f, 0.58f, 1f);
            shapes.circle(graph.x + curve.getX1() * graph.width, graphY(curve.getY1()), 7f);
            shapes.circle(graph.x + curve.getX2() * graph.width, graphY(curve.getY2()), 7f);
        }
        shapes.end();
    }

    private void drawLabels(Batch batch, BitmapFont font, Texture pixel) {
        batch.begin();
        text(batch, font, "Curve:", toolbar.x + 16f, toolbar.y + 38f, 0.86f, 0.89f, 0.93f);
        text(batch, font, "L", linearButton.x + 20f, linearButton.y + 25f, 0.90f, 0.92f, 0.95f);
        text(batch, font, "B", bezierButton.x + 20f, bezierButton.y + 25f, 0.90f, 0.92f, 0.95f);
        text(batch, font, "S", steppedButton.x + 20f, steppedButton.y + 25f, 0.90f, 0.92f, 0.95f);
        text(batch, font, "Match", matchButton.x + 18f, matchButton.y + 25f, 0.90f, 0.92f, 0.95f);
        text(batch, font, "Presets", presetsBox.x + 14f, presetsBox.y + presetsBox.height - 22f, 0.86f, 0.89f, 0.93f);
        text(batch, font, "+", plusButton.x + 8f, plusButton.y + 22f, 0.90f, 0.92f, 0.95f);
        text(batch, font, "-", minusButton.x + 10f, minusButton.y + 22f, 0.90f, 0.92f, 0.95f);

        for (int i = 0; i < presets.size(); i++) {
            GraphPreset preset = presets.get(i);
            float y = presetsBox.y + presetsBox.height - 68f - i * 38f;
            text(batch, font, preset.name, presetsBox.x + 24f, y, 0.78f, 0.82f, 0.88f);
        }

        text(batch, font, "Type: " + curve.getType(), graph.x, graph.y - 26f, 0.72f, 0.76f, 0.82f);
        text(batch, font, exportText(), graph.x, graph.y - 56f, 0.72f, 0.76f, 0.82f);
        text(batch, font, "Drag green handles. X is independent, Y can go below 0 or above 1.", graph.x, panel.y -242f, 0.56f, 0.60f, 0.66f);
        batch.end();
    }

    private String exportText() {
        if (curve.getType() == CurveType.LINEAR) return "JSON: no curve field";
        if (curve.getType() == CurveType.STEPPED) return "JSON: \"curve\":\"stepped\"";
        return "JSON: curve=" + round(curve.getX1()) + ", c2=" + round(curve.getY1())
                + ", c3=" + round(curve.getX2()) + ", c4=" + round(curve.getY2());
    }

    private float displayValue(float percent) {
        if (curve.getType() == CurveType.STEPPED) return percent >= 1f ? 1f : 0f;
        return curve.map(percent);
    }

    private float graphY(float value) {
        float percent = (value - GRAPH_MIN_Y) / (GRAPH_MAX_Y - GRAPH_MIN_Y);
        return graph.y + percent * graph.height;
    }

    private float graphValue(float screenY) {
        float percent = (screenY - graph.y) / graph.height;
        return GRAPH_MIN_Y + percent * (GRAPH_MAX_Y - GRAPH_MIN_Y);
    }

    private void drawButton(Batch batch, Texture pixel, Rectangle r, boolean selected) {
        float base = selected ? 0.26f : 0.15f;
        rect(batch, pixel, r, base, base + 0.01f, base + 0.03f, 1f);
    }

    private static float distance2(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return dx * dx + dy * dy;
    }

    private static String round(float value) {
        return String.format("%.2f", value);
    }

    private static void text(Batch batch, BitmapFont font, String text, float x, float y,
                             float r, float g, float b) {
        font.setColor(r, g, b, 1f);
        font.draw(batch, text, x, y);
    }

    private static void rect(Batch batch, Texture pixel, Rectangle r, float red, float green, float blue, float alpha) {
        rect(batch, pixel, r.x, r.y, r.width, r.height, red, green, blue, alpha);
    }

    private static void rect(Batch batch, Texture pixel, float x, float y, float w, float h,
                             float red, float green, float blue, float alpha) {
        Color old = batch.getColor();
        float oldR = old.r;
        float oldG = old.g;
        float oldB = old.b;
        float oldA = old.a;
        batch.setColor(red, green, blue, alpha);
        batch.draw(pixel, x, y, w, h);
        batch.setColor(oldR, oldG, oldB, oldA);
    }

    private static final class GraphPreset {
        final String name;
        final CurveType type;
        final float x1;
        final float y1;
        final float x2;
        final float y2;

        GraphPreset(String name, CurveType type, float x1, float y1, float x2, float y2) {
            this.name = name;
            this.type = type;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }
}
