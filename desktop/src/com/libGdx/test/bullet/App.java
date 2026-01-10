package com.libGdx.test.bullet;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App.run(App.class);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        BulletFlow bulletFlow = new BulletFlow();
        addActor(bulletFlow);
    }
}
