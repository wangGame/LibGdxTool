package com.libGdx.test.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.event.simpleevent.DelayEventListener;
import com.kw.gdx.event.simpleevent.EventListener;
import com.kw.gdx.event.simpleevent.EventManager;
import com.kw.gdx.event.simpleevent.EventType;
import com.kw.gdx.event.simpleevent.SubTaskManager;
import com.kw.gdx.group.A;
import com.libGdx.test.base.LibGdxTestMain;

public class AppRun extends LibGdxTestMain {
    public static void main(String[] args) {
        AppRun appRun = new AppRun();
        appRun.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        EventManager.getInstance().addEventListener("addCoin", new EventListener<String>() {
            @Override
            public void listener(String e) {
                System.out.println(e);
            }
        });
        EventManager.getInstance().addEventListener("delayAddCoin", new DelayEventListener<String>() {
            @Override
            public void listener(String e) {

                System.out.println(e);
            }
        });

        EventManager.getInstance().submit("addCoin","立即执行！");
        SubTaskManager subTaskManager = new SubTaskManager();
        subTaskManager.setTime(4);
        subTaskManager.setData("延迟执行！");
        subTaskManager.setEventType(EventType.Once);
        EventManager.getInstance().submit("delayAddCoin",subTaskManager);



    }

    @Override
    public void render() {
        super.render();
        EventManager.getInstance().update(Gdx.graphics.getDeltaTime());
    }
}
