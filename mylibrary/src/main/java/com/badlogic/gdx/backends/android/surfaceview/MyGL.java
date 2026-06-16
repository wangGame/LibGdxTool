//package com.badlogic.gdx.backends.android.surfaceview;
//
//
//import com.badlogic.gdx.backends.android.AndroidApplicationBase;
//import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
//import com.badlogic.gdx.backends.android.AndroidGraphics;
//
//import javax.microedition.khronos.opengles.GL10;
//
//public class MAndroidGraphics extends AndroidGraphics {
//    public MAndroidGraphics(AndroidApplicationBase application, AndroidApplicationConfiguration config, ResolutionStrategy resolutionStrategy) {
//        super(application, config, resolutionStrategy);
//    }
//
//    public MAndroidGraphics(AndroidApplicationBase application, AndroidApplicationConfiguration config, ResolutionStrategy resolutionStrategy, boolean focusableView) {
//        super(application, config, resolutionStrategy, focusableView);
//    }
//
//    String LOG_TAG = "Doodle";
//
//    @Override
//    public void onDrawFrame(GL10 gl) {
//        long time = System.nanoTime();
//        deltaTime = (time - lastFrameTime) / 1000000000.0f;
//        lastFrameTime = time;
//
//        // After pause deltaTime can have somewhat huge value that destabilizes the mean, so let's cut it off
//        if (!resume) {
//            mean.addValue(deltaTime);
//        } else {
//            deltaTime = 0;
//        }
//
//        boolean lrunning = false;
//        boolean lpause = false;
//        boolean ldestroy = false;
//        boolean lresume = false;
//
//        synchronized (synch) {
//            lrunning = running;
//            lpause = pause;
//            ldestroy = destroy;
//            lresume = resume;
//
//            if (resume) {
//                resume = false;
//            }
//
//            if (pause) {
//                pause = false;
//                synch.notifyAll();
//            }
//
//            if (destroy) {
//                destroy = false;
//                synch.notifyAll();
//            }
//        }
//
//        if (lresume) {
//            SnapshotArray<LifecycleListener> lifecycleListeners = app.getLifecycleListeners();
//            synchronized (lifecycleListeners) {
//                LifecycleListener[] listeners = lifecycleListeners.begin();
//                for (int i = 0, n = lifecycleListeners.size; i < n; ++i) {
//                    listeners[i].resume();
//                }
//                lifecycleListeners.end();
//            }
//            app.getApplicationListener().resume();
//            Gdx.app.log(LOG_TAG, "resumed");
//        }
//
//        if (lrunning) {
//            synchronized (app.getRunnables()) {
//                app.getExecutedRunnables().clear();
//                app.getExecutedRunnables().addAll(app.getRunnables());
//                app.getRunnables().clear();
//            }
//
//            for (int i = 0; i < app.getExecutedRunnables().size; i++) {
//                try {
//                    app.getExecutedRunnables().get(i).run();
//                } catch (Throwable t) {
//                    t.printStackTrace();
//                }
//            }
//            app.getInput().processEvents();
//            frameId++;
//            app.getApplicationListener().render();
//        }
//
//        if (lpause) {
//            SnapshotArray<LifecycleListener> lifecycleListeners = app.getLifecycleListeners();
//            synchronized (lifecycleListeners) {
//                LifecycleListener[] listeners = lifecycleListeners.begin();
//                for (int i = 0, n = lifecycleListeners.size; i < n; ++i) {
//                    listeners[i].pause();
//                }
//            }
//            app.getApplicationListener().pause();
//            Gdx.app.log(LOG_TAG, "paused");
//        }
//
//        if (ldestroy) {
//            SnapshotArray<LifecycleListener> lifecycleListeners = app.getLifecycleListeners();
//            synchronized (lifecycleListeners) {
//                LifecycleListener[] listeners = lifecycleListeners.begin();
//                for (int i = 0, n = lifecycleListeners.size; i < n; ++i) {
//                    listeners[i].dispose();
//                }
//            }
//            app.getApplicationListener().dispose();
//            Gdx.app.log(LOG_TAG, "destroyed");
//        }
//
//        if (time - frameStart > 1000000000) {
//            fps = frames;
//            frames = 0;
//            frameStart = time;
//        }
//        frames++;
//    }
//}
