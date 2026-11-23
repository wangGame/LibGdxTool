package com.example.mvvm;

import com.kw.gdx.event.simpleevent.EventListener;

import org.junit.Test;

public class StatelessViewModelTest {
    @Test
    public void statelessRun(){
        StatelessViewModel statelessViewModel = new StatelessViewModel();
        statelessViewModel.getSingleEventManager().addEventListener("test", new EventListener<Object>() {
            @Override
            public void listener(Object o) {
                super.listener(o);
                System.out.println("test");
            }
        });
        statelessViewModel.getSingleEventManager().submit("test",new Object());

    }
}