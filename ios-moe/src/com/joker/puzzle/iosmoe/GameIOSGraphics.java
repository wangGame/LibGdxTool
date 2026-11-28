package com.joker.puzzle.iosmoe;

import apple.uikit.UIScreen;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import com.badlogic.gdx.backends.iosmoe.IOSGraphics;
import com.badlogic.gdx.backends.iosmoe.IOSInput;

/** Keeps the render scale consistent with UIKit. */
public class GameIOSGraphics extends IOSGraphics {
    public GameIOSGraphics(IOSApplication application, IOSApplicationConfiguration configuration, IOSInput input) {
        super(application, configuration, input, configuration.useGL30);
    }

    @Override
    public float getBackBufferScale() {
        return (float) renderScale();
    }

    public static double renderScale() {
        return UIScreen.mainScreen().scale();
    }
}
