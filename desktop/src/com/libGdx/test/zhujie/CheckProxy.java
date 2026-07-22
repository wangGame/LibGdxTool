package com.libGdx.test.zhujie;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CheckProxy {
    public static <T> T create(T target) {


        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),

                target.getClass()
                        .getInterfaces(),

                new InvocationHandler() {


                    @Override
                    public Object invoke(
                            Object proxy,
                            Method method,
                            Object[] args
                    ) throws Throwable {


                        // 获取参数注解

                        Annotation[][] annotations =
                                method.getParameterAnnotations();


                        for(int i=0;i<annotations.length;i++){


                            for(Annotation annotation:
                                    annotations[i]){


                                if(annotation instanceof IntRange){


                                    IntRange range =
                                            (IntRange) annotation;


                                    int value =
                                            (int) args[i];


                                    if(value < range.from()
                                            ||
                                            value > range.to()){


                                        throw new RuntimeException(
                                                "参数越界:"
                                                        +value
                                                        +" 范围:"
                                                        +range.from()
                                                        +"~"
                                                        +range.to()
                                        );
                                    }
                                }
                            }
                        }


                        return method.invoke(
                                target,
                                args
                        );
                    }
                }
        );
    }
}