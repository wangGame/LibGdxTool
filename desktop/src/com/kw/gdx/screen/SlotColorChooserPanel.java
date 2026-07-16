package com.kw.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Standalone slot color chooser panel.
 *
 * This mirrors the Spine-style color dialog:
 * title bar, alpha strip, saturation/brightness square, hue strip,
 * preview, HSB/RGB/A values, HEX, OK, and Cancel.
 */
final class SlotColorChooserPanel {
    interface Listener {
        void applyColor(int target, Color color);
    }

    private final Listener listener;
    private final Rectangle panel = new Rectangle();
    private final Rectangle titleBar = new Rectangle();
    private final Rectangle alphaBar = new Rectangle();
    private final Rectangle colorSquare = new Rectangle();
    private final Rectangle hueBar = new Rectangle();
    private final Rectangle previewBox = new Rectangle();
    private final Rectangle okButton = new Rectangle();
    private final Rectangle cancelButton = new Rectangle();
    private final Rectangle hexBox = new Rectangle();
    private final Rectangle[] valueBoxes = new Rectangle[7];

    private final Color editing = new Color();
    private final Color original = new Color();

    private boolean open;
    private boolean alpha;
    private int target;
    private int activeArea;
    private String title = "";
    private float hue;
    private float saturation;
    private float brightness;
    private float screenWidth;
    private float screenHeight;

    SlotColorChooserPanel(Listener listener) {
        this.listener = listener;
        for (int i = 0; i < valueBoxes.length; i++) {
            valueBoxes[i] = new Rectangle();
        }
    }

    boolean isOpen() {
        return open;
    }

    void layout(float width, float height) {
        screenWidth = width;
        screenHeight = height;

        float w = Math.min(width - 70f, 990f);
        float h = Math.min(height - 140f, 760f);
        panel.set((width - w) * 0.5f, (height - h) * 0.5f, w, h);
        titleBar.set(panel.x, panel.y + panel.height - 44f, panel.width, 44f);
        alphaBar.set(panel.x + 20f, panel.y + 78f, 42f, panel.height - 138f);
        colorSquare.set(panel.x + 76f, panel.y + 78f, panel.width - 292f, panel.height - 138f);
        hueBar.set(colorSquare.x + colorSquare.width + 14f, colorSquare.y, 42f, colorSquare.height);
        previewBox.set(hueBar.x + hueBar.width + 18f, panel.y + panel.height - 140f, 128f, 82f);

        float boxX = previewBox.x + 58f;
        float y = previewBox.y - 48f;
        for (int i = 0; i < valueBoxes.length; i++) {
            valueBoxes[i].set(boxX, y - i * 44f, 70f, 32f);
        }
        hexBox.set(previewBox.x, panel.y + 95f, 128f, 34f);
        okButton.set(panel.x + panel.width * 0.5f - 95f, panel.y + 28f, 86f, 38f);
        cancelButton.set(panel.x + panel.width * 0.5f + 15f, panel.y + 28f, 86f, 38f);
    }

    void open(String title, int target, Color color, boolean alpha) {
        this.open = true;
        this.title = title;
        this.target = target;
        this.alpha = alpha;
        this.editing.set(color);
        this.original.set(color);
        syncHsvFromRgb();
    }

    boolean touchDown(float x, float y) {
        if (okButton.contains(x, y)) {
            apply();
            open = false;
            activeArea = 0;
            return true;
        }
        if (cancelButton.contains(x, y)) {
            editing.set(original);
            apply();
            open = false;
            activeArea = 0;
            return true;
        }
        if (colorSquare.contains(x, y)) {
            activeArea = 1;
            setColorSquareValue(x, y);
            apply();
            return true;
        }
        if (hueBar.contains(x, y)) {
            activeArea = 2;
            setHueValue(y);
            apply();
            return true;
        }
        if (alpha && alphaBar.contains(x, y)) {
            activeArea = 3;
            setAlphaValue(y);
            apply();
            return true;
        }
        return true;
    }

    void touchDragged(float x, float y) {
        if (activeArea == 1) {
            setColorSquareValue(x, y);
        } else if (activeArea == 2) {
            setHueValue(y);
        } else if (activeArea == 3) {
            setAlphaValue(y);
        } else {
            return;
        }
        apply();
    }

    boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ENTER) {
            apply();
            open = false;
            return true;
        }
        if (keycode == Input.Keys.ESCAPE) {
            editing.set(original);
            apply();
            open = false;
            return true;
        }
        return false;
    }

    void draw(Batch batch, BitmapFont font, Texture pixel) {
        drawRect(batch, pixel, 0, 0, screenWidth, screenHeight, 0f, 0f, 0f, 0.38f);
        drawRect(batch, pixel, panel.x, panel.y, panel.width, panel.height, 0.10f, 0.11f, 0.12f, 1f);
        drawRect(batch, pixel, titleBar.x, titleBar.y, titleBar.width, titleBar.height, 0.18f, 0.19f, 0.20f, 1f);
        drawRect(batch, pixel, titleBar.x + 1f, titleBar.y + 1f, 180f, titleBar.height - 2f,
                0.05f, 0.35f, 0.56f, 1f);
        drawText(batch, font, title, titleBar.x + 18f, titleBar.y + 28f, 0.92f, 0.96f, 1f, 1f);

        drawChecker(batch, pixel, alphaBar, 8f);
        drawAlphaOverlay(batch, pixel);
        drawVerticalHandle(batch, pixel, alphaBar, alphaBar.y + alphaBar.height * editing.a);

        drawColorSquare(batch, pixel);
        drawColorCursor(batch, pixel);
        drawHueBar(batch, pixel);
        drawVerticalHandle(batch, pixel, hueBar, hueBar.y + hueBar.height * (1f - hue));

        drawRect(batch, pixel, previewBox.x, previewBox.y, previewBox.width, previewBox.height,
                editing.r, editing.g, editing.b, editing.a);
        drawValueList(batch, font, pixel);

        drawRect(batch, pixel, panel.x, panel.y, panel.width, 70f, 0.23f, 0.24f, 0.25f, 1f);
        drawButton(batch, font, pixel, okButton, "确定");
        drawButton(batch, font, pixel, cancelButton, "取消");
    }

    private void apply() {
        listener.applyColor(target, editing);
    }

    private void setColorSquareValue(float x, float y) {
        saturation = MathUtils.clamp((x - colorSquare.x) / colorSquare.width, 0f, 1f);
        brightness = MathUtils.clamp((y - colorSquare.y) / colorSquare.height, 0f, 1f);
        applyHsv();
    }

    private void setHueValue(float y) {
        hue = 1f - MathUtils.clamp((y - hueBar.y) / hueBar.height, 0f, 1f);
        applyHsv();
    }

    private void setAlphaValue(float y) {
        editing.a = MathUtils.clamp((y - alphaBar.y) / alphaBar.height, 0f, 1f);
    }

    private void applyHsv() {
        Color c = hsvToRgb(hue, saturation, brightness);
        editing.r = c.r;
        editing.g = c.g;
        editing.b = c.b;
    }

    private void syncHsvFromRgb() {
        float max = Math.max(editing.r, Math.max(editing.g, editing.b));
        float min = Math.min(editing.r, Math.min(editing.g, editing.b));
        float delta = max - min;
        float h = 0f;
        if (delta != 0f) {
            if (max == editing.r) {
                h = ((editing.g - editing.b) / delta) % 6f;
            } else if (max == editing.g) {
                h = (editing.b - editing.r) / delta + 2f;
            } else {
                h = (editing.r - editing.g) / delta + 4f;
            }
            h /= 6f;
            if (h < 0f) h += 1f;
        }
        hue = h;
        saturation = max == 0f ? 0f : delta / max;
        brightness = max;
    }

    private void drawValueList(Batch batch, BitmapFont font, Texture pixel) {
        String[] labels = {"H", "S", "B", "R", "G", "B", "A"};
        String[] texts = {
                String.valueOf(Math.round(hue * 360f)),
                String.valueOf(Math.round(saturation * 100f)),
                String.valueOf(Math.round(brightness * 100f)),
                String.valueOf(Math.round(editing.r * 255f)),
                String.valueOf(Math.round(editing.g * 255f)),
                String.valueOf(Math.round(editing.b * 255f)),
                String.valueOf(Math.round(editing.a * 255f))
        };

        for (int i = 0; i < valueBoxes.length; i++) {
            Rectangle r = valueBoxes[i];
            float dotY = r.y + 16f;
            drawRadioDot(batch, pixel, r.x - 50f, dotY, i == 0);
            drawText(batch, font, labels[i] + ":", r.x - 30f, r.y + 22f, 0.82f, 0.86f, 0.90f, 1f);
            drawRect(batch, pixel, r.x, r.y, r.width, r.height, 0.25f, 0.26f, 0.27f, 1f);
            drawText(batch, font, texts[i], r.x + 9f, r.y + 23f, 0.90f, 0.92f, 0.94f, 1f);
        }

        drawRect(batch, pixel, hexBox.x, hexBox.y, hexBox.width, hexBox.height, 0.25f, 0.26f, 0.27f, 1f);
        drawText(batch, font, toHex(editing, true), hexBox.x + 8f, hexBox.y + 24f, 0.90f, 0.92f, 0.94f, 1f);
    }

    private void drawColorSquare(Batch batch, Texture pixel) {
        float step = 5f;
        for (float yy = 0; yy < colorSquare.height; yy += step) {
            float value = MathUtils.clamp(yy / colorSquare.height, 0f, 1f);
            for (float xx = 0; xx < colorSquare.width; xx += step) {
                float sat = MathUtils.clamp(xx / colorSquare.width, 0f, 1f);
                Color c = hsvToRgb(hue, sat, value);
                drawRect(batch, pixel, colorSquare.x + xx, colorSquare.y + yy,
                        Math.min(step + 1f, colorSquare.width - xx),
                        Math.min(step + 1f, colorSquare.height - yy), c.r, c.g, c.b, 1f);
            }
        }
    }

    private void drawHueBar(Batch batch, Texture pixel) {
        float step = 4f;
        for (float yy = 0; yy < hueBar.height; yy += step) {
            float sampleHue = 1f - MathUtils.clamp(yy / hueBar.height, 0f, 1f);
            Color c = hsvToRgb(sampleHue, 1f, 1f);
            drawRect(batch, pixel, hueBar.x, hueBar.y + yy, hueBar.width,
                    Math.min(step + 1f, hueBar.height - yy), c.r, c.g, c.b, 1f);
        }
    }

    private void drawAlphaOverlay(Batch batch, Texture pixel) {
        float step = 4f;
        for (float yy = 0; yy < alphaBar.height; yy += step) {
            float a = MathUtils.clamp(yy / alphaBar.height, 0f, 1f);
            drawRect(batch, pixel, alphaBar.x, alphaBar.y + yy, alphaBar.width,
                    Math.min(step + 1f, alphaBar.height - yy), editing.r, editing.g, editing.b, a);
        }
    }

    private void drawChecker(Batch batch, Texture pixel, Rectangle r, float size) {
        for (float yy = 0; yy < r.height; yy += size) {
            for (float xx = 0; xx < r.width; xx += size) {
                boolean dark = (((int)(xx / size) + (int)(yy / size)) & 1) == 0;
                float v = dark ? 0.78f : 0.93f;
                drawRect(batch, pixel, r.x + xx, r.y + yy,
                        Math.min(size, r.width - xx), Math.min(size, r.height - yy), v, v, v, 1f);
            }
        }
    }

    private void drawColorCursor(Batch batch, Texture pixel) {
        float x = colorSquare.x + saturation * colorSquare.width;
        float y = colorSquare.y + brightness * colorSquare.height;
        drawRect(batch, pixel, x - 6f, y - 1f, 12f, 2f, 0f, 0f, 0f, 1f);
        drawRect(batch, pixel, x - 1f, y - 6f, 2f, 12f, 0f, 0f, 0f, 1f);
        drawRect(batch, pixel, x - 4f, y - 4f, 8f, 8f, 1f, 1f, 1f, 1f);
        drawRect(batch, pixel, x - 2f, y - 2f, 4f, 4f, 0f, 0f, 0f, 1f);
    }

    private void drawVerticalHandle(Batch batch, Texture pixel, Rectangle bar, float y) {
        y = MathUtils.clamp(y, bar.y, bar.y + bar.height);
        drawRect(batch, pixel, bar.x - 4f, y - 4f, bar.width + 8f, 8f, 0f, 0f, 0f, 1f);
        drawRect(batch, pixel, bar.x - 2f, y - 2f, bar.width + 4f, 4f, 1f, 1f, 1f, 1f);
    }

    private void drawRadioDot(Batch batch, Texture pixel, float x, float y, boolean selected) {
        drawRect(batch, pixel, x - 7f, y - 7f, 14f, 14f, 0f, 0f, 0f, 1f);
        drawRect(batch, pixel, x - 5f, y - 5f, 10f, 10f, 0.86f, 0.88f, 0.90f, 1f);
        if (selected) {
            drawRect(batch, pixel, x - 2f, y - 2f, 4f, 4f, 0.05f, 0.06f, 0.07f, 1f);
        }
    }

    private static Color hsvToRgb(float h, float s, float v) {
        float r;
        float g;
        float b;
        if (s == 0f) {
            r = g = b = v;
        } else {
            float sector = h * 6f;
            int i = (int)Math.floor(sector);
            float f = sector - i;
            float p = v * (1f - s);
            float q = v * (1f - s * f);
            float t = v * (1f - s * (1f - f));
            switch (i % 6) {
                case 0: r = v; g = t; b = p; break;
                case 1: r = q; g = v; b = p; break;
                case 2: r = p; g = v; b = t; break;
                case 3: r = p; g = q; b = v; break;
                case 4: r = t; g = p; b = v; break;
                default: r = v; g = p; b = q; break;
            }
        }
        return new Color(r, g, b, 1f);
    }

    private static void drawButton(Batch batch, BitmapFont font, Texture pixel, Rectangle r, String text) {
        drawRect(batch, pixel, r.x, r.y, r.width, r.height, 0.13f, 0.14f, 0.16f, 1f);
        drawText(batch, font, text, r.x + 15f, r.y + 27f, 0.82f, 0.84f, 0.88f, 1f);
    }

    private static void drawText(Batch batch, BitmapFont font, String text, float x, float y,
                                 float r, float g, float b, float a) {
        font.setColor(r, g, b, a);
        font.draw(batch, text, x, y);
    }

    private static void drawRect(Batch batch, Texture pixel, float x, float y, float w, float h,
                                 float r, float g, float b, float a) {
        Color old = batch.getColor();
        float oldR = old.r;
        float oldG = old.g;
        float oldB = old.b;
        float oldA = old.a;
        batch.setColor(r, g, b, a);
        batch.draw(pixel, x, y, w, h);
        batch.setColor(oldR, oldG, oldB, oldA);
    }

    private static String toHex(Color color, boolean alpha) {
        int r = MathUtils.clamp(Math.round(color.r * 255f), 0, 255);
        int g = MathUtils.clamp(Math.round(color.g * 255f), 0, 255);
        int b = MathUtils.clamp(Math.round(color.b * 255f), 0, 255);
        int a = MathUtils.clamp(Math.round(color.a * 255f), 0, 255);
        if (alpha) {
            return two(r) + two(g) + two(b) + two(a);
        }
        return two(r) + two(g) + two(b);
    }

    private static String two(int value) {
        String text = Integer.toHexString(value).toUpperCase();
        return text.length() == 1 ? "0" + text : text;
    }
}
