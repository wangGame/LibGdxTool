package com.kw.gdx.event;

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
            instance.sumbit("addCoin", 10);
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
            instance.sumbit("addCoin1", data);
        }

    }
}
