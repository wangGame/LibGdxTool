package com.libGdx.test.base;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.BaseGame;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.screen.BaseScreen;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/21 15:51
 */
public class LinGdxTestMain extends LibGdxTestBase {

    @Override
    protected void loadingView() {
        super.loadingView();
        setScreen(new TestScreen(this));
    }

    class TestScreen extends BaseScreen {
        public TestScreen(BaseGame game) {
            super(game);
        }

        @Override
        public void show() {
            super.show();
            useShow(stage);
        }
    }

    public void useShow(Stage stage){

    }
}
