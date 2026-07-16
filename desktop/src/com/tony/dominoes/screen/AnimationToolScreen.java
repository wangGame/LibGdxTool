package com.tony.dominoes.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.screen.BaseScreen;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnimationToolScreen extends BaseScreen {
    private static final float TIMELINE_X = 80.0f;
    private static final float TIMELINE_Y = 76.0f;
    private static final float TIMELINE_HEIGHT = 34.0f;
    private static final float PALETTE_WIDTH = 214.0f;
    private static final float DURATION = 3.0f;

    private ShapeRenderer shapes;
    private BitmapFont font;
    private final Vector2 mouseWorld = new Vector2();
    private final Vector2 temp = new Vector2();
    private final List<Bone2D> bones = new ArrayList<Bone2D>();
    private final List<Keyframe> keyframes = new ArrayList<Keyframe>();
    private int selectedBone = 1;
    private float currentTime = 0.0f;
    private boolean playing;
    private boolean draggingBone;
    private boolean draggingTimeline;
    private boolean mouseWasDown;
    private Keyframe clipboard;
    private String message = "Ready";
    private final Color currentColor = new Color(0.95f, 0.75f, 0.30f, 1.0f);
    private final Color[] swatches = new Color[] {
            Color.valueOf("f6c85f"), Color.valueOf("7fc97f"), Color.valueOf("4fb0c6"),
            Color.valueOf("c77cff"), Color.valueOf("f26d6d"), Color.valueOf("ffffff"),
            Color.valueOf("2d3440"), Color.valueOf("ff9f40"), Color.valueOf("6a8dff"),
            Color.valueOf("5fd4a8"), Color.valueOf("c9ced6"), Color.valueOf("111820")
    };
    private final Color[] customSwatches = new Color[6];
    private int nextCustomSwatch;
    private boolean colorDialogOpen;
    private final Color editingColor = new Color();
    private float pickerHue;
    private float pickerSaturation;
    private float pickerValue;
    private float pickerAlpha;

    public AnimationToolScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        shapes = new ShapeRenderer();
        font = Asset.getAsset().loadBitFont("Ma-B_46.fnt");
        font.setColor(Color.WHITE);
        buildSkeleton();
        addKeyframe(0.0f);
        bones.get(1).localRotation = 28.0f;
        bones.get(2).localRotation = -42.0f;
        addKeyframe(1.0f);
        bones.get(1).localRotation = -32.0f;
        bones.get(2).localRotation = 36.0f;
        addKeyframe(2.0f);
        bones.get(1).localRotation = 0.0f;
        bones.get(2).localRotation = 0.0f;
        addKeyframe(3.0f);
        applyAnimation(0.0f);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        updateMouse();
        handleInput(delta);
        if (playing && !draggingBone && !draggingTimeline) {
            currentTime += delta;
            if (currentTime > DURATION) {
                currentTime -= DURATION;
            }
            applyAnimation(currentTime);
        }
        updateWorld();

        Gdx.gl.glClearColor(0.045f, 0.049f, 0.055f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapes.setProjectionMatrix(stage.getCamera().combined);
        game.getBatch().setProjectionMatrix(stage.getCamera().combined);

        drawBackground();
        drawSkeleton();
        drawTimeline();
        drawPalette();
        if (colorDialogOpen) {
            drawColorDialog();
        }
        drawText();
    }

    private void buildSkeleton() {
        bones.clear();
        float cx = stage.getViewport().getWorldWidth() * 0.5f;
        float cy = stage.getViewport().getWorldHeight() * 0.47f;
        Bone2D root = new Bone2D("root", null, 125.0f);
        root.localX = cx - 140.0f;
        root.localY = cy - 80.0f;
        root.localRotation = 12.0f;
        Bone2D upper = new Bone2D("upper", root, 150.0f);
        upper.localX = root.length;
        Bone2D lower = new Bone2D("lower", upper, 130.0f);
        lower.localX = upper.length;
        bones.add(root);
        bones.add(upper);
        bones.add(lower);
        updateWorld();
    }

    private void updateMouse() {
        mouseWorld.set(Gdx.input.getX(), Gdx.input.getY());
        stage.getViewport().unproject(mouseWorld);
    }

    private void handleInput(float delta) {
        boolean mouseDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean mouseJustPressed = mouseDown && !mouseWasDown;
        mouseWasDown = mouseDown;
        if (colorDialogOpen) {
            if (mouseDown || Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                handleColorDialogInput(mouseWorld.x, mouseWorld.y, mouseDown);
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            playing = !playing;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            selectedBone = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            selectedBone = 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            selectedBone = 2;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            addKeyframe(currentTime);
            message = "Keyframe saved at " + format(currentTime);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X) || Gdx.input.isKeyJustPressed(Input.Keys.FORWARD_DEL)) {
            deleteNearestKeyframe();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            clipboard = captureKeyframe(currentTime);
            message = "Copied current pose";
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.V) && clipboard != null) {
            pasteKeyframe();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            saveAnimation();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            loadAnimation();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            resetPose();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            currentTime = Math.max(0.0f, currentTime - 0.05f);
            applyAnimation(currentTime);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            currentTime = Math.min(DURATION, currentTime + 0.05f);
            applyAnimation(currentTime);
        }
        if (!playing) {
            float speed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 90.0f : 35.0f;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                bones.get(selectedBone).localRotation += speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                bones.get(selectedBone).localRotation -= speed * delta;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                bones.get(selectedBone).localScale = Math.max(0.25f, bones.get(selectedBone).localScale - delta);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                bones.get(selectedBone).localScale = Math.min(2.0f, bones.get(selectedBone).localScale + delta);
            }
        }

        float timelineWidth = timelineWidth();
        boolean overTimeline = mouseWorld.x >= TIMELINE_X && mouseWorld.x <= TIMELINE_X + timelineWidth
                && mouseWorld.y >= TIMELINE_Y - 18.0f && mouseWorld.y <= TIMELINE_Y + TIMELINE_HEIGHT + 18.0f;
        boolean overPalette = isOverPalette(mouseWorld.x, mouseWorld.y);
        if (mouseJustPressed && overPalette) {
            handlePaletteClick(mouseWorld.x, mouseWorld.y);
        } else if (mouseJustPressed && overTimeline) {
            draggingTimeline = true;
        } else if (mouseJustPressed && !playing) {
            selectedBone = nearestBone(mouseWorld);
            draggingBone = true;
        }
        if (!mouseDown) {
            draggingTimeline = false;
            draggingBone = false;
        }
        if (draggingTimeline) {
            currentTime = MathUtils.clamp((mouseWorld.x - TIMELINE_X) / timelineWidth * DURATION, 0.0f, DURATION);
            applyAnimation(currentTime);
        }
        if (draggingBone && !playing) {
            if (selectedBone == 0) {
                bones.get(0).localX = mouseWorld.x;
                bones.get(0).localY = mouseWorld.y;
            } else {
                rotateSelectedBoneTo(mouseWorld);
            }
        }
    }

    private void rotateSelectedBoneTo(Vector2 point) {
        Bone2D bone = bones.get(selectedBone);
        Bone2D parent = bone.parent;
        float parentRotation = parent == null ? 0.0f : parent.worldRotation;
        float worldAngle = MathUtils.atan2(point.y - bone.worldY, point.x - bone.worldX) * MathUtils.radiansToDegrees;
        bone.localRotation = normalizeAngle(worldAngle - parentRotation);
    }

    private int nearestBone(Vector2 point) {
        int nearest = selectedBone;
        float best = Float.MAX_VALUE;
        for (int i = 0; i < bones.size(); i++) {
            Bone2D bone = bones.get(i);
            Vector2 tip = bone.getTip(temp);
            float distance = distanceToSegment(point.x, point.y, bone.worldX, bone.worldY, tip.x, tip.y);
            if (distance < best) {
                best = distance;
                nearest = i;
            }
        }
        return nearest;
    }

    private void addKeyframe(float time) {
        float snapped = MathUtils.clamp(MathUtils.round(time * 20.0f) / 20.0f, 0.0f, DURATION);
        for (int i = 0; i < keyframes.size(); i++) {
            if (Math.abs(keyframes.get(i).time - snapped) < 0.001f) {
                keyframes.set(i, captureKeyframe(snapped));
                return;
            }
        }
        keyframes.add(captureKeyframe(snapped));
        Collections.sort(keyframes, new Comparator<Keyframe>() {
            @Override
            public int compare(Keyframe a, Keyframe b) {
                return Float.compare(a.time, b.time);
            }
        });
    }

    private Keyframe captureKeyframe(float time) {
        float[] rotations = new float[bones.size()];
        float[] x = new float[bones.size()];
        float[] y = new float[bones.size()];
        float[] scale = new float[bones.size()];
        float[] color = new float[bones.size() * 4];
        for (int i = 0; i < bones.size(); i++) {
            rotations[i] = bones.get(i).localRotation;
            x[i] = bones.get(i).localX;
            y[i] = bones.get(i).localY;
            scale[i] = bones.get(i).localScale;
            color[i * 4] = bones.get(i).color.r;
            color[i * 4 + 1] = bones.get(i).color.g;
            color[i * 4 + 2] = bones.get(i).color.b;
            color[i * 4 + 3] = bones.get(i).color.a;
        }
        return new Keyframe(time, rotations, x, y, scale, color);
    }

    private void deleteNearestKeyframe() {
        if (keyframes.size() <= 1) {
            return;
        }
        int nearest = -1;
        float best = 0.16f;
        for (int i = 0; i < keyframes.size(); i++) {
            float distance = Math.abs(keyframes.get(i).time - currentTime);
            if (distance < best) {
                best = distance;
                nearest = i;
            }
        }
        if (nearest >= 0) {
            keyframes.remove(nearest);
            applyAnimation(currentTime);
            message = "Deleted keyframe";
        }
    }

    private void pasteKeyframe() {
        for (int i = 0; i < bones.size(); i++) {
            bones.get(i).localRotation = clipboard.rotations[i];
            bones.get(i).localX = clipboard.x[i];
            bones.get(i).localY = clipboard.y[i];
            bones.get(i).localScale = clipboard.scale[i];
            bones.get(i).color.set(clipboard.color[i * 4], clipboard.color[i * 4 + 1], clipboard.color[i * 4 + 2], clipboard.color[i * 4 + 3]);
        }
        addKeyframe(currentTime);
        message = "Pasted pose to " + format(currentTime);
    }

    private void applyAnimation(float time) {
        if (keyframes.isEmpty()) {
            return;
        }
        Keyframe first = keyframes.get(0);
        Keyframe last = keyframes.get(keyframes.size() - 1);
        if (time <= first.time) {
            applyKeyframe(first);
            return;
        }
        if (time >= last.time) {
            applyKeyframe(last);
            return;
        }
        Keyframe a = first;
        Keyframe b = last;
        for (int i = 0; i < keyframes.size() - 1; i++) {
            Keyframe left = keyframes.get(i);
            Keyframe right = keyframes.get(i + 1);
            if (time >= left.time && time <= right.time) {
                a = left;
                b = right;
                break;
            }
        }
        float alpha = (time - a.time) / (b.time - a.time);
        alpha = alpha * alpha * (3.0f - 2.0f * alpha);
        for (int i = 0; i < bones.size(); i++) {
            bones.get(i).localRotation = lerpAngle(a.rotations[i], b.rotations[i], alpha);
            bones.get(i).localX = MathUtils.lerp(a.x[i], b.x[i], alpha);
            bones.get(i).localY = MathUtils.lerp(a.y[i], b.y[i], alpha);
            bones.get(i).localScale = MathUtils.lerp(a.scale[i], b.scale[i], alpha);
            bones.get(i).color.set(
                    MathUtils.lerp(a.color[i * 4], b.color[i * 4], alpha),
                    MathUtils.lerp(a.color[i * 4 + 1], b.color[i * 4 + 1], alpha),
                    MathUtils.lerp(a.color[i * 4 + 2], b.color[i * 4 + 2], alpha),
                    MathUtils.lerp(a.color[i * 4 + 3], b.color[i * 4 + 3], alpha)
            );
        }
    }

    private void applyKeyframe(Keyframe keyframe) {
        for (int i = 0; i < bones.size(); i++) {
            bones.get(i).localRotation = keyframe.rotations[i];
            bones.get(i).localX = keyframe.x[i];
            bones.get(i).localY = keyframe.y[i];
            bones.get(i).localScale = keyframe.scale[i];
            bones.get(i).color.set(keyframe.color[i * 4], keyframe.color[i * 4 + 1], keyframe.color[i * 4 + 2], keyframe.color[i * 4 + 3]);
        }
        currentColor.set(bones.get(selectedBone).color);
    }

    private void resetPose() {
        playing = false;
        for (int i = 0; i < bones.size(); i++) {
            bones.get(i).localRotation = i == 0 ? 12.0f : 0.0f;
            bones.get(i).localScale = 1.0f;
        }
        buildSkeleton();
        message = "Pose reset";
    }

    private void saveAnimation() {
        JSONObject root = new JSONObject();
        root.put("duration", DURATION);
        JSONArray boneNames = new JSONArray();
        for (int i = 0; i < bones.size(); i++) {
            boneNames.put(bones.get(i).name);
        }
        root.put("bones", boneNames);
        JSONArray frames = new JSONArray();
        for (int i = 0; i < keyframes.size(); i++) {
            Keyframe keyframe = keyframes.get(i);
            JSONObject frame = new JSONObject();
            frame.put("time", keyframe.time);
            frame.put("rotation", toJson(keyframe.rotations));
            frame.put("x", toJson(keyframe.x));
            frame.put("y", toJson(keyframe.y));
            frame.put("scale", toJson(keyframe.scale));
            frame.put("color", toJson(keyframe.color));
            frames.put(frame);
        }
        root.put("keyframes", frames);
        FileHandle file = Gdx.files.local("animation-tool.json");
        file.writeString(root.toString(2), false, "UTF-8");
        message = "Saved " + keyframes.size() + " keyframes to " + file.path();
    }

    private void loadAnimation() {
        FileHandle file = Gdx.files.local("animation-tool.json");
        if (!file.exists()) {
            message = "No saved animation-tool.json";
            return;
        }
        JSONObject root = new JSONObject(file.readString("UTF-8"));
        JSONArray frames = root.getJSONArray("keyframes");
        keyframes.clear();
        for (int i = 0; i < frames.length(); i++) {
            JSONObject frame = frames.getJSONObject(i);
            keyframes.add(new Keyframe(
                    (float)frame.getDouble("time"),
                    fromJson(frame.getJSONArray("rotation")),
                    fromJson(frame.getJSONArray("x")),
                    fromJson(frame.getJSONArray("y")),
                    fromJson(frame.getJSONArray("scale")),
                    frame.has("color") ? fromJson(frame.getJSONArray("color")) : defaultColorFrame()
            ));
        }
        Collections.sort(keyframes, new Comparator<Keyframe>() {
            @Override
            public int compare(Keyframe a, Keyframe b) {
                return Float.compare(a.time, b.time);
            }
        });
        currentTime = 0.0f;
        applyAnimation(currentTime);
        message = "Loaded " + keyframes.size() + " keyframes";
    }

    private void updateWorld() {
        for (int i = 0; i < bones.size(); i++) {
            bones.get(i).updateWorld();
        }
    }

    private void drawBackground() {
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.045f, 0.049f, 0.055f, 1.0f);
        shapes.rect(0.0f, 0.0f, width, height);
        shapes.setColor(0.090f, 0.098f, 0.106f, 1.0f);
        shapes.rect(58.0f, 138.0f, width - PALETTE_WIDTH - 146.0f, height - 230.0f);
        shapes.end();
    }

    private void drawSkeleton() {
        for (int i = 0; i < bones.size(); i++) {
            drawBone(bones.get(i), i == selectedBone);
        }
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.WHITE);
        for (int i = 0; i < bones.size(); i++) {
            shapes.circle(bones.get(i).worldX, bones.get(i).worldY, 6.0f, 16);
        }
        Vector2 tip = bones.get(bones.size() - 1).getTip(temp);
        shapes.circle(tip.x, tip.y, 6.0f, 16);
        shapes.end();
    }

    private void drawBone(Bone2D bone, boolean selected) {
        Vector2 tip = bone.getTip(temp);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(bone.color);
        shapes.rectLine(bone.worldX, bone.worldY, tip.x, tip.y, selected ? 17.0f : 13.0f);
        if (selected) {
            shapes.setColor(Color.WHITE);
            shapes.circle(bone.worldX, bone.worldY, 11.0f, 20);
        }
        shapes.end();
    }

    private void drawPalette() {
        float x = paletteX();
        float y = stage.getViewport().getWorldHeight() - 118.0f;
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.075f, 0.083f, 0.092f, 1.0f);
        shapes.rect(x, 138.0f, PALETTE_WIDTH, stage.getViewport().getWorldHeight() - 230.0f);
        shapes.setColor(currentColor);
        shapes.rect(x + 18.0f, y - 28.0f, 68.0f, 42.0f);
        shapes.setColor(0.18f, 0.20f, 0.22f, 1.0f);
        shapes.rect(x + 94.0f, y - 28.0f, 92.0f, 42.0f);

        float swatchY = y - 82.0f;
        for (int i = 0; i < swatches.length; i++) {
            drawSwatch(x + 18.0f + (i % 4) * 44.0f, swatchY - (i / 4) * 38.0f, swatches[i]);
        }

        float sliderY = swatchY - 132.0f;
        drawSlider(x + 18.0f, sliderY, currentColor.r, Color.RED);
        drawSlider(x + 18.0f, sliderY - 30.0f, currentColor.g, Color.GREEN);
        drawSlider(x + 18.0f, sliderY - 60.0f, currentColor.b, Color.BLUE);
        drawSlider(x + 18.0f, sliderY - 90.0f, currentColor.a, Color.LIGHT_GRAY);

        float customY = sliderY - 142.0f;
        for (int i = 0; i < customSwatches.length; i++) {
            Color color = customSwatches[i] == null ? Color.valueOf("222832") : customSwatches[i];
            drawSwatch(x + 18.0f + i * 30.0f, customY, color);
        }
        shapes.setColor(0.30f, 0.33f, 0.36f, 1.0f);
        shapes.rect(x + 18.0f, customY - 45.0f, 168.0f, 28.0f);
        shapes.end();
    }

    private void drawSwatch(float x, float y, Color color) {
        shapes.setColor(color);
        shapes.rect(x, y, 30.0f, 28.0f);
        shapes.setColor(0.02f, 0.025f, 0.03f, 1.0f);
        shapes.rect(x, y, 30.0f, 2.0f);
        shapes.rect(x, y + 26.0f, 30.0f, 2.0f);
        shapes.rect(x, y, 2.0f, 28.0f);
        shapes.rect(x + 28.0f, y, 2.0f, 28.0f);
    }

    private void drawSlider(float x, float y, float value, Color color) {
        shapes.setColor(0.18f, 0.20f, 0.22f, 1.0f);
        shapes.rect(x, y, 168.0f, 14.0f);
        shapes.setColor(color);
        shapes.rect(x, y, 168.0f * value, 14.0f);
        shapes.setColor(Color.WHITE);
        shapes.rect(x + 168.0f * value - 2.0f, y - 3.0f, 4.0f, 20.0f);
    }

    private void drawColorDialog() {
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        float x = (width - 636.0f) * 0.5f;
        float y = (height - 546.0f) * 0.5f;
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.0f, 0.0f, 0.0f, 0.48f);
        shapes.rect(0.0f, 0.0f, width, height);
        shapes.setColor(0.18f, 0.20f, 0.21f, 1.0f);
        shapes.rect(x, y, 636.0f, 546.0f);
        shapes.setColor(0.08f, 0.35f, 0.52f, 1.0f);
        shapes.rect(x, y + 512.0f, 636.0f, 34.0f);
        shapes.setColor(0.12f, 0.13f, 0.14f, 1.0f);
        shapes.rect(x + 1.0f, y + 44.0f, 634.0f, 467.0f);
        drawAlphaStrip(x + 14.0f, y + 56.0f);
        drawSaturationValueSquare(x + 62.0f, y + 56.0f);
        drawHueStrip(x + 520.0f, y + 56.0f);
        shapes.setColor(editingColor);
        shapes.rect(x + 556.0f, y + 414.0f, 82.0f, 80.0f);
        shapes.setColor(0.25f, 0.27f, 0.29f, 1.0f);
        shapes.rect(x + 254.0f, y + 12.0f, 62.0f, 26.0f);
        shapes.rect(x + 324.0f, y + 12.0f, 62.0f, 26.0f);
        shapes.end();

        game.getBatch().begin();
        font.draw(game.getBatch(), "Set slot color", x + 12.0f, y + 535.0f);
        font.draw(game.getBatch(), "OK", x + 274.0f, y + 33.0f);
        font.draw(game.getBatch(), "Cancel", x + 336.0f, y + 33.0f);
        font.draw(game.getBatch(), "H: " + MathUtils.round(pickerHue), x + 568.0f, y + 400.0f);
        font.draw(game.getBatch(), "S: " + MathUtils.round(pickerSaturation * 100.0f), x + 568.0f, y + 370.0f);
        font.draw(game.getBatch(), "B: " + MathUtils.round(pickerValue * 100.0f), x + 568.0f, y + 340.0f);
        font.draw(game.getBatch(), "R: " + MathUtils.round(editingColor.r * 255.0f), x + 568.0f, y + 302.0f);
        font.draw(game.getBatch(), "G: " + MathUtils.round(editingColor.g * 255.0f), x + 568.0f, y + 272.0f);
        font.draw(game.getBatch(), "B: " + MathUtils.round(editingColor.b * 255.0f), x + 568.0f, y + 242.0f);
        font.draw(game.getBatch(), "A: " + MathUtils.round(editingColor.a * 255.0f), x + 568.0f, y + 204.0f);
        font.draw(game.getBatch(), toHex(editingColor), x + 556.0f, y + 168.0f);
        game.getBatch().end();
    }

    private void drawSaturationValueSquare(float x, float y) {
        int columns = 36;
        int rows = 36;
        float cellW = 450.0f / columns;
        float cellH = 450.0f / rows;
        Color color = new Color();
        for (int row = 0; row < rows; row++) {
            float value = row / (float)(rows - 1);
            for (int col = 0; col < columns; col++) {
                float saturation = col / (float)(columns - 1);
                hsvToColor(pickerHue, saturation, value, pickerAlpha, color);
                shapes.setColor(color);
                shapes.rect(x + col * cellW, y + row * cellH, cellW + 1.0f, cellH + 1.0f);
            }
        }
        shapes.setColor(Color.WHITE);
        shapes.circle(x + pickerSaturation * 450.0f, y + pickerValue * 450.0f, 4.0f, 12);
    }

    private void drawHueStrip(float x, float y) {
        Color color = new Color();
        for (int i = 0; i < 90; i++) {
            float hue = i / 89.0f * 360.0f;
            hsvToColor(hue, 1.0f, 1.0f, 1.0f, color);
            shapes.setColor(color);
            shapes.rect(x, y + i * 5.0f, 28.0f, 6.0f);
        }
        shapes.setColor(Color.WHITE);
        shapes.circle(x + 14.0f, y + pickerHue / 360.0f * 450.0f, 4.0f, 12);
    }

    private void drawAlphaStrip(float x, float y) {
        for (int i = 0; i < 18; i++) {
            for (int j = 0; j < 45; j++) {
                boolean light = ((i + j) & 1) == 0;
                shapes.setColor(light ? 0.82f : 0.64f, light ? 0.82f : 0.64f, light ? 0.82f : 0.64f, 1.0f);
                shapes.rect(x + i * 1.5f, y + j * 10.0f, 1.5f, 10.0f);
            }
        }
        shapes.setColor(1.0f, 1.0f, 1.0f, pickerAlpha);
        shapes.rect(x, y, 28.0f, 450.0f);
        shapes.setColor(Color.WHITE);
        shapes.circle(x + 14.0f, y + pickerAlpha * 450.0f, 4.0f, 12);
    }

    private void drawTimeline() {
        float width = timelineWidth();
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.075f, 0.083f, 0.092f, 1.0f);
        shapes.rect(TIMELINE_X - 18.0f, TIMELINE_Y - 22.0f, width + 36.0f, TIMELINE_HEIGHT + 44.0f);
        shapes.setColor(0.18f, 0.20f, 0.22f, 1.0f);
        shapes.rect(TIMELINE_X, TIMELINE_Y, width, TIMELINE_HEIGHT);
        shapes.setColor(0.38f, 0.66f, 0.78f, 1.0f);
        shapes.rect(TIMELINE_X, TIMELINE_Y, width * currentTime / DURATION, TIMELINE_HEIGHT);
        shapes.setColor(0.95f, 0.75f, 0.30f, 1.0f);
        for (int i = 0; i < keyframes.size(); i++) {
            float x = TIMELINE_X + width * keyframes.get(i).time / DURATION;
            shapes.rect(x - 4.0f, TIMELINE_Y - 7.0f, 8.0f, TIMELINE_HEIGHT + 14.0f);
        }
        shapes.setColor(Color.WHITE);
        float playX = TIMELINE_X + width * currentTime / DURATION;
        shapes.rect(playX - 2.0f, TIMELINE_Y - 14.0f, 4.0f, TIMELINE_HEIGHT + 28.0f);
        shapes.end();
    }

    private void drawText() {
        game.getBatch().begin();
        font.draw(game.getBatch(), "Simple Animation Tool", 40.0f, stage.getViewport().getWorldHeight() - 34.0f);
        font.draw(game.getBatch(), "Selected: " + bones.get(selectedBone).name + "   Time: " + format(currentTime) + " / " + format(DURATION) + "   " + (playing ? "Playing" : "Paused") + "   Keys: " + keyframes.size(), 40.0f, stage.getViewport().getWorldHeight() - 76.0f);
        font.draw(game.getBatch(), "Drag bone/root   K key   C copy   V paste   X/Delete remove   S save   O load   Space play   Q/E scale   A/D rotate   R reset", 40.0f, 42.0f);
        float px = paletteX();
        font.draw(game.getBatch(), "Palette", px + 18.0f, stage.getViewport().getWorldHeight() - 34.0f);
        font.draw(game.getBatch(), "Current", px + 98.0f, stage.getViewport().getWorldHeight() - 132.0f);
        font.draw(game.getBatch(), "R", px + 190.0f, stage.getViewport().getWorldHeight() - 334.0f);
        font.draw(game.getBatch(), "G", px + 190.0f, stage.getViewport().getWorldHeight() - 364.0f);
        font.draw(game.getBatch(), "B", px + 190.0f, stage.getViewport().getWorldHeight() - 394.0f);
        font.draw(game.getBatch(), "A", px + 190.0f, stage.getViewport().getWorldHeight() - 424.0f);
        font.draw(game.getBatch(), "Save custom", px + 36.0f, stage.getViewport().getWorldHeight() - 513.0f);
        font.draw(game.getBatch(), message, 40.0f, 18.0f);
        game.getBatch().end();
    }

    private boolean isOverPalette(float x, float y) {
        return x >= paletteX() && x <= paletteX() + PALETTE_WIDTH
                && y >= 138.0f && y <= stage.getViewport().getWorldHeight() - 92.0f;
    }

    private void handlePaletteClick(float mouseX, float mouseY) {
        float x = paletteX();
        float y = stage.getViewport().getWorldHeight() - 118.0f;
        if (inside(mouseX, mouseY, x + 18.0f, y - 28.0f, 168.0f, 42.0f)) {
            openColorDialog();
            return;
        }
        float swatchY = y - 82.0f;
        for (int i = 0; i < swatches.length; i++) {
            float sx = x + 18.0f + (i % 4) * 44.0f;
            float sy = swatchY - (i / 4) * 38.0f;
            if (inside(mouseX, mouseY, sx, sy, 30.0f, 28.0f)) {
                applyColor(swatches[i]);
                return;
            }
        }

        float sliderY = swatchY - 132.0f;
        if (handleSlider(mouseX, mouseY, x + 18.0f, sliderY, 0)) return;
        if (handleSlider(mouseX, mouseY, x + 18.0f, sliderY - 30.0f, 1)) return;
        if (handleSlider(mouseX, mouseY, x + 18.0f, sliderY - 60.0f, 2)) return;
        if (handleSlider(mouseX, mouseY, x + 18.0f, sliderY - 90.0f, 3)) return;

        float customY = sliderY - 142.0f;
        for (int i = 0; i < customSwatches.length; i++) {
            float sx = x + 18.0f + i * 30.0f;
            if (inside(mouseX, mouseY, sx, customY, 30.0f, 28.0f) && customSwatches[i] != null) {
                applyColor(customSwatches[i]);
                return;
            }
        }
        if (inside(mouseX, mouseY, x + 18.0f, customY - 45.0f, 168.0f, 28.0f)) {
            customSwatches[nextCustomSwatch] = new Color(currentColor);
            nextCustomSwatch = (nextCustomSwatch + 1) % customSwatches.length;
            message = "Custom color saved";
        }
    }

    private boolean handleSlider(float mouseX, float mouseY, float x, float y, int channel) {
        if (!inside(mouseX, mouseY, x, y - 4.0f, 168.0f, 22.0f)) {
            return false;
        }
        float value = MathUtils.clamp((mouseX - x) / 168.0f, 0.0f, 1.0f);
        if (channel == 0) currentColor.r = value;
        if (channel == 1) currentColor.g = value;
        if (channel == 2) currentColor.b = value;
        if (channel == 3) currentColor.a = value;
        applyColor(currentColor);
        return true;
    }

    private void applyColor(Color color) {
        currentColor.set(color);
        bones.get(selectedBone).color.set(color);
        message = "Applied color to " + bones.get(selectedBone).name;
    }

    private void openColorDialog() {
        editingColor.set(currentColor);
        colorToHsv(editingColor, tempHsv);
        pickerHue = tempHsv[0];
        pickerSaturation = tempHsv[1];
        pickerValue = tempHsv[2];
        pickerAlpha = editingColor.a;
        colorDialogOpen = true;
    }

    private final float[] tempHsv = new float[3];

    private void handleColorDialogInput(float mouseX, float mouseY, boolean mouseDown) {
        float x = (stage.getViewport().getWorldWidth() - 636.0f) * 0.5f;
        float y = (stage.getViewport().getWorldHeight() - 546.0f) * 0.5f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            colorDialogOpen = false;
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            applyColor(editingColor);
            colorDialogOpen = false;
            return;
        }
        if (!mouseDown) {
            return;
        }
        if (inside(mouseX, mouseY, x + 254.0f, y + 12.0f, 62.0f, 26.0f)) {
            applyColor(editingColor);
            colorDialogOpen = false;
            return;
        }
        if (inside(mouseX, mouseY, x + 324.0f, y + 12.0f, 62.0f, 26.0f)) {
            colorDialogOpen = false;
            return;
        }
        if (inside(mouseX, mouseY, x + 62.0f, y + 56.0f, 450.0f, 450.0f)) {
            pickerSaturation = MathUtils.clamp((mouseX - (x + 62.0f)) / 450.0f, 0.0f, 1.0f);
            pickerValue = MathUtils.clamp((mouseY - (y + 56.0f)) / 450.0f, 0.0f, 1.0f);
            updateEditingColor();
            return;
        }
        if (inside(mouseX, mouseY, x + 520.0f, y + 56.0f, 28.0f, 450.0f)) {
            pickerHue = MathUtils.clamp((mouseY - (y + 56.0f)) / 450.0f, 0.0f, 1.0f) * 360.0f;
            updateEditingColor();
            return;
        }
        if (inside(mouseX, mouseY, x + 14.0f, y + 56.0f, 28.0f, 450.0f)) {
            pickerAlpha = MathUtils.clamp((mouseY - (y + 56.0f)) / 450.0f, 0.0f, 1.0f);
            updateEditingColor();
        }
    }

    private void updateEditingColor() {
        hsvToColor(pickerHue, pickerSaturation, pickerValue, pickerAlpha, editingColor);
    }

    private float paletteX() {
        return stage.getViewport().getWorldWidth() - PALETTE_WIDTH - 58.0f;
    }

    private static boolean inside(float px, float py, float x, float y, float width, float height) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    private float timelineWidth() {
        return stage.getViewport().getWorldWidth() - TIMELINE_X * 2.0f;
    }

    private static String format(float value) {
        return String.valueOf(MathUtils.round(value * 100.0f) / 100.0f);
    }

    private static float distanceToSegment(float px, float py, float ax, float ay, float bx, float by) {
        float dx = bx - ax;
        float dy = by - ay;
        float length2 = dx * dx + dy * dy;
        if (length2 == 0.0f) {
            return Vector2.dst(px, py, ax, ay);
        }
        float t = ((px - ax) * dx + (py - ay) * dy) / length2;
        t = MathUtils.clamp(t, 0.0f, 1.0f);
        return Vector2.dst(px, py, ax + dx * t, ay + dy * t);
    }

    private static float normalizeAngle(float angle) {
        while (angle > 180.0f) {
            angle -= 360.0f;
        }
        while (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    private static float lerpAngle(float from, float to, float alpha) {
        return from + normalizeAngle(to - from) * alpha;
    }

    private static JSONArray toJson(float[] values) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < values.length; i++) {
            array.put(values[i]);
        }
        return array;
    }

    private static float[] fromJson(JSONArray array) {
        float[] values = new float[array.length()];
        for (int i = 0; i < array.length(); i++) {
            values[i] = (float)array.getDouble(i);
        }
        return values;
    }

    private static void hsvToColor(float hue, float saturation, float value, float alpha, Color out) {
        float h = (hue % 360.0f + 360.0f) % 360.0f;
        float c = value * saturation;
        float x = c * (1.0f - Math.abs((h / 60.0f) % 2.0f - 1.0f));
        float m = value - c;
        float r;
        float g;
        float b;
        if (h < 60.0f) {
            r = c; g = x; b = 0.0f;
        } else if (h < 120.0f) {
            r = x; g = c; b = 0.0f;
        } else if (h < 180.0f) {
            r = 0.0f; g = c; b = x;
        } else if (h < 240.0f) {
            r = 0.0f; g = x; b = c;
        } else if (h < 300.0f) {
            r = x; g = 0.0f; b = c;
        } else {
            r = c; g = 0.0f; b = x;
        }
        out.set(r + m, g + m, b + m, alpha);
    }

    private static void colorToHsv(Color color, float[] hsv) {
        float max = Math.max(color.r, Math.max(color.g, color.b));
        float min = Math.min(color.r, Math.min(color.g, color.b));
        float delta = max - min;
        float hue;
        if (delta == 0.0f) {
            hue = 0.0f;
        } else if (max == color.r) {
            hue = 60.0f * (((color.g - color.b) / delta) % 6.0f);
        } else if (max == color.g) {
            hue = 60.0f * (((color.b - color.r) / delta) + 2.0f);
        } else {
            hue = 60.0f * (((color.r - color.g) / delta) + 4.0f);
        }
        if (hue < 0.0f) {
            hue += 360.0f;
        }
        hsv[0] = hue;
        hsv[1] = max == 0.0f ? 0.0f : delta / max;
        hsv[2] = max;
    }

    private static String toHex(Color color) {
        int r = MathUtils.clamp(MathUtils.round(color.r * 255.0f), 0, 255);
        int g = MathUtils.clamp(MathUtils.round(color.g * 255.0f), 0, 255);
        int b = MathUtils.clamp(MathUtils.round(color.b * 255.0f), 0, 255);
        int a = MathUtils.clamp(MathUtils.round(color.a * 255.0f), 0, 255);
        return hex2(r) + hex2(g) + hex2(b) + hex2(a);
    }

    private static String hex2(int value) {
        String hex = Integer.toHexString(value).toUpperCase();
        return hex.length() == 1 ? "0" + hex : hex;
    }

    private static float[] defaultColorFrame() {
        float[] color = new float[12];
        for (int i = 0; i < 3; i++) {
            Color c = defaultBoneColor(i);
            color[i * 4] = c.r;
            color[i * 4 + 1] = c.g;
            color[i * 4 + 2] = c.b;
            color[i * 4 + 3] = c.a;
        }
        return color;
    }

    private static Color defaultBoneColor(int index) {
        if (index == 0) return Color.valueOf("4fb0c6");
        if (index == 1) return Color.valueOf("f6c85f");
        return Color.valueOf("7fc97f");
    }

    private static final class Bone2D {
        final String name;
        final Bone2D parent;
        final float length;
        float localX;
        float localY;
        float localRotation;
        float localScale = 1.0f;
        final Color color;
        float worldX;
        float worldY;
        float worldRotation;
        float worldScale = 1.0f;

        Bone2D(String name, Bone2D parent, float length) {
            this.name = name;
            this.parent = parent;
            this.length = length;
            this.color = defaultBoneColor(parent == null ? 0 : parent.parent == null ? 1 : 2);
        }

        void updateWorld() {
            if (parent == null) {
                worldX = localX;
                worldY = localY;
                worldRotation = localRotation;
                worldScale = localScale;
                return;
            }
            parent.updateWorld();
            float cos = MathUtils.cosDeg(parent.worldRotation);
            float sin = MathUtils.sinDeg(parent.worldRotation);
            worldX = parent.worldX + localX * cos - localY * sin;
            worldY = parent.worldY + localX * sin + localY * cos;
            worldRotation = parent.worldRotation + localRotation;
            worldScale = parent.worldScale * localScale;
        }

        Vector2 getTip(Vector2 out) {
            out.set(length * worldScale, 0.0f).rotate(worldRotation).add(worldX, worldY);
            return out;
        }
    }

    private static final class Keyframe {
        final float time;
        final float[] rotations;
        final float[] x;
        final float[] y;
        final float[] scale;
        final float[] color;

        Keyframe(float time, float[] rotations, float[] x, float[] y, float[] scale, float[] color) {
            this.time = time;
            this.rotations = rotations;
            this.x = x;
            this.y = y;
            this.scale = scale;
            this.color = color;
        }
    }
}
