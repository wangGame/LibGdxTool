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
        Object o = instances.get(clazz);
        if (o != null) {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                instances.put(clazz, constructor.newInstance());
                return (T) constructor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Singleton create failed: " + clazz.getName(), e);
            }
        }
        return (T) instances.get(clazz);
    }
}