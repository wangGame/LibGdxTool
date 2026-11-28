package com.joker.puzzle.iosmoe;

import apple.uikit.c.UIKit;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.joker.domos.GameTest;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.RegisterOnStartup;
import org.moe.natj.objc.ann.Selector;

/** MOE entry point for the same demo launched on Android and RoboVM. */
@RegisterOnStartup
public class Main extends IOSApplication.Delegate {
    protected Main(Pointer peer) {
        super(peer);
    }

    @Selector("alloc")
    public static native Main alloc();

    public static void main(String[] args) {
        UIKit.UIApplicationMain(0, null, null, Main.class.getName());
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationPortrait = true;
        config.orientationLandscape = true;
        config.useAccelerometer = false;
        config.useCompass = false;
        config.hdpiMode = HdpiMode.Logical;
        return new GameIOSApplication(new GameTest(new IOSGameListener()), config);
    }
}
