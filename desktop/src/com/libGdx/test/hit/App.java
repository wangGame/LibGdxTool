package com.libGdx.test.hit;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
        Actor a = new Actor(){
            @Override
            public Actor hit(float x, float y, boolean touchable) {
                return super.hit(x, y, touchable);
            }
        };
        addActor(a);
    }
}
