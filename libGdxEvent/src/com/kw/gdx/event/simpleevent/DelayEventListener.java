package com.kw.gdx.event.simpleevent;

import java.util.ArrayList;

public class DelayEventListener<T> extends EventListener<T>{
    private ArrayList<SubTaskManager> finishSub = new ArrayList<>();
    public void update(float dt){
        if (subTaskManagers.size()<=0){
            return;
        }
        //
        for (SubTaskManager<T> subTaskManager : subTaskManagers) {
            /**
             * 条件到了  就执行  执行完就删除，每次subTask是单独出创建的， 并且这里不处理重复事件，
             *
             * 如果有需要在创建一个删除条件，我认为这样比较合理
             */
            if (subTaskManager.update(dt) && subTaskManager.condiction()){
                if (subTaskManager.getEventType() == EventType.OneRemove) {
                    finishSub.add(subTaskManager);
                    listener(subTaskManager.getData());
                }
            }
        }
        if(finishSub.size()>0){
            for (SubTaskManager subTaskManager : finishSub) {
                subTaskManagers.remove(subTaskManager);
            }
            finishSub.clear();
        }
    }
}
