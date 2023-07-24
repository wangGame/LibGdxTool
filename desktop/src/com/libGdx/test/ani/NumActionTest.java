package com.libGdx.test.ani;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.IntAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.asset.AssetLoadingTest;
import com.libGdx.test.asset.EffectResResourceTest;
import com.libGdx.test.asset.SpineResResourceTest;
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
        IntAction intAction = new IntAction(0, 100);
        intAction.setDuration(10);
        intAction.setReverse(true);
        Image i = new Image(){
            @Override
            public void act(float delta) {
                super.act(delta);
                System.out.println(intAction.getValue());
            }
        };
        i.addAction(intAction);
        stage.addActor(i);

    }
}
