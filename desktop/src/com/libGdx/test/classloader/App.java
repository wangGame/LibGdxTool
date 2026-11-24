package com.libGdx.test.classloader;

import com.joker.discover.ClassScanner;
import com.kw.gdx.resource.AssetRes;
import com.kw.gdx.resource.annotation.AssetFlag;
import com.kw.gdx.resource.annotation.ScreenResource;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        Set<String> names = ClassScanner.scan("com.libGdx.test");
        for (String name : names) {
            try {
                Class<?> aClass = ClassScanner.loadClass(name);
                if (aClass.isAnnotationPresent(AssetFlag.class)) {
                    try {
                        AssetRes assetRes = (AssetRes) aClass.getDeclaredConstructor().newInstance();
                        assetRes.loadRes();
                        assetRes.getRes();
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
