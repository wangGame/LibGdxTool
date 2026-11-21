package com.example.mvvm;

import com.kw.gdx.event.simpleevent.SingleEventManager;

public class StatelessViewModel extends ViewModel {
    private SingleEventManager singleEventManager;
    public StatelessViewModel(){
        this.singleEventManager = new SingleEventManager();
    }

    public SingleEventManager getSingleEventManager() {
        return singleEventManager;
    }
}
