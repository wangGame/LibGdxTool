package com.example.mvvm;

/**
 * Platform-neutral base class for UI state holders.
 * The owner of its ViewModelStore decides when this object is cleared.
 */
public abstract class ViewModel {
    private boolean cleared;

    /** Called once when the owning ViewModelStore is cleared or this entry is replaced. */
    protected void onCleared() {
        // Override to release observers, subscriptions, and other resources.
    }

    final void clearInternal() {
        synchronized (this) {
            if (cleared) {
                return;
            }
            cleared = true;
        }
        onCleared();
    }

    public final synchronized boolean isCleared() {
        return cleared;
    }
}
