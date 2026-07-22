package com.libGdx.test.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public final class MethodTimeAspect {

    @Around(
            "execution(@com.example.methodtime.MethodTime * *(..))"
                    + " && @annotation(methodTime)"
    )
    public Object measureMethod(
            ProceedingJoinPoint joinPoint,
            MethodTime methodTime
    ) throws Throwable {

        long startNanos = System.nanoTime();

        boolean success = false;
        Throwable failure = null;

        try {
            Object result = joinPoint.proceed();
            success = true;
            return result;
        } catch (Throwable throwable) {
            failure = throwable;
            throw throwable;
        } finally {
            long durationNanos =
                    System.nanoTime() - startNanos;

            MethodSignature signature =
                    (MethodSignature) joinPoint.getSignature();

            MethodTimeReporter.report(
                    methodTime.value(),
                    signature.getDeclaringTypeName(),
                    signature.getName(),
                    durationNanos,
                    methodTime.thresholdMs(),
                    success,
                    failure
            );
        }
    }
}