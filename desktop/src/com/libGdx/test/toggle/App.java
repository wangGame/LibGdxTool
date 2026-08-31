package com.libGdx.test.toggle;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.toggle.ToggleContainer;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        toggleUsed();

    }

    private void toggleUsed() {
        ToggleContainer toggleContainer = new ToggleContainer(-1);
        toggleContainer.setPaddLeft(10);
        toggleContainer.setPaddRight(10);
        toggleContainer.setMultSelect(true);
        addActor(toggleContainer);
        for (int i = 0; i < 100; i++) {
            toggleContainer.addToggleButton(new ItemToggle());
        }
        toggleContainer.pack();
        toggleContainer.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT - 100, Align.center);
    }
}
