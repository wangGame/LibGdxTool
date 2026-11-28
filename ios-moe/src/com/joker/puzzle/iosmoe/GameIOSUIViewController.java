package com.joker.puzzle.iosmoe;

import apple.uikit.UIView;
import com.badlogic.gdx.backends.bindings.metalangle.MGLKView;
import com.badlogic.gdx.backends.iosmoe.IOSUIViewController;
import com.badlogic.gdx.backends.iosmoe.IOSGraphics;
import com.badlogic.gdx.backends.iosmoe.IOSAudio;
import com.badlogic.gdx.Gdx;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.RegisterOnStartup;
import org.moe.natj.objc.ann.Selector;

@RegisterOnStartup
public class GameIOSUIViewController extends IOSUIViewController {
    protected GameIOSUIViewController(Pointer peer) { super(peer); }
    @Selector("alloc")
    public static native GameIOSUIViewController alloc();

    @Override
    public void setView(UIView view) {
        super.setView(view);
        applyStableRenderScale(view);
    }

    @Override
    public void viewDidLayoutSubviews() {
        applyStableRenderScale(view());
        super.viewDidLayoutSubviews();
    }

    @Override
    public void viewDidAppear(boolean animated) {
        super.viewDidAppear(animated);
        // The MOE backend writes nativeScale here. Restore UIKit's scale before the next draw.
        applyStableRenderScale(view());
    }

    private static void applyStableRenderScale(UIView view) {
        if (view instanceof MGLKView) {
            double scale = GameIOSGraphics.renderScale();
            if (Math.abs(view.contentScaleFactor() - scale) > 0.000001) {
                view.setContentScaleFactor(scale);
            }
        }
    }

    @Override
    public void viewWillDisappear(boolean animated) {
        if (Gdx.graphics instanceof IOSGraphics) ((IOSGraphics) Gdx.graphics).pause();
        if (Gdx.audio instanceof IOSAudio) ((IOSAudio) Gdx.audio).willResignActive();
        super.viewWillDisappear(animated);
        setPaused(true);
    }

    @Override
    public void viewWillAppear(boolean animated) {
        applyStableRenderScale(view());
        super.viewWillAppear(animated);
        if (Gdx.audio instanceof IOSAudio) ((IOSAudio) Gdx.audio).didBecomeActive();
        if (Gdx.graphics instanceof IOSGraphics) ((IOSGraphics) Gdx.graphics).resume();
    }
}
