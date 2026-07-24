//package com.badlogic.gdx.backends.android;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Debug;
//import android.os.Handler;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import com.badlogic.gdx.*;
//import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
//import com.badlogic.gdx.utils.*;
//
//import java.lang.reflect.Method;
//
//public class MAndroidApplication extends AndroidApplication{
//
//    public View initializeForView (ApplicationListener listener, AndroidApplicationConfiguration config) {
//        init2(listener, config, true);
//        return graphics.getView();
//    }
//
//    public void initialize (ApplicationListener listener, AndroidApplicationConfiguration config) {
//        init2(listener, config, false);
//    }
//
//    private void init2 (ApplicationListener listener, AndroidApplicationConfiguration config, boolean isForView) {
//        if (this.getVersion() < MINIMUM_SDK) {
//            throw new GdxRuntimeException("LibGDX requires Android API Level " + MINIMUM_SDK + " or later.");
//        }
//        setApplicationLogger(new AndroidApplicationLogger());
//        graphics = new MAndroidGraphics(this, config, config.resolutionStrategy == null ? new FillResolutionStrategy()
//                : config.resolutionStrategy);
//        input = AndroidInputFactory.newAndroidInput(this, this, graphics.view, config);
//        audio = new AndroidAudio(this, config);
//        this.getFilesDir(); // workaround for Android bug #10515463
//        files = new AndroidFiles(this.getAssets(), this.getFilesDir().getAbsolutePath());
//        net = new AndroidNet(this);
//        this.listener = listener;
//        this.handler = new Handler();
//        this.useImmersiveMode = config.useImmersiveMode;
//        this.hideStatusBar = config.hideStatusBar;
//        this.clipboard = new AndroidClipboard(this);
//
//        // Add a specialized audio lifecycle listener
//        addLifecycleListener(new LifecycleListener() {
//
//            @Override
//            public void resume () {
//                // No need to resume audio here
//            }
//
//            @Override
//            public void pause () {
////                audio.pause();
//            }
//
//            @Override
//            public void dispose () {
//                audio.dispose();
//            }
//        });
//
//        Gdx.app = this;
//        Gdx.input = this.getInput();
//        Gdx.audio = this.getAudio();
//        Gdx.files = this.getFiles();
//        Gdx.graphics = this.getGraphics();
//        Gdx.net = this.getNet();
//
//        if (!isForView) {
//            try {
//                requestWindowFeature(Window.FEATURE_NO_TITLE);
//            } catch (Exception ex) {
//                log("AndroidApplication", "Content already displayed, cannot request FEATURE_NO_TITLE", ex);
//            }
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//            setContentView(graphics.getView(), createLayoutParams());
//        }
//
//        createWakeLock(config.useWakelock);
//        hideStatusBar(this.hideStatusBar);
//        useImmersiveMode(this.useImmersiveMode);
//        if (this.useImmersiveMode && getVersion() >= Build.VERSION_CODES.KITKAT) {
//            try {
//                Class<?> vlistener = Class.forName("com.badlogic.gdx.backends.android.AndroidVisibilityListener");
//                Object o = vlistener.newInstance();
//                Method method = vlistener.getDeclaredMethod("createListener", AndroidApplicationBase.class);
//                method.invoke(o, this);
//            } catch (Exception e) {
//                log("AndroidApplication", "Failed to create AndroidVisibilityListener", e);
//            }
//        }
//
//        // detect an already connected bluetooth keyboardAvailable
//        if (getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS)
//            this.getInput().keyboardAvailable = true;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        try {
//            audio.pause();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}
