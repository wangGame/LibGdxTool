package com.example.mvvm;

/** Receives values published by MutableLiveData. */
public interface Observer<T> {
    void onChanged(T value);
}
