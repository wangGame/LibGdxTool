package com.joker.domos.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.joker.domos.GameTest;
import com.joker.domos.listener.UserInputListener;

public class HtmlLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(true);
        config.padHorizontal = 0;
        config.padVertical = 0;
        return config;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new GameTest(new UserInputListener() {
            @Override
            public void showHandleInput(String hint, Input.TextInputListener callback) {
                Gdx.input.getTextInput(callback, hint, "", hint);
            }
        });
    }
}
