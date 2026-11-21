package com.example.mvvm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Holds ViewModel instances for one owner (for example, one Game or one Screen). */
public final class ViewModelStore {
    private final Map<String, ViewModel> models = new LinkedHashMap<>();

    public synchronized ViewModel get(String key) {
        return models.get(requireKey(key));
    }

    public void put(String key, ViewModel model) {
        requireKey(key);
        if (model == null) {
            throw new NullPointerException("model == null");
        }

        ViewModel old;
        synchronized (this) {
            old = models.put(key, model);
        }
        if (old != null && old != model) {
            old.clearInternal();
        }
    }

    public synchronized Set<String> keys() {
        return new java.util.LinkedHashSet<>(models.keySet());
    }

    /** Clears the store and invokes onCleared() on every stored model. */
    public void clear() {
        List<ViewModel> oldModels;
        synchronized (this) {
            oldModels = new ArrayList<>(models.values());
            models.clear();
        }
        for (ViewModel model : oldModels) {
            model.clearInternal();
        }
    }

    private static String requireKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("key must not be empty");
        }
        return key;
    }
}
