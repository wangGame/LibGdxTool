package com.kw.gdx.refleat;

import com.kw.gdx.resource.csvanddata.ConvertUtil;

import java.lang.reflect.Field;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/6 19:25
 */
public class ClassUtils {
    private Class<Object> beanClass;
    public ClassUtils(Class clazz){
        this.beanClass = clazz;
//        Object bean = null;
//        try {
//            bean = beanClass.newInstance();
//            Field[] declaredFields = bean.getClass().getDeclaredFields();
//            for (Field declaredField : declaredFields) {
//                mathodSetValue(kv.get(declaredField.getName()),bean,declaredField);
//            }
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return bean;
    }

    public Object getInstance(){
        if (beanClass != null){
            try {
                return beanClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static boolean feildBoolean(Field field, String type){
        return field.getGenericType().toString().equals(type);
    }

    private static void mathodSetValue(String value, Object o, Field declaredField) {
        try {
            declaredField.setAccessible(true);
            if (feildBoolean(declaredField,"byte")) {
                declaredField.set(o, ConvertUtil.convertToByte(value,(byte) 0));
            }else if (feildBoolean(declaredField,"int")){
                declaredField.set(o,ConvertUtil.convertToInt(value,0));
            }else if (feildBoolean(declaredField,"float")){
                declaredField.set(o,ConvertUtil.convertToFloat(value,0F));
            }else if (feildBoolean(declaredField,"double")){
                declaredField.set(o,ConvertUtil.convertToFloat(value,0));
            }else if (feildBoolean(declaredField,"long")){
                declaredField.set(o,ConvertUtil.convertToLong(value,0L));
            }else if (feildBoolean(declaredField,"boolean")){
                declaredField.set(o,ConvertUtil.convertToBoolean(value,false));
            }else {
                declaredField.set(o,value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static Object mathodGetValue(Object o, Field declaredField) {
        Object value = null;
        try {
            declaredField.setAccessible(true);
            if (feildBoolean(declaredField,"byte")) {
                value = declaredField.getByte(o);
            }else if (feildBoolean(declaredField,"int")){
                value = declaredField.getInt(o);
            }else if (feildBoolean(declaredField,"float")){
                value = declaredField.getFloat(o);
            }else if (feildBoolean(declaredField,"double")){
                value = declaredField.getDouble(o);
            }else if (feildBoolean(declaredField,"long")){
                value = declaredField.getLong(o);
            }else if (feildBoolean(declaredField,"boolean")){
                value = declaredField.getBoolean(o);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }
}
