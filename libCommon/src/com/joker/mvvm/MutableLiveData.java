package com.joker.mvvm;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

/**
 * A small, platform-neutral observable state holder.
 * setValue() should be called on the UI thread; postValue() dispatches via mainExecutor.
 */
public class MutableLiveData<T> {
    private final CopyOnWriteArrayList<Observer<T>> observers = new CopyOnWriteArrayList<>();
    private final Executor mainExecutor;
    private volatile T value;
    private volatile boolean initialized;

    public MutableLiveData(Executor mainExecutor) {
        this.mainExecutor = Objects.requireNonNull(mainExecutor, "mainExecutor");
    }

    public MutableLiveData(T initialValue, Executor mainExecutor) {
        this(mainExecutor);
        this.value = initialValue;
        this.initialized = true;
    }

    public T getValue() {
        return value;
    }

    public boolean hasValue() {
        return initialized;
    }

    public void setValue(T newValue) {
        value = newValue;
        initialized = true;
        notifyObservers(newValue);
    }

    public void postValue(final T newValue) {
        mainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                setValue(newValue);
            }
        });
    }

    public void observe(Observer<T> observer) {
        Objects.requireNonNull(observer, "observer");
        if (observers.addIfAbsent(observer) && initialized) {
            observer.onChanged(value);
        }
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    public void clearObservers() {
        observers.clear();
    }

    private void notifyObservers(T newValue) {
        for (Observer<T> observer : observers) {
            observer.onChanged(newValue);
        }
    }
}
