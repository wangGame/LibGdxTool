package com.libGdx.test.anr;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.libGdx.test.base.LibGdxTestMain;

public class RUn {
    public static void main(String[] args) {
           LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.x = 1000;
            config.stencil=8;
            config.y = 0;
            config.height = 900;
            config.width = 900;
            new LwjglApplication(new ANRDemoGame(), config);

    }
}
