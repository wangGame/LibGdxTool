package com.kw.gdx.event.simpleevent.demo;

import com.kw.gdx.event.simpleevent.EventListener;
import com.kw.gdx.event.simpleevent.EventManager;

/**
 * event事件system
 */
public class App {
    public static void main(String[] args) {
        {
            EventManager instance = EventManager.getInstance();
            instance.addEventListener("addCoin", new EventListener<Integer>() {
                @Override
                public void listener(Integer e) {
                    System.out.println(e);
                }
            });
            instance.submit(",mainAddCoin", 10);
        }
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


        //退出或者移除的时候清除
    }
}
