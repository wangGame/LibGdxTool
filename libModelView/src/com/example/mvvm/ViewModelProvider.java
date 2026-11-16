package com.example.mvvm;

import java.lang.reflect.Constructor;

/** Finds an existing ViewModel or creates and stores a new one. */
public final class ViewModelProvider {
    private static final String DEFAULT_KEY_PREFIX = "com.example.mvvm.DefaultKey:";

    public interface Factory {
        <T extends ViewModel> T create(Class<T> modelClass);
    }

    private final ViewModelStore store;
    private final Factory factory;

    public ViewModelProvider(ViewModelStore store) {
        this(store, new NewInstanceFactory());
    }

    public ViewModelProvider(ViewModelStoreOwner owner) {
        this(owner.getViewModelStore(), new NewInstanceFactory());
    }

    public ViewModelProvider(ViewModelStore store, Factory factory) {
        if (store == null || factory == null) {
            throw new NullPointerException("store and factory must not be null");
        }
        this.store = store;
        this.factory = factory;
    }

    public ViewModelProvider(ViewModelStoreOwner owner, Factory factory) {
        this(owner.getViewModelStore(), factory);
    }

    public <T extends ViewModel> T get(Class<T> modelClass) {
        String canonicalName = modelClass.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local or anonymous classes cannot be ViewModels");
        }
        return get(DEFAULT_KEY_PREFIX + canonicalName, modelClass);
    }

    public <T extends ViewModel> T get(String key, Class<T> modelClass) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("key must not be empty");
        }
        if (modelClass == null) {
            throw new NullPointerException("modelClass == null");
        }

        ViewModel existing = store.get(key);
        if (modelClass.isInstance(existing)) {
            return modelClass.cast(existing);
        }

        T created = factory.create(modelClass);
        if (created == null) {
            throw new IllegalStateException("Factory returned null for " + modelClass.getName());
        }
        store.put(key, created);
        return created;
    }

    /** Creates ViewModels with an accessible no-argument constructor. */
    public static final class NewInstanceFactory implements Factory {
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            try {
                Constructor<T> constructor = modelClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Cannot create " + modelClass.getName()
                                + ". Provide a no-argument constructor or a custom Factory.", e);
            }
        }
    }
}
