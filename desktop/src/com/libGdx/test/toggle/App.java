package com.libGdx.test.toggle;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.action.NumAction;
import com.kw.gdx.action.NumActionListener;
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
//        toggleUsed();
        tiggleUseScrolle();
    }

    private void tiggleUseScrolle() {
        ToggleContainer toggleContainer = new ToggleContainer(-1);
        toggleContainer.setPaddLeft(10);
        toggleContainer.setPaddRight(10);
        toggleContainer.setMultSelect(true);
        toggleContainer.TopEmpty(10,false);
        for (int i = 0; i < 100; i++) {
            toggleContainer.addToggleButton(new ItemToggle(i));
        }
        toggleContainer.pack();
        toggleContainer.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT - 100, Align.center);


        ScrollPane scrollPane = new ScrollPane(toggleContainer);
        scrollPane.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT - 100);
        scrollPane.setPosition(0,0);
        addActor(scrollPane);
        float v1 = toggleContainer.caluHeight(5);
        float v = toggleContainer.caluHeight(20);
        NumAction numAction = new NumAction();
        numAction.setStart(v1);
        numAction.setEnd(v);
        numAction.setDuration(5);
        numAction.setNumActionListener(new NumActionListener() {
            @Override
            public void update(float value) {
                toggleContainer.scrollPointX(scrollPane,value);
            }
        });
        scrollPane.addAction(numAction);
    }

    private void toggleUsed() {
        ToggleContainer toggleContainer = new ToggleContainer(-1);
        toggleContainer.setPaddLeft(10);
        toggleContainer.setPaddRight(10);
        toggleContainer.setMultSelect(true);
        addActor(toggleContainer);
        for (int i = 0; i < 100; i++) {
            toggleContainer.addToggleButton(new ItemToggle(i));
        }
        toggleContainer.pack();
        toggleContainer.setPosition(Constant.GAMEWIDTH/2,Constant.GAMEHIGHT - 100, Align.center);
    }


}
