package com.kw.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Clean-room reconstruction of Spine's slot color property panel.
 *
 * Reference behavior:
 * - acS.java owns Set slot color, Set slot dark color, Tint black, and color key controls.
 * - vE/vH are the dialog and color chooser body.
 * - qk.C(Hq) writes the current slot light/dark color to a color timeline key.
 */
public class SlotColorScreen extends BaseScreen {
    private BitmapFont font;
    private Texture pixel;
    private SlotColorActor actor;

    public SlotColorScreen(BaseGame game) {
        super(game);
    }

    @Override
    protected void initData() {
        font = Asset.getAsset().loadBitFont("Ma-B_46.fnt");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void initView() {
        Gdx.app.log("SlotColorScreen", "open slot color chooser");
        actor = new SlotColorActor(font, pixel);
        actor.setBounds(0, 0, Constant.GAMEWIDTH, Constant.GAMEHIGHT);
        rootView.addActor(actor);
        actor.openLightColorPanel();
    }

    @Override
    protected void initTouch() {
        super.initTouch();
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE && actor != null) {
                    actor.keyCurrentColor();
                    return true;
                }
                if (keycode == Input.Keys.T && actor != null) {
                    actor.toggleTintBlack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        Gdx.gl.glClearColor(0.055f, 0.060f, 0.066f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);
    }

    @Override
    public void dispose() {
        if (pixel != null) pixel.dispose();
        if (font != null) font.dispose();
        super.dispose();
    }

    private static final class SlotColorActor extends Actor {
        private static final int TARGET_LIGHT = 0;
        private static final int TARGET_DARK = 1;

        private final BitmapFont font;
        private final Texture pixel;
        private final SlotModel slot = new SlotModel();
        private final List<SlotColorKey> keys = new ArrayList<SlotColorKey>();
        private final SlotColorChooserPanel chooser = new SlotColorChooserPanel(new SlotColorChooserPanel.Listener() {
            @Override
            public void applyColor(int target, Color color) {
                if (target == TARGET_DARK) {
                    slot.dark.set(color.r, color.g, color.b, 1f);
                    slot.tintBlack = true;
                } else {
                    slot.light.set(color);
                }
            }
        });

        private final Rectangle lightSwatch = new Rectangle();
        private final Rectangle darkSwatch = new Rectangle();
        private final Rectangle tintToggle = new Rectangle();
        private final Rectangle keyButton = new Rectangle();
        private final Rectangle blendBox = new Rectangle();

        private float currentTime = 0f;

        SlotColorActor(BitmapFont font, Texture pixel) {
            this.font = font;
            this.pixel = pixel;
            setTouchable(com.badlogic.gdx.scenes.scene2d.Touchable.enabled);
            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return SlotColorActor.this.touchDown(x, y);
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    SlotColorActor.this.touchDragged(x, y);
                }

                @Override
                public boolean keyDown(InputEvent event, int keycode) {
                    return SlotColorActor.this.keyDown(keycode);
                }
            });
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            layout();
            drawRect(batch, 0, 0, getWidth(), getHeight(), 0.055f, 0.060f, 0.066f, 1f);
            drawPreview(batch);
            drawProperties(batch);
            drawTimeline(batch);
            if (chooser.isOpen()) chooser.draw(batch, font, pixel);
        }

        void toggleTintBlack() {
            slot.tintBlack = !slot.tintBlack;
        }

        void openLightColorPanel() {
            chooser.open("设置插槽颜色", TARGET_LIGHT, slot.light, true);
        }

        void keyCurrentColor() {
            SlotColorKey key = new SlotColorKey();
            key.time = currentTime;
            key.light.set(slot.light);
            key.dark.set(slot.tintBlack ? slot.dark : Color.BLACK);
            key.tintBlack = slot.tintBlack;
            keys.add(key);
            currentTime += 0.25f;
            if (currentTime > 2f) currentTime = 0f;
        }

        private boolean touchDown(float x, float y) {
            if (chooser.isOpen()) {
                return chooser.touchDown(x, y);
            }
            if (lightSwatch.contains(x, y)) {
                chooser.open("Set slot color", TARGET_LIGHT, slot.light, true);
                return true;
            }
            if (darkSwatch.contains(x, y)) {
                if (!slot.tintBlack) slot.tintBlack = true;
                chooser.open("Set slot dark color", TARGET_DARK, slot.dark, false);
                return true;
            }
            if (tintToggle.contains(x, y)) {
                toggleTintBlack();
                return true;
            }
            if (keyButton.contains(x, y)) {
                keyCurrentColor();
                return true;
            }
            if (blendBox.contains(x, y)) {
                slot.blendMode = (slot.blendMode + 1) % SlotModel.BLEND_MODES.length;
                return true;
            }
            return true;
        }

        private void touchDragged(float x, float y) {
            if (chooser.isOpen()) chooser.touchDragged(x, y);
        }

        private boolean keyDown(int keycode) {
            if (chooser.isOpen() && chooser.keyDown(keycode)) return true;
            if (keycode == Input.Keys.SPACE) {
                keyCurrentColor();
                return true;
            }
            if (keycode == Input.Keys.T) {
                toggleTintBlack();
                return true;
            }
            return false;
        }

        private void layout() {
            float panelX = 48f;
            float panelY = getHeight() - 455f;
            lightSwatch.set(panelX + 170f, panelY + 228f, 96f, 40f);
            darkSwatch.set(panelX + 288f, panelY + 228f, 96f, 40f);
            tintToggle.set(panelX + 170f, panelY + 170f, 30f, 30f);
            keyButton.set(panelX + 410f, panelY + 228f, 50f, 40f);
            blendBox.set(panelX + 170f, panelY + 104f, 214f, 42f);
            chooser.layout(getWidth(), getHeight());
        }

        private void drawPreview(Batch batch) {
            float cx = getWidth() * 0.66f;
            float cy = getHeight() * 0.55f;
            drawText(batch, "Slot Preview", cx - 170f, cy + 285f, 0.95f, 0.95f, 0.95f, 1f);

            drawRect(batch, cx - 190f, cy - 210f, 380f, 470f, 0.085f, 0.091f, 0.100f, 1f);
            drawRect(batch, cx - 150f, cy - 170f, 300f, 370f, 0.12f, 0.13f, 0.145f, 1f);

            Color shaded = previewColor();
            drawRect(batch, cx - 105f, cy - 105f, 210f, 210f, shaded.r, shaded.g, shaded.b, shaded.a);
            drawRect(batch, cx - 60f, cy - 55f, 120f, 85f, slot.light.r, slot.light.g, slot.light.b, 0.32f);
            drawText(batch, slot.name, cx - 120f, cy - 205f, 0.72f, 0.76f, 0.82f, 1f);
            drawText(batch, slot.tintBlack ? "twoColor: light + dark" : "color: light only",
                    cx - 165f, cy - 242f, 0.62f, 0.66f, 0.72f, 1f);
        }

        private Color previewColor() {
            Color c = new Color(slot.light);
            if (slot.tintBlack) {
                c.r = MathUtils.clamp(c.r * 0.82f + slot.dark.r * 0.18f, 0f, 1f);
                c.g = MathUtils.clamp(c.g * 0.82f + slot.dark.g * 0.18f, 0f, 1f);
                c.b = MathUtils.clamp(c.b * 0.82f + slot.dark.b * 0.18f, 0f, 1f);
            }
            return c;
        }

        private void drawProperties(Batch batch) {
            float x = 48f;
            float y = getHeight() - 455f;
            drawRect(batch, x, y, 520f, 360f, 0.078f, 0.085f, 0.094f, 1f);
            drawText(batch, "Slot", x + 22f, y + 325f, 0.92f, 0.94f, 0.97f, 1f);
            drawText(batch, slot.name, x + 170f, y + 325f, 0.78f, 0.82f, 0.88f, 1f);

            drawText(batch, "Color", x + 22f, y + 262f, 0.86f, 0.88f, 0.92f, 1f);
            drawSwatch(batch, lightSwatch, slot.light, "Light");
            drawSwatch(batch, darkSwatch, slot.dark, "Dark");
            drawButton(batch, keyButton, "Key");

            drawText(batch, "Tint black", x + 22f, y + 195f, 0.76f, 0.80f, 0.85f, 1f);
            drawRect(batch, tintToggle.x, tintToggle.y, tintToggle.width, tintToggle.height,
                    slot.tintBlack ? 0.96f : 0.16f, slot.tintBlack ? 0.47f : 0.17f,
                    slot.tintBlack ? 0.12f : 0.19f, 1f);
            if (slot.tintBlack) drawText(batch, "x", tintToggle.x + 9f, tintToggle.y + 24f, 0.08f, 0.08f, 0.08f, 1f);

            drawText(batch, "Blending", x + 22f, y + 133f, 0.76f, 0.80f, 0.85f, 1f);
            drawButton(batch, blendBox, SlotModel.BLEND_MODES[slot.blendMode]);
            drawText(batch, "Click color boxes to open the slot color panel. SPACE creates a color key.",
                    x + 22f, y + 42f, 0.54f, 0.58f, 0.64f, 1f);
        }

        private void drawTimeline(Batch batch) {
            float x = 48f;
            float y = 85f;
            drawRect(batch, x, y, getWidth() - 96f, 150f, 0.078f, 0.085f, 0.094f, 1f);
            drawText(batch, "Slot color keys", x + 22f, y + 116f, 0.88f, 0.90f, 0.94f, 1f);
            if (keys.isEmpty()) {
                drawText(batch, "No keys yet", x + 22f, y + 68f, 0.55f, 0.59f, 0.66f, 1f);
                return;
            }
            float keyX = x + 190f;
            for (int i = 0; i < keys.size(); i++) {
                SlotColorKey key = keys.get(i);
                float px = keyX + i * 70f;
                drawRect(batch, px, y + 48f, 46f, 46f, key.light.r, key.light.g, key.light.b, 1f);
                if (key.tintBlack) {
                    drawRect(batch, px + 25f, y + 48f, 21f, 21f, key.dark.r, key.dark.g, key.dark.b, 1f);
                }
                drawText(batch, String.format("%.2f", key.time), px - 2f, y + 35f, 0.55f, 0.59f, 0.66f, 1f);
            }
        }

        private void drawSwatch(Batch batch, Rectangle r, Color color, String label) {
            drawRect(batch, r.x - 2f, r.y - 2f, r.width + 4f, r.height + 4f, 0.22f, 0.24f, 0.27f, 1f);
            drawRect(batch, r.x, r.y, r.width, r.height, color.r, color.g, color.b, color.a);
            drawText(batch, label, r.x + 4f, r.y - 11f, 0.62f, 0.66f, 0.72f, 1f);
        }

        private void drawButton(Batch batch, Rectangle r, String text) {
            drawRect(batch, r.x, r.y, r.width, r.height, 0.13f, 0.14f, 0.16f, 1f);
            drawText(batch, text, r.x + 12f, r.y + 27f, 0.82f, 0.84f, 0.88f, 1f);
        }

        private void drawText(Batch batch, String text, float x, float y, float r, float g, float b, float a) {
            font.setColor(r, g, b, a);
            font.draw(batch, text, x, y);
        }

        private void drawRect(Batch batch, float x, float y, float w, float h, float r, float g, float b, float a) {
            Color old = batch.getColor();
            float oldR = old.r;
            float oldG = old.g;
            float oldB = old.b;
            float oldA = old.a;
            batch.setColor(r, g, b, a);
            batch.draw(pixel, x, y, w, h);
            batch.setColor(oldR, oldG, oldB, oldA);
        }
    }

    private static final class ColorChooser {
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
        private final float[] values = new float[7];
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

        ColorChooser() {
            for (int i = 0; i < valueBoxes.length; i++) valueBoxes[i] = new Rectangle();
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

            float labelX = previewBox.x;
            float boxX = labelX + 58f;
            float y = previewBox.y - 48f;
            for (int i = 0; i < valueBoxes.length; i++) {
                valueBoxes[i].set(boxX, y - i * 44f, 70f, 32f);
            }
            hexBox.set(labelX, panel.y + 95f, 128f, 34f);
            okButton.set(panel.x + panel.width * 0.5f - 95f, panel.y + 28f, 86f, 38f);
            cancelButton.set(panel.x + panel.width * 0.5f + 15f, panel.y + 28f, 86f, 38f);
        }

        void open(String title, int target, Color color, boolean alpha) {
            this.open = true;
            this.title = target == SlotColorActor.TARGET_DARK ? "设置插槽暗色" : "设置插槽颜色";
            this.target = target;
            this.alpha = alpha;
            this.editing.set(color);
            this.original.set(color);
            syncValuesFromColor();
        }

        boolean touchDown(float x, float y, SlotModel slot) {
            if (okButton.contains(x, y)) {
                apply(slot);
                open = false;
                return true;
            }
            if (cancelButton.contains(x, y)) {
                editing.set(original);
                apply(slot);
                open = false;
                return true;
            }
            if (colorSquare.contains(x, y)) {
                activeArea = 1;
                setColorSquareValue(x, y);
                apply(slot);
                return true;
            }
            if (hueBar.contains(x, y)) {
                activeArea = 2;
                setHueValue(y);
                apply(slot);
                return true;
            }
            if (alpha && alphaBar.contains(x, y)) {
                activeArea = 3;
                setAlphaValue(y);
                apply(slot);
                return true;
            }
            return true;
        }

        void touchDragged(float x, float y, SlotModel slot) {
            if (activeArea == 1) {
                setColorSquareValue(x, y);
            } else if (activeArea == 2) {
                setHueValue(y);
            } else if (activeArea == 3) {
                setAlphaValue(y);
            } else {
                return;
            }
            apply(slot);
        }

        boolean keyDown(int keycode, SlotModel slot) {
            if (keycode == Input.Keys.ENTER) {
                apply(slot);
                open = false;
                return true;
            }
            if (keycode == Input.Keys.ESCAPE) {
                editing.set(original);
                apply(slot);
                open = false;
                return true;
            }
            return false;
        }

        void draw(Batch batch, BitmapFont font, Texture pixel) {
            drawRect(batch, pixel, 0, 0, screenWidth, screenHeight, 0f, 0f, 0f, 0.38f);
            drawRect(batch, pixel, panel.x, panel.y, panel.width, panel.height, 0.10f, 0.11f, 0.12f, 1f);
            drawRect(batch, pixel, titleBar.x, titleBar.y, titleBar.width, titleBar.height, 0.18f, 0.19f, 0.20f, 1f);
            drawRect(batch, pixel, titleBar.x + 1f, titleBar.y + 1f, 180f, titleBar.height - 2f, 0.05f, 0.35f, 0.56f, 1f);
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

        private void apply(SlotModel slot) {
            if (target == SlotColorActor.TARGET_DARK) {
                slot.dark.set(editing.r, editing.g, editing.b, 1f);
                slot.tintBlack = true;
            } else {
                slot.light.set(editing);
            }
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
            syncRgbValues();
        }

        private void applyHsv() {
            Color c = hsvToRgb(hue, saturation, brightness);
            editing.r = c.r;
            editing.g = c.g;
            editing.b = c.b;
            syncRgbValues();
        }

        private void syncValuesFromColor() {
            syncHsvFromRgb();
            syncRgbValues();
            values[6] = editing.a;
        }

        private void syncRgbValues() {
            values[3] = editing.r;
            values[4] = editing.g;
            values[5] = editing.b;
            values[6] = editing.a;
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
            values[0] = hue;
            values[1] = saturation;
            values[2] = brightness;
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
    }

    private static final class SlotModel {
        static final String[] BLEND_MODES = {"Normal", "Additive", "Multiply", "Screen"};

        final String name = "head-slot";
        final Color light = new Color(Color.WHITE);
        final Color dark = new Color(Color.BLACK);
        boolean tintBlack;
        int blendMode;
    }

    private static final class SlotColorKey {
        float time;
        final Color light = new Color();
        final Color dark = new Color();
        boolean tintBlack;
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
