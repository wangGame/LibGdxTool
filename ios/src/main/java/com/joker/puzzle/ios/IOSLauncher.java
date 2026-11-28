package com.joker.puzzle.ios;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.joker.domos.GameTest;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

/** Launches the same demo as Android on iPhone and iPad. */
public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new GameTest((hint, callback) -> {
            if (callback != null) {
                Gdx.input.getTextInput(callback, hint == null ? "" : hint, "", hint == null ? "" : hint);
            }
        }), config);
    }

    public static void main(String[] args) {
        try (NSAutoreleasePool pool = new NSAutoreleasePool()) {
            UIApplication.main(args, null, IOSLauncher.class);
        }
    }
}
