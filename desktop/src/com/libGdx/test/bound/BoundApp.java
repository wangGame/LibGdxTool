package com.libGdx.test.bound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class BoundApp extends LibGdxTestMain {
    public static void main(String[] args) {
        BoundApp app = new BoundApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        Gdx.input.getTextInput(new Input.TextInputListener() {
//
//            @Override
//            public void input(String text) {
//
//            }
//
//            @Override
//            public void canceled() {
//            }
//        }, "Enter name", "");
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {

            }

            @Override
            public void canceled() {

            }
        },"x","xx","xxxx");
    }
}
