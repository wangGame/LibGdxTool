package com.joker.domos;

import com.joker.domos.listener.UserInputListener;
import com.joker.domos.screen.LoadScreen;
import com.kw.gdx.BaseGame;

public class GameTest extends BaseGame {
    private static UserInputListener userInputListener;
    public GameTest(UserInputListener userInputListener) {
        this.userInputListener = userInputListener;
    }
    @Override
    protected void loadingView() {
        super.loadingView();
        setScreen(new LoadScreen(this));
    }

    public static UserInputListener getUserInputListener() {
        return userInputListener;
    }
}
