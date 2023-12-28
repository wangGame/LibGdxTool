package com.libGdx.test;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/28 13:17
 */
class XX extends LibGdxTestMain {
    public static void main(String[] args) {
        XX xx = new XX();
        xx.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Table table = new Table() {{
            for (int i = 0; i < 10; i++) {
                add(new Image());
                add(new Image());
                row();
            }
            pack();
        }};
        ScrollPane pane = new ScrollPane(table){
            @Override
            public void act(float delta) {
                super.act(delta);
                if (getScrollPercentY()>0.9) {
                    addContent(table);
                }
            }
        };
    }

    public void addContent(Table table){
        for (int i = 0; i < 10; i++) {
            table.add(new Image());
            table.add(new Image());
            table.row();
        }
    }
}
