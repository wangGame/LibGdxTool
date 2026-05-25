package com.libGdx.test.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kw.gdx.action.NewActions;

import java.util.LinkedHashSet;

public final class SpineTranslateActionParser {
    private SpineTranslateActionParser() {
    }

    public static Array<String> getAnimationNames(String jsonText) {
        JsonValue root = new JsonReader().parse(jsonText);
        return getAnimationNames(root);
    }

    public static Array<String> getAnimationNames(JsonValue root) {
        JsonValue animations = requireChild(root, "animations");
        Array<String> names = new Array<>();
        for (JsonValue animation = animations.child; animation != null; animation = animation.next) {
            names.add(animation.name);
        }
        return names;
    }

    public static void printAnimationNames(String jsonText) {
        printAnimationNames(new JsonReader().parse(jsonText));
    }

    public static void printAnimationNames(JsonValue root) {
        Array<String> names = getAnimationNames(root);
        System.out.println("Spine animations:");
        for (String name : names) {
            System.out.println(name);
        }
    }

    public static Array<String> getAnimationTrackSummary(String jsonText, String animationName) {
        JsonValue root = new JsonReader().parse(jsonText);
        return getAnimationTrackSummary(root, animationName);
    }

    public static Array<String> getAnimationTrackSummary(JsonValue root, String animationName) {
        Array<String> lines = new Array<>();
        JsonValue animation = requireChild(requireChild(root, "animations"), animationName);

        JsonValue bones = animation.get("bones");
        if (bones != null) {
            for (JsonValue bone = bones.child; bone != null; bone = bone.next) {
                LinkedHashSet<String> labels = collectTrackLabels(bone, true);
                if (!labels.isEmpty()) {
                    lines.add("bone " + bone.name + ": " + joinLabels(labels));
                }
            }
        }

        JsonValue slots = animation.get("slots");
        if (slots != null) {
            for (JsonValue slot = slots.child; slot != null; slot = slot.next) {
                LinkedHashSet<String> labels = collectTrackLabels(slot, false);
                if (!labels.isEmpty()) {
                    lines.add("slot " + slot.name + ": " + joinLabels(labels));
                }
            }
        }

        return lines;
    }

    public static void printAnimationTrackSummary(String jsonText, String animationName) {
        printAnimationTrackSummary(new JsonReader().parse(jsonText), animationName);
    }

    public static void printAnimationTrackSummary(JsonValue root, String animationName) {
        Array<String> lines = getAnimationTrackSummary(root, animationName);
        System.out.println("Spine animation tracks: " + animationName);
        for (String line : lines) {
            System.out.println(line);
        }
    }

    public static String getTranslateTimelineText(String jsonText, String animationName, String boneName) {
        JsonValue root = new JsonReader().parse(jsonText);
        return getTranslateTimelineText(root, animationName, boneName);
    }

    public static String getTranslateTimelineText(JsonValue root, String animationName, String boneName) {
        JsonValue translateFrames = getTranslateFrames(root, animationName, boneName);
        return formatTranslateTimeline(translateFrames);
    }

    public static void printTranslateTimeline(String jsonText, String animationName, String boneName) {
        printTranslateTimeline(new JsonReader().parse(jsonText), animationName, boneName);
    }

    public static void printTranslateTimeline(JsonValue root, String animationName, String boneName) {
        System.out.println("translate timeline: " + animationName + " / " + boneName);
        System.out.println(getTranslateTimelineText(root, animationName, boneName));
    }

    public static Action parseTranslate(String jsonText, String animationName, String boneName, float baseX, float baseY) {
        JsonValue root = new JsonReader().parse(jsonText);
        return parseTranslate(root, animationName, boneName, baseX, baseY);
    }

    public static Action parseTranslate(JsonValue root, String animationName, String boneName, float baseX, float baseY) {
        JsonValue translateFrames = getTranslateFrames(root, animationName, boneName);
        return parseTranslateFrames(translateFrames, baseX, baseY);
    }

    public static Action parseTranslateFrames(JsonValue translateFrames, float baseX, float baseY) {
        if (translateFrames == null || translateFrames.size == 0) {
            return Actions.delay(0);
        }

        Action sequence = Actions.sequence();
        JsonValue firstFrame = translateFrames.child;
        float startX = getTimelineValue(firstFrame, "x", 0);
        float startY = getTimelineValue(firstFrame, "y", 0);
        sequence = Actions.sequence(
                Actions.moveToAligned(baseX + startX, baseY + startY, Align.center, 0)
        );

        for (JsonValue frame = translateFrames.child; frame != null && frame.next != null; frame = frame.next) {
            JsonValue nextFrame = frame.next;
            float currentTime = getTimelineValue(frame, "time", 0);
            float nextTime = getTimelineValue(nextFrame, "time", currentTime);
            float duration = nextTime - currentTime;
            if (duration < 0) continue;

            float currentX = getTimelineValue(frame, "x", 0);
            float currentY = getTimelineValue(frame, "y", 0);
            float nextX = getTimelineValue(nextFrame, "x", 0);
            float nextY = getTimelineValue(nextFrame, "y", 0);

            Action segmentAction = buildSegmentAction(frame, baseX, baseY, currentX, currentY, nextX, nextY, duration, currentTime);
            sequence = Actions.sequence(sequence, segmentAction);
        }

        return sequence;
    }

    private static Action buildSegmentAction(JsonValue frame, float baseX, float baseY,
                                             float currentX, float currentY, float nextX, float nextY,
                                             float duration, float currentTime) {
        JsonValue curve = frame.get("curve");
        if (curve == null) {
            return Actions.moveToAligned(baseX + nextX, baseY + nextY, Align.center, duration);
        }

        if (curve.isString()) {
            if ("stepped".equalsIgnoreCase(curve.asString())) {
                return Actions.sequence(
                        Actions.delay(duration),
                        Actions.moveToAligned(baseX + nextX, baseY + nextY, Align.center, 0)
                );
            }
            return Actions.moveToAligned(baseX + nextX, baseY + nextY, Align.center, duration);
        }

        if (curve.size < 8) {
            return Actions.moveToAligned(baseX + nextX, baseY + nextY, Align.center, duration);
        }

        return NewActions.newSpineCurveMoveAction(
                baseX, baseY,
                currentX, currentY, nextX, nextY,
                curve.getFloat(0), curve.getFloat(1), curve.getFloat(2), curve.getFloat(3),
                curve.getFloat(4), curve.getFloat(5), curve.getFloat(6), curve.getFloat(7),
                duration, currentTime
        );
    }

    private static JsonValue getTranslateFrames(JsonValue root, String animationName, String boneName) {
        JsonValue animations = requireChild(root, "animations");
        JsonValue animation = requireChild(animations, animationName);
        JsonValue bones = requireChild(animation, "bones");
        JsonValue bone = requireChild(bones, boneName);
        return requireChild(bone, "translate");
    }

    private static JsonValue requireChild(JsonValue parent, String name) {
        JsonValue child = parent == null ? null : parent.get(name);
        if (child == null) {
            throw new IllegalArgumentException("Missing json node: " + name);
        }
        return child;
    }

    private static String formatTranslateTimeline(JsonValue translateFrames) {
        StringBuilder builder = new StringBuilder();
        builder.append("\"translate\":[\n");
        for (JsonValue frame = translateFrames.child; frame != null; frame = frame.next) {
            builder.append("  {\n");
            appendFloatField(builder, frame, "time", true);
            appendFloatField(builder, frame, "x", true);
            appendFloatField(builder, frame, "y", true);
            appendCurveField(builder, frame);
            trimTrailingComma(builder);
            builder.append("\n  }");
            if (frame.next != null) {
                builder.append(',');
            }
            builder.append('\n');
        }
        builder.append("]");
        return builder.toString();
    }

    private static void appendFloatField(StringBuilder builder, JsonValue frame, String name, boolean indent) {
        JsonValue value = frame.get(name);
        if (value == null) return;
        if (indent) builder.append("    ");
        builder.append('"').append(name).append("\":").append(formatFloat(value.asFloat())).append(",\n");
    }

    private static void appendCurveField(StringBuilder builder, JsonValue frame) {
        JsonValue curve = frame.get("curve");
        if (curve == null) return;
        builder.append("    \"curve\":");
        if (curve.isString()) {
            builder.append('"').append(curve.asString()).append('"').append(",\n");
            return;
        }
        builder.append('[');
        for (int index = 0; index < curve.size; index++) {
            if (index > 0) builder.append(',');
            builder.append(formatFloat(curve.getFloat(index)));
        }
        builder.append("] ,\n");
    }

    private static void trimTrailingComma(StringBuilder builder) {
        int length = builder.length();
        if (length >= 2 && builder.charAt(length - 2) == ',') {
            builder.delete(length - 2, length - 1);
        }
    }

    private static String formatFloat(float value) {
        if (value == (long)value) {
            return Long.toString((long)value);
        }
        return Float.toString(value);
    }

    private static LinkedHashSet<String> collectTrackLabels(JsonValue target, boolean boneTrack) {
        LinkedHashSet<String> labels = new LinkedHashSet<>();
        for (JsonValue timeline = target.child; timeline != null; timeline = timeline.next) {
            String label = mapTrackName(timeline.name, boneTrack);
            if (label != null) {
                labels.add(label);
            }
        }
        return labels;
    }

    private static String mapTrackName(String trackName, boolean boneTrack) {
        if (trackName == null) return null;
        switch (trackName) {
            case "translate":
                return "位移";
            case "translatex":
                return "X位移";
            case "translatey":
                return "Y位移";
            case "scale":
                return "缩放";
            case "scalex":
                return "X缩放";
            case "scaley":
                return "Y缩放";
            case "rotate":
                return "旋转";
            case "shear":
                return "切变";
            case "shearx":
                return "X切变";
            case "sheary":
                return "Y切变";
            case "rgba":
            case "rgb":
                return boneTrack ? "颜色" : "颜色/透明度";
            case "alpha":
                return "透明度";
            case "attachment":
                return "资源切换";
            case "sequence":
                return "序列帧";
            case "deform":
                return "顶点变形";
            case "inherit":
                return "继承";
            default:
                return trackName;
        }
    }

    private static String joinLabels(LinkedHashSet<String> labels) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String label : labels) {
            if (!first) builder.append(' ');
            builder.append(label);
            first = false;
        }
        return builder.toString();
    }

    private static float getTimelineValue(JsonValue frame, String name, float defaultValue) {
        if (frame == null) return defaultValue;
        JsonValue value = frame.get(name);
        return value == null ? defaultValue : value.asFloat();
    }
}