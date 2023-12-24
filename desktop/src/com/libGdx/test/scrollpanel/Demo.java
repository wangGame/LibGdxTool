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

    private float baseX;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Table table = new Table(){
            {
                for (int i = 0; i < 100; i++) {
                    add(new DemoGroup(){
                        @Override
                        public void draw(Batch batch, float parentAlpha) {
                            super.draw(batch, parentAlpha);
                            float v = getX() - baseX;
                            setOffSetX(v);
                        }
                    });
                }
                pack();
            }
        };
        ScrollPane pane = new ScrollPane(table){
            @Override
            public void act(float delta) {

                baseX = getScrollX()+Constant.GAMEWIDTH/2;
                super.act(delta);
            }
        };
        pane.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        addActor(pane);



        pane.addAction(Actions.sequence(Actions.run(new Runnable() {
            @Override
            public void run() {
                pane.setScrollX(100);
                pane.updateVisualScroll();
            }
        }),Actions.delay(4),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        pane.setScrollX(0);
                        pane.updateVisualScroll();
                    }
                }),Actions.delay(4),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {

                        pane.setScrollX(29);

                    }
                }),Actions.delay(4),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        pane.setScrollX(500);

                    }
                }),Actions.delay(4),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {

                        pane.setScrollX(400);

                    }
                })
                ));
    }
}
