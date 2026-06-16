package com.joker.puzzle.demo;

/**
 * Pure Java engine that mirrors the key behavior of DefaultAndroidInput#onKeyboardHeightChanged.
 * This class is Android-free so it can be tested from a plain JVM runner.
 */
public class KeyboardHeightCaseEngine {

    public static final class Input {
        public int height;
        public int leftInset;
        public int rightInset;
        public boolean nativeInputOpen;
        public boolean standardHeightProvider;
        public boolean popupShowing;
        public boolean useImmersiveMode;
        public int softButtonsBarHeight;
        public boolean adjustNothing;
        public int keyboardLandscapeHeight;
        public int keyboardPortraitHeight;
        public int editTextHeight;
        public int screenWidth;
    }

    public static final class Output {
        public float x;
        public float y;
        public float scaleX = 1f;
        public int observerHeight;
        public boolean closedTextInputField;
        public boolean popupRefreshed;
        public boolean skipLayoutUpdate;
    }

    public Output apply(Input in) {
        Output out = new Output();

        int height = in.height;
        if (in.useImmersiveMode && in.standardHeightProvider) {
            height += in.softButtonsBarHeight;
        }

        if (!in.nativeInputOpen) {
            out.observerHeight = height;
            return out;
        }

        if (height == 0) {
            if (!in.standardHeightProvider && (in.keyboardLandscapeHeight != 0 || in.keyboardPortraitHeight != 0)) {
                out.closedTextInputField = true;
            }
            if (in.standardHeightProvider && in.popupShowing) {
                out.skipLayoutUpdate = true;
                return out;
            }
            out.observerHeight = 0;
            out.y = 0f;
            return out;
        }

        out.observerHeight = height + in.editTextHeight;

        if (!in.adjustNothing) {
            height = 0;
        }

        out.y = -height;

        int width = in.screenWidth <= 0 ? 1 : in.screenWidth;
        out.scaleX = ((float)width - in.rightInset - in.leftInset) / width;
        out.x = (float)(in.leftInset - in.rightInset) / 2f;

        if (in.popupShowing) {
            out.popupRefreshed = true;
        }

        return out;
    }
}

