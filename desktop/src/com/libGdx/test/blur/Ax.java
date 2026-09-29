package com.libGdx.test.blur;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.joker.domos.screen.BlurScreen;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class Ax extends LibGdxTestMain {
    public static void main(String[] args) {
        Ax ax = new Ax();
        ax.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Constant.SHOWFRAMESPERSECOND = true;
        setScreen(BlurScreen.class);
    }
}
