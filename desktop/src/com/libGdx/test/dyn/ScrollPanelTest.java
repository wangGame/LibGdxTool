package com.libGdx.test.dyn;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/8/4 12:24
 */
public class ScrollPanelTest extends LibGdxTestMain {
    public static void main(String[] args) {
        ScrollPanelTest test = new ScrollPanelTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        DaynScrollPanel daynScrollPanel = new DaynScrollPanel();
        daynScrollPanel.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        stage.addActor(daynScrollPanel);
    }
}
