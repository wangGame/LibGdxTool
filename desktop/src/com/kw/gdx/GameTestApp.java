package com.kw.gdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.joker.domos.GameTest;
import com.joker.domos.listener.UserInputListener;

public class GameTestApp {
    public static void main(String[] args) {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "Game Test";
        configuration.width = 800;
        configuration.height = 600;
        new LwjglApplication(new GameTest(new UserInputListener() {
            @Override
            public void showHandleInput(String hint, Input.TextInputListener callback) {

            }
        }), configuration);
    }
}
