package com.kw.gdx.singleton;


import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 感觉没啥暖用
 */
public class Singleton {

    private static final Map<Class<?>, Object> instances = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static synchronized <T> T getInstance(Class<T> clazz) {
        return (T) instances.computeIfAbsent(clazz, key -> {
            try {
                Constructor<?> constructor = key.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Singleton create failed: " + key.getName(), e);
            }
        });
    }
}