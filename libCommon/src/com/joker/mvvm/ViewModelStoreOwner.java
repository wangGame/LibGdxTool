package com.joker.mvvm;

/** Implemented by the object that owns a ViewModelStore. */
public interface ViewModelStoreOwner {
    ViewModelStore getViewModelStore();
}
