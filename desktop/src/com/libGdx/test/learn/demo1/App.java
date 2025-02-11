package com.libGdx.test.learn.demo1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.stencil=8;
        config.y = 0;
        config.height = (int) (1920 * 0.25f);
        config.width = (int) (1080 * 0.5f);
        Gdx.isJiami = true;
        new LwjglApplication(new IciclesGame(), config);
    }
}
