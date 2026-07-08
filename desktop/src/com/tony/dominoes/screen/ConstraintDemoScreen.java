package com.tony.dominoes.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.screen.BaseScreen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConstraintDemoScreen extends BaseScreen {
    private enum Mode {
        IK,
        TRANSFORM,
        PATH
    }

    private ShapeRenderer shapes;
    private BitmapFont font;
    private Mode mode = Mode.IK;
    private final Vector2 mouseWorld = new Vector2();
    private final Vector2 temp = new Vector2();
    private final ConstraintSkin skin = new ConstraintSkin("demo");

    private Bone2D ikRoot;
    private Bone2D ikChild;
    private final Vector2 ikTarget = new Vector2();
    private IkConstraint2D ikConstraint;

    private Bone2D transformSource;
    private Bone2D transformFollower;
    private TransformConstraint2D transformConstraint;

    private final List<Bone2D> pathBones = new ArrayList<Bone2D>();
    private PathConstraint2D pathConstraint;
    private float pathPosition = 0.0f;
    private boolean positiveBend = true;
    private boolean stretch = true;
    private float mix = 1.0f;

    public ConstraintDemoScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        shapes = new ShapeRenderer();
        font = Asset.getAsset().loadBitFont("Ma-B_46.fnt");
        font.setColor(Color.WHITE);
        buildDemo();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        updateMouse();
        handleInput(delta);
        updateConstraints(delta);

        Gdx.gl.glClearColor(0.045f, 0.050f, 0.055f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        shapes.setProjectionMatrix(stage.getCamera().combined);
        game.getBatch().setProjectionMatrix(stage.getCamera().combined);

        drawBackground();
        if (mode == Mode.IK) {
            drawIkDemo();
        } else if (mode == Mode.TRANSFORM) {
            drawTransformDemo();
        } else {
            drawPathDemo();
        }
        drawText();
    }

    private void buildDemo() {
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        Vector2 center = new Vector2(width * 0.5f, height * 0.46f);

        ikRoot = new Bone2D("parent", null, 150.0f);
        ikRoot.localX = center.x - 120.0f;
        ikRoot.localY = center.y - 30.0f;
        ikRoot.localRotation = 12.0f;
        ikChild = new Bone2D("child", ikRoot, 120.0f);
        ikChild.localX = ikRoot.length;
        ikChild.localRotation = 25.0f;
        ikTarget.set(center.x + 170.0f, center.y + 80.0f);
        ikConstraint = new IkConstraint2D("arm-ik", ikRoot, ikChild, ikTarget);
        skin.add(ikConstraint.name, ikConstraint);

        transformSource = new Bone2D("target-bone", null, 150.0f);
        transformSource.localX = center.x - 180.0f;
        transformSource.localY = center.y - 70.0f;
        transformFollower = new Bone2D("follower-bone", null, 150.0f);
        transformFollower.localX = center.x - 180.0f;
        transformFollower.localY = center.y + 70.0f;
        transformConstraint = new TransformConstraint2D("copy-transform", transformFollower, transformSource);
        skin.add(transformConstraint.name, transformConstraint);

        pathBones.clear();
        Bone2D previous = null;
        for (int i = 0; i < 5; i++) {
            Bone2D bone = new Bone2D("path-bone-" + i, previous, 72.0f);
            if (previous == null) {
                bone.localX = center.x - 220.0f;
                bone.localY = center.y - 40.0f;
            } else {
                bone.localX = previous.length;
            }
            pathBones.add(bone);
            previous = bone;
        }
        pathConstraint = new PathConstraint2D("tail-path", pathBones);
        skin.add(pathConstraint.name, pathConstraint);
    }

    private void updateMouse() {
        mouseWorld.set(Gdx.input.getX(), Gdx.input.getY());
        stage.getViewport().unproject(mouseWorld);
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            mode = Mode.IK;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            mode = Mode.TRANSFORM;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            mode = Mode.PATH;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            positiveBend = !positiveBend;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            stretch = !stretch;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT_BRACKET)) {
            mix = Math.max(0.0f, mix - 0.1f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET)) {
            mix = Math.min(1.0f, mix + 0.1f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            toggleSkinConstraint();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            skin.rename("arm-ik", "arm-ik-renamed");
        }

        if (mode == Mode.IK && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            ikTarget.set(mouseWorld);
        }
        if (mode == Mode.TRANSFORM) {
            transformSource.localRotation += 40.0f * delta;
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                transformSource.localX = mouseWorld.x;
                transformSource.localY = mouseWorld.y;
            }
        }
        if (mode == Mode.PATH) {
            pathPosition += delta * 0.16f;
            if (pathPosition > 1.0f) {
                pathPosition -= 1.0f;
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                pathConstraint.control2.set(mouseWorld);
            }
        }
    }

    private void toggleSkinConstraint() {
        if (skin.contains("copy-transform")) {
            skin.remove("copy-transform");
        } else {
            skin.add("copy-transform", transformConstraint);
        }
    }

    private void updateConstraints(float delta) {
        ikConstraint.mix = mix;
        ikConstraint.positiveBend = positiveBend;
        ikConstraint.compress = stretch;
        ikConstraint.stretch = stretch;
        ikRoot.updateWorld();
        ikChild.updateWorld();
        ikConstraint.apply();

        transformSource.updateWorld();
        transformFollower.updateWorld();
        transformConstraint.rotateMix = mix;
        transformConstraint.translateMix = mix;
        transformConstraint.scaleMix = mix;
        if (skin.contains("copy-transform")) {
            transformConstraint.apply();
        }

        pathConstraint.position = pathPosition;
        pathConstraint.rotateMix = mix;
        pathConstraint.translateMix = mix;
        pathConstraint.chainScale = stretch;
        pathConstraint.apply();
    }

    private void drawBackground() {
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.070f, 0.078f, 0.086f, 1.0f);
        shapes.rect(0.0f, 0.0f, width, height);
        shapes.setColor(0.110f, 0.118f, 0.128f, 1.0f);
        shapes.rect(60.0f, 90.0f, width - 120.0f, height - 170.0f);
        shapes.end();
    }

    private void drawIkDemo() {
        drawTarget(ikTarget, Color.valueOf("f6c85f"));
        drawBone(ikRoot, Color.valueOf("4fb0c6"));
        drawBone(ikChild, Color.valueOf("7fc97f"));
        drawJoint(ikRoot.worldX, ikRoot.worldY);
        drawJoint(ikChild.worldX, ikChild.worldY);
        Vector2 tip = ikChild.getTip(temp);
        drawJoint(tip.x, tip.y);
    }

    private void drawTransformDemo() {
        drawBone(transformSource, Color.valueOf("f6c85f"));
        drawBone(transformFollower, skin.contains("copy-transform") ? Color.valueOf("7fc97f") : Color.valueOf("888888"));
        drawTarget(new Vector2(transformSource.worldX, transformSource.worldY), Color.valueOf("f6c85f"));
    }

    private void drawPathDemo() {
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.valueOf("f6c85f"));
        Vector2 last = new Vector2();
        Vector2 current = new Vector2();
        pathConstraint.pointAt(0.0f, last);
        for (int i = 1; i <= 64; i++) {
            pathConstraint.pointAt(i / 64.0f, current);
            shapes.line(last, current);
            last.set(current);
        }
        shapes.end();
        for (int i = 0; i < pathBones.size(); i++) {
            drawBone(pathBones.get(i), Color.valueOf("7fc97f"));
        }
    }

    private void drawBone(Bone2D bone, Color color) {
        Vector2 tip = bone.getTip(temp);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(color);
        shapes.rectLine(bone.worldX, bone.worldY, tip.x, tip.y, 14.0f);
        shapes.setColor(0.15f, 0.16f, 0.17f, 1.0f);
        shapes.circle(bone.worldX, bone.worldY, 8.0f, 18);
        shapes.end();
    }

    private void drawJoint(float x, float y) {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.WHITE);
        shapes.circle(x, y, 5.0f, 16);
        shapes.end();
    }

    private void drawTarget(Vector2 target, Color color) {
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(color);
        shapes.circle(target.x, target.y, 18.0f, 24);
        shapes.line(target.x - 25.0f, target.y, target.x + 25.0f, target.y);
        shapes.line(target.x, target.y - 25.0f, target.x, target.y + 25.0f);
        shapes.end();
    }

    private void drawText() {
        game.getBatch().begin();
        font.draw(game.getBatch(), "Constraint Demo  1 IK  2 Transform  3 Path", 40.0f, stage.getViewport().getWorldHeight() - 36.0f);
        font.draw(game.getBatch(), "Mode: " + mode + "   mix: " + MathUtils.round(mix * 10.0f) / 10.0f + "   bend: " + (positiveBend ? "positive" : "negative") + "   stretch/compress: " + stretch, 40.0f, stage.getViewport().getWorldHeight() - 78.0f);
        font.draw(game.getBatch(), "Left mouse: move target/control   [ ]: mix   B: bend   S: stretch   A: add/remove transform from skin   R: rename IK", 40.0f, 58.0f);
        font.draw(game.getBatch(), "Skin constraints: " + skin.names(), 40.0f, 28.0f);
        game.getBatch().end();
    }

    private static final class Bone2D {
        final String name;
        final Bone2D parent;
        float length;
        float localX;
        float localY;
        float localRotation;
        float localScaleX = 1.0f;
        float localScaleY = 1.0f;
        float worldX;
        float worldY;
        float worldRotation;
        float worldScaleX = 1.0f;
        float worldScaleY = 1.0f;

        Bone2D(String name, Bone2D parent, float length) {
            this.name = name;
            this.parent = parent;
            this.length = length;
        }

        void updateWorld() {
            if (parent == null) {
                worldX = localX;
                worldY = localY;
                worldRotation = localRotation;
                worldScaleX = localScaleX;
                worldScaleY = localScaleY;
                return;
            }
            parent.updateWorld();
            float cos = MathUtils.cosDeg(parent.worldRotation);
            float sin = MathUtils.sinDeg(parent.worldRotation);
            worldX = parent.worldX + localX * cos - localY * sin;
            worldY = parent.worldY + localX * sin + localY * cos;
            worldRotation = parent.worldRotation + localRotation;
            worldScaleX = parent.worldScaleX * localScaleX;
            worldScaleY = parent.worldScaleY * localScaleY;
        }

        Vector2 getTip(Vector2 out) {
            out.set(length * worldScaleX, 0.0f).rotate(worldRotation).add(worldX, worldY);
            return out;
        }
    }

    private static final class IkConstraint2D {
        final String name;
        final Bone2D parent;
        final Bone2D child;
        final Vector2 target;
        float mix = 1.0f;
        float softness = 0.0f;
        boolean positiveBend = true;
        boolean compress = true;
        boolean stretch = true;

        IkConstraint2D(String name, Bone2D parent, Bone2D child, Vector2 target) {
            this.name = name;
            this.parent = parent;
            this.child = child;
            this.target = target;
        }

        void apply() {
            float px = parent.worldX;
            float py = parent.worldY;
            float dx = target.x - px;
            float dy = target.y - py;
            float distance = Math.max(0.001f, (float)Math.sqrt(dx * dx + dy * dy) - softness);
            float parentLength = parent.length * parent.worldScaleX;
            float childLength = child.length * child.worldScaleX;
            if (!stretch) {
                distance = Math.min(distance, parentLength + childLength);
            }
            if (!compress) {
                distance = Math.max(distance, Math.abs(parentLength - childLength));
            }

            float cosChild = (distance * distance - parentLength * parentLength - childLength * childLength) / (2.0f * parentLength * childLength);
            cosChild = MathUtils.clamp(cosChild, -1.0f, 1.0f);
            float childAngle = (float)Math.acos(cosChild) * MathUtils.radiansToDegrees;
            if (!positiveBend) {
                childAngle = -childAngle;
            }

            float adjacent = parentLength + childLength * MathUtils.cosDeg(childAngle);
            float opposite = childLength * MathUtils.sinDeg(childAngle);
            float parentAngle = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees - MathUtils.atan2(opposite, adjacent) * MathUtils.radiansToDegrees;
            parent.localRotation = lerpAngle(parent.localRotation, parentAngle, mix);
            parent.updateWorld();
            child.localRotation = lerpAngle(child.localRotation, childAngle, mix);
            child.localX = parent.length;
            child.updateWorld();
        }
    }

    private static final class TransformConstraint2D {
        final String name;
        final Bone2D bone;
        final Bone2D target;
        float rotateMix = 1.0f;
        float translateMix = 1.0f;
        float scaleMix = 1.0f;
        float offsetRotate = 25.0f;
        float offsetX = 0.0f;
        float offsetY = 110.0f;
        float offsetScaleX = 0.0f;
        float offsetScaleY = 0.0f;

        TransformConstraint2D(String name, Bone2D bone, Bone2D target) {
            this.name = name;
            this.bone = bone;
            this.target = target;
        }

        void apply() {
            bone.localRotation = lerpAngle(bone.localRotation, target.localRotation + offsetRotate, rotateMix);
            bone.localX += (target.localX + offsetX - bone.localX) * translateMix;
            bone.localY += (target.localY + offsetY - bone.localY) * translateMix;
            bone.localScaleX += (target.localScaleX + offsetScaleX - bone.localScaleX) * scaleMix;
            bone.localScaleY += (target.localScaleY + offsetScaleY - bone.localScaleY) * scaleMix;
            bone.updateWorld();
        }
    }

    private static final class PathConstraint2D {
        final String name;
        final List<Bone2D> bones;
        final Vector2 control0 = new Vector2();
        final Vector2 control1 = new Vector2();
        final Vector2 control2 = new Vector2();
        final Vector2 control3 = new Vector2();
        float position;
        float spacing = 0.115f;
        float rotateMix = 1.0f;
        float translateMix = 1.0f;
        boolean tangent = true;
        boolean chainScale = true;

        PathConstraint2D(String name, List<Bone2D> bones) {
            this.name = name;
            this.bones = bones;
            control0.set(170.0f, 230.0f);
            control1.set(310.0f, 470.0f);
            control2.set(620.0f, 210.0f);
            control3.set(780.0f, 430.0f);
        }

        void apply() {
            Vector2 point = new Vector2();
            Vector2 next = new Vector2();
            for (int i = 0; i < bones.size(); i++) {
                float t = (position + spacing * i) % 1.0f;
                pointAt(t, point);
                pointAt((t + 0.01f) % 1.0f, next);
                Bone2D bone = bones.get(i);
                bone.localX += (point.x - bone.localX) * translateMix;
                bone.localY += (point.y - bone.localY) * translateMix;
                if (tangent) {
                    float angle = MathUtils.atan2(next.y - point.y, next.x - point.x) * MathUtils.radiansToDegrees;
                    bone.localRotation = lerpAngle(bone.localRotation, angle, rotateMix);
                }
                if (chainScale) {
                    bone.localScaleX = 0.8f + 0.4f * MathUtils.sin(t * MathUtils.PI2);
                }
                bone.updateWorld();
            }
        }

        void pointAt(float t, Vector2 out) {
            float inv = 1.0f - t;
            float a = inv * inv * inv;
            float b = 3.0f * inv * inv * t;
            float c = 3.0f * inv * t * t;
            float d = t * t * t;
            out.set(
                    control0.x * a + control1.x * b + control2.x * c + control3.x * d,
                    control0.y * a + control1.y * b + control2.y * c + control3.y * d
            );
        }
    }

    private static final class ConstraintSkin {
        final String name;
        private final Map<String, Object> constraints = new LinkedHashMap<String, Object>();

        ConstraintSkin(String name) {
            this.name = name;
        }

        String add(String requestedName, Object constraint) {
            String uniqueName = uniqueName(requestedName);
            constraints.put(uniqueName, constraint);
            return uniqueName;
        }

        void remove(String name) {
            constraints.remove(name);
        }

        void rename(String oldName, String requestedName) {
            Object value = constraints.remove(oldName);
            if (value != null) {
                constraints.put(uniqueName(requestedName), value);
            }
        }

        boolean contains(String name) {
            return constraints.containsKey(name);
        }

        String names() {
            return constraints.keySet().toString();
        }

        private String uniqueName(String requestedName) {
            if (!constraints.containsKey(requestedName)) {
                return requestedName;
            }
            int index = 2;
            while (constraints.containsKey(requestedName + "-" + index)) {
                index++;
            }
            return requestedName + "-" + index;
        }
    }

    private static float lerpAngle(float from, float to, float alpha) {
        float delta = ((to - from + 180.0f + 360.0f) % 360.0f) - 180.0f;
        return from + delta * alpha;
    }
}
