package com.libGdx.test.action;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.IntAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.action.NumAction;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/20 10:17
 */
public class NumActionTest extends LibGdxTestMain {
    public static void main(String[] args) {
        NumActionTest test  = new NumActionTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        NumAction numAction = new NumAction(0, 100);
        numAction.setDuration(10);
        numAction.setReverse(true);
        Image i = new Image(){
            @Override
            public void act(float delta) {
                super.act(delta);
                System.out.println(numAction.getValue());
            }
        };
        i.addAction(numAction);
        stage.addActor(i);

    }
}
