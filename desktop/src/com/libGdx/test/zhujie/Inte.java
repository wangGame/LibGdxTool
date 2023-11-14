package com.libGdx.test.zhujie;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @Auther jian xian si qi
 * @Date 2023/11/14 11:06
 */
public class Inte {
    public static void check(Class clazz){
        try {
            Method method = clazz.getMethod("setAl", int.class); // 获取方法名为myMethod，参数类型为double的方法
            Parameter parameter1 = method.getParameters()[0]; // 获取方法的第一个参数
            Annotation[] annotations = parameter1.getAnnotations(); // 获取参数上的所有注解

            for (Annotation annotation : annotations) {
                if (annotation instanceof IntRange) {
                    IntRange rangeAnnotation = (IntRange) annotation;
                    double min = rangeAnnotation.from();
                    double max = rangeAnnotation.to();

                    System.out.println("Parameter range: " + min + " to " + max);
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
