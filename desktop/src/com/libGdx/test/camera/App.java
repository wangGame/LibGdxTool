package com.libGdx.test.camera;

import com.badlogic.gdx.Gdx;
import com.libGdx.test.base.LibGdxTestMain;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
public class App {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.stencil=8;
        config.y = 0;
        config.height = (int) (1920 * 0.4f);
        config.width = (int) (1080* 0.8f);
        new LwjglApplication(new Closeup(), config);

    }
}
