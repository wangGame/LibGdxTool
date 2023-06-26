package com.kw.gdx.resource.csvanddata;

import com.kw.gdx.resource.annotation.AnnotationInfo;

import java.lang.reflect.Field;

public class Interpreter {
    public static String iteratorAnnotations(Field field) {
        Value annotation = AnnotationInfo.checkFeildAnnotation(field, Value.class);
        return annotation == null ? field.getName() : annotation.value();
    }
}