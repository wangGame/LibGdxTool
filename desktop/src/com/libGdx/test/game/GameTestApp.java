package com.libGdx.test.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.joker.domos.GameTest;
import com.joker.domos.listener.UserInputListener;
import com.kw.gdx.resource.annotation.GameInfo;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/25 19:34
 */
@GameInfo
public class GameTestApp {
    public static void main(String[] args) {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "GameTestApp";
        configuration.width = 800;
        configuration.height = 600;
        new LwjglApplication(new GameTest(new UserInputListener() {
            @Override
            public void showHandleInput(String hint, Input.TextInputListener callback) {

            }
        }), configuration);
    }
}
