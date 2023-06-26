package com.kw.gdx.resource.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AnnotationInfo {

    /**
     * 检测 类是否存在此注解
     */
    public static <A extends Annotation> A checkClassAnnotation(Object object, Class clazz){
        if (object.getClass().isAnnotationPresent(clazz)) {
            return (A) object.getClass().getAnnotation(clazz);
        }
        return null;
    }

    /**
     * 检测字段是否存在注解
     * @param object
     * @param clazz
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A checkFeildAnnotation(Field object, Class clazz){
        if (object.isAnnotationPresent(clazz)) {
            return (A) object.getAnnotation(clazz);
        }
        return null;
    }

    /**
     * 检测 方法是不是存在注解
     * @param object
     * @param clazz
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A checkMethodAnnotation(Method object, Class clazz){
        if (object.isAnnotationPresent(clazz)) {
            return (A) object.getAnnotation(clazz);
        }
        return null;
    }
}
