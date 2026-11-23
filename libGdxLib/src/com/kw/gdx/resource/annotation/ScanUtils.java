package com.kw.gdx.resource.annotation;

import com.joker.discover.ClassScanner;
import com.kw.gdx.resource.AssetRes;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * 注解扫描
 */
public class ScanUtils {
    /**
     *
     * @param packageName 扫描的包
     * @param clazz 注解名称
     */
    public static void scanAndLoad(String packageName,Class clazz){
        Set<String> names = ClassScanner.scan(packageName);
        for (String name : names) {
            try {
                Class<?> aClass = ClassScanner.loadClass(name);
                if (aClass.isAnnotationPresent(clazz)) {
                    try {
                        AssetRes assetRes = (AssetRes) aClass.getDeclaredConstructor().newInstance();
                        assetRes.loadRes();
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void scanAndGet(String packageName,Class clazz){
        Set<String> names = ClassScanner.scan(packageName);
        for (String name : names) {
            try {
                Class<?> aClass = ClassScanner.loadClass(name);
                if (aClass.isAnnotationPresent(clazz)) {
                    try {
                        AssetRes assetRes = (AssetRes) aClass.getDeclaredConstructor().newInstance();
                        assetRes.loadRes();
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}