package com.libGdx.test.ai.path;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        //25    30   00 2525
        BoardGroup group = new BoardGroup(Constant.size,Constant.size,new PathState(0,0),new PathState(Constant.size-1,Constant.size-1));
        addActor(group);
    }
}
