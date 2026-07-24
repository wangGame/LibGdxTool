package com.libGdx.test.anr;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kw.gdx.anr.ANRDEMO;
import com.kw.gdx.resource.annotation.GameInfo;
import com.libGdx.test.base.LibGdxTestMain;

import org.knowm.xchart.style.theme.Theme;

@GameInfo(width = 100)
@ANRDEMO(delaytime = 50)
public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage){
        super.useShow(stage);

        stage.addAction(Actions.forever(Actions.delay(1f, Actions.run(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }))));
    }
}
