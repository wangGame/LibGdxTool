package com.joker.puzzle.demo;

public class KeyboardHeightCaseEngineRunner {

    private static void assertTrue(boolean value, String message) {
        if (!value) {
            throw new IllegalStateException("Assertion failed: " + message);
        }
    }

    private static void assertFloat(float expected, float actual, String message) {
        float diff = Math.abs(expected - actual);
        if (diff > 0.0001f) {
            throw new IllegalStateException("Assertion failed: " + message + ", expected=" + expected + ", actual=" + actual);
        }
    }

    private static KeyboardHeightCaseEngine.Input baseInput() {
        KeyboardHeightCaseEngine.Input in = new KeyboardHeightCaseEngine.Input();
        in.nativeInputOpen = true;
        in.standardHeightProvider = true;
        in.adjustNothing = true;
        in.screenWidth = 1280;
        in.editTextHeight = 56;
        return in;
    }

    public static void main(String[] args) {
        KeyboardHeightCaseEngine engine = new KeyboardHeightCaseEngine();

        KeyboardHeightCaseEngine.Input open = baseInput();
        open.height = 400;
        KeyboardHeightCaseEngine.Output out1 = engine.apply(open);
        assertFloat(-400f, out1.y, "keyboard open should move container up");
        assertFloat(1f, out1.scaleX, "no inset should keep scaleX=1");
        assertTrue(out1.observerHeight == 456, "observer height should include editTextHeight");

        KeyboardHeightCaseEngine.Input notch = baseInput();
        notch.height = 420;
        notch.leftInset = 40;
        notch.rightInset = 60;
        notch.popupShowing = true;
        KeyboardHeightCaseEngine.Output out2 = engine.apply(notch);
        assertFloat(-10f, out2.x, "x offset should center by inset difference");
        assertTrue(out2.scaleX < 1f, "insets should reduce scaleX");
        assertTrue(out2.popupRefreshed, "popup should be refreshed after animation path");

        KeyboardHeightCaseEngine.Input resizeMode = baseInput();
        resizeMode.height = 380;
        resizeMode.adjustNothing = false;
        KeyboardHeightCaseEngine.Output out3 = engine.apply(resizeMode);
        assertFloat(0f, out3.y, "non-adjustNothing should disable manual Y movement");

        KeyboardHeightCaseEngine.Input popupClose = baseInput();
        popupClose.height = 0;
        popupClose.popupShowing = true;
        KeyboardHeightCaseEngine.Output out4 = engine.apply(popupClose);
        assertTrue(out4.skipLayoutUpdate, "close with popup on standard provider should early return");

        KeyboardHeightCaseEngine.Input floatingClose = baseInput();
        floatingClose.height = 0;
        floatingClose.standardHeightProvider = false;
        floatingClose.keyboardLandscapeHeight = 120;
        KeyboardHeightCaseEngine.Output out5 = engine.apply(floatingClose);
        assertTrue(out5.closedTextInputField, "floating keyboard close should trigger close branch");

        System.out.println("All keyboard callback engine checks passed.");
    }
}

