package com.libGdx.test.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.event.simpleevent.Data;
import com.kw.gdx.event.simpleevent.DelayEventListener;
import com.kw.gdx.event.simpleevent.EventListener;
import com.kw.gdx.event.simpleevent.EventManager;
import com.libGdx.test.base.LibGdxTestMain;

public class EventApp extends LibGdxTestMain {
    private EventManager eventManager;
    public static void main(String[] args) {
        EventApp eventApp = new EventApp();
        eventApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        eventManager = EventManager.getInstance();
        //注册
        //立即执行     不存在问题
        {
            EventManager instance = EventManager.getInstance();
            instance.addEventListener("addCoin1", new EventListener<Data>() {
                @Override
                public void listener(Data e) {
                    System.out.println(e);
                }
            });
            Data data = new Data();
            data.setAddr("xxxxxxxxxxx");
            data.setName("zzzzzzzzzzzzzzzz");
            instance.submit("addCoin1", data);
        }
        //延迟执行 ，需要考虑后面任务覆盖前面的任务
        {
            EventManager instance = EventManager.getInstance();
            instance.addEventListener("addCoin2", new DelayEventListener<Data>() {
                @Override
                public void listener(Data e) {
                    System.out.println(e);
                }
            });
            {
                Data data = new Data();
                data.setAddr("xxxxxxxxxxx");
                data.setName("zzzzzzzzzzzzzzzz");
                instance.submit("addCoin2", data,2);
            }
            {
                Data data = new Data();
                data.setAddr("xxxxxxxxxxx");
                data.setName("zzzzzzzzzzzzzzzz");
                instance.submit("addCoin2", data, 4);
            }
            {
                Data data = new Data();
                data.setAddr("xxxxxxxxxxx");
                data.setName("zzzzzzzzzzzzzzzz");
                instance.submit("addCoin2", data, 6);
            }

        }
    }

    @Override
    public void render() {
        super.render();
        if (eventManager!=null){
            eventManager.update(Gdx.graphics.getDeltaTime());
        }
    }
}
