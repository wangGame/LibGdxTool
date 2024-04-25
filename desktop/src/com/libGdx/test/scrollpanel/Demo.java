package com.libGdx.test.scrollpanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/5 18:20
 */
public class Demo extends LibGdxTestMain {
    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.start(demo);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Table table = new Table(){
            {
                for (int i = 0; i < 15; i++) {
                    add(new DemoGroup(i));
                }
                pack();
            }
        };
        ScrollPane pane = new ScrollPane(table);
        pane.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        addActor(pane);
    }
}
