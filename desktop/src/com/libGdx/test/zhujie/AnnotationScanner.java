package com.libGdx.test.zhujie;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationScanner {
    public static void check(Object obj)
            throws Exception {
        Class<?> clazz = obj.getClass();
        // 获取所有方法
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods){
            // 判断方法有没有@Check
            if(!method.isAnnotationPresent(Check.class)){
                continue;
            }
            System.out.println(
                    "发现方法:"
                            + method.getName()
            );


            // 获取参数注解

            Annotation[][] annotations =
                    method.getParameterAnnotations();


            for(int i=0;i<annotations.length;i++){


                for(Annotation annotation:
                        annotations[i]){


                    if(annotation instanceof IntRange){


                        IntRange range =
                                (IntRange) annotation;


                        System.out.println(
                                "参数"+i+
                                        "范围:"
                                        +range.from()
                                        +"~"
                                        +range.to()
                        );

                    }
                }
            }
        }
    }
}