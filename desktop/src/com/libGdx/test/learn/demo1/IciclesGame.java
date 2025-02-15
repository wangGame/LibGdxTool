package com.libGdx.test.learn.demo1;

import com.badlogic.gdx.Game;



public class IciclesGame extends Game {

    @Override
    public void create() {
        showDifficultyScreen();
    }


    public void showDifficultyScreen() {
        // TODO: Show the difficulty screen
        setScreen(new DifficultyScreen(this));
    }

    public void showIciclesScreen(Constants.Difficulty difficulty) {
        // TODO: Show the Icicles screen with the appropriate difficulty
        setScreen(new IciclesScreen(this, difficulty));
    }
}
