package com.kw;


import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

public class MutableLiveData<T> {

    public interface Observer<T> {
        void onChanged(T value);
    }

    private final CopyOnWriteArrayList<Observer<T>> observers =
            new CopyOnWriteArrayList<>();

    private final Executor mainExecutor;

    private volatile T value;

    public MutableLiveData(Executor mainExecutor) {
        this.mainExecutor = mainExecutor;
    }

    public MutableLiveData(T value, Executor mainExecutor) {
        this.value = value;
        this.mainExecutor = mainExecutor;
    }

    /**
     * 注册观察者
     */
    public void observe(Observer<T> observer) {
        if (observer == null) return;

        observers.addIfAbsent(observer);

        // 注册时立即发送当前值
        observer.onChanged(value);
    }

    /**
     * 移除观察者
     */
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    /**
     * 主线程设置数据
     */
    public void setValue(T value) {
        this.value = value;
        dispatchValue(value);
    }

    /**
     * 任意线程提交数据
     */
    public void postValue(T value) {
        mainExecutor.execute(() -> setValue(value));
    }

    /**
     * 获取当前数据
     */
    public T getValue() {
        return value;
    }

    /**
     * 通知所有观察者
     */
    private void dispatchValue(T value) {
        for (Observer<T> observer : observers) {
            observer.onChanged(value);
        }
    }

}
