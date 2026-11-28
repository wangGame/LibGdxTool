package com.joker.puzzle.iosmoe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.joker.domos.listener.UserInputListener;

/** Native text input for the shared LibGdxTool demo. */
public final class IOSGameListener implements UserInputListener {
    @Override
    public void showHandleInput(String hint, Input.TextInputListener callback) {
        if (callback != null) {
            String prompt = hint == null ? "" : hint;
            Gdx.input.getTextInput(callback, prompt, "", prompt);
        }
    }
}
