package com.libGdx.test.wiget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.group.PageView;
import com.libGdx.test.base.LibGdxTestMain;


public class AppDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        AppDemo appDemo = new AppDemo();
        appDemo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Group group = new Group();
        group.setSize(Constant.GAMEWIDTH-100, Constant.GAMEHIGHT-100);
        addActor(group);
        group.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f, Align.center);


        PageView pageView = new PageView(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        pageView.setSize(Constant.GAMEWIDTH-100, Constant.GAMEHIGHT-100);
        group.addActor(pageView);
        for (int i = 0; i < 100; i++) {
            pageView.add(new ItemGroup());
        }






//        Widget widget = new Widget();
//        group.addActor(widget);
//        widget.setFillParent(true);
//        widget.setDebug(true);
//
//        Actor actor = new Actor();
//        actor.setSize(100,100);

    }
}
