package com.libGdx.test.scrollroll;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class ScrollPanelApp extends LibGdxTestMain {
    public static void main(String[] args) {
        ScrollPanelApp app = new ScrollPanelApp();
        app.start();
    }

    private float minY;
    private float maxY;

    private DemoGroup image;

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        image = new DemoGroup();

        image.setIndex(5);
        image.setX(Constant.GAMEWIDTH/2.0f,Align.center);
        ScrollPane pane = new ScrollPane(new Table(){{
            for (int i = 0; i < 20; i++) {
                DemoGroup demoGroup = new DemoGroup();
                add(demoGroup);
                demoGroup.setIndex(i);
                row();
            }
            minY = 500 * 4  - Constant.GAMEHIGHT/2.0f+30;
            maxY = 500 * 4 - 500 + Constant.GAMEHIGHT/2.0f+30;
            pack();
        }}){
            @Override
            public void act(float delta) {
                super.act(delta);
                if (getScrollY()>=maxY){
                    image.setY(Constant.GAMEHIGHT, Align.top);
                    image.setVisible(true);
                }else if (getScrollY()<=minY){
                    image.setY(0);
                    image.setVisible(true);
                }else {
                    image.setVisible(false);
                }
            }
        };
        pane.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        addActor(pane);
        addActor(image);
    }
}
