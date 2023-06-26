package kw.test.file;

import com.badlogic.gdx.files.FileHandle;
import com.kw.gdx.resource.csvanddata.ConvertUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReadFileConfig {

    public Bean getValue(){
        try {
            HashMap<String,String> kv = new HashMap<>();
            FileHandle fileHandle = new FileHandle("../gameconfig.txt");
            if (!fileHandle.exists()) {
//                width : 360
//                height : 640
//                version : v1.0
//                appName : Art Puzzle

                fileHandle.writeString("width : ",true);
                fileHandle.writeString("360",true);
                fileHandle.writeString("\n",true);
                fileHandle.writeString("height : ",true);
                fileHandle.writeString("640",true);
                fileHandle.writeString("\n",true);
                fileHandle.writeString("version : ",true);
                fileHandle.writeString("v1.0",true);
                fileHandle.writeString("\n",true);
                fileHandle.writeString("appName : ",true);
                fileHandle.writeString("Art Puzzle",true);
                fileHandle.writeString("\n",true);
            }


            String s = fileHandle.readString();
            String[] split = s.split("\n");
            for (String s1 : split) {
                String[] split1 = s1.split(":");
                if (split1.length>1) {
                    kv.put(split1[0].trim(),split1[1]);
                }
            }

            Class<Bean> beanClass = Bean.class;
            Bean bean = beanClass.newInstance();
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                mathodSetValue(kv.get(declaredField.getName()),bean,declaredField);
            }
//            Field width = bean.getClass().getDeclaredField("width");
//            width.setAccessible(true);
//            width.set(bean,1);
            return bean;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean feildBoolean(Field field, String type){
        return field.getGenericType().toString().equals(type);
    }

    private void mathodSetValue(String value, Object o, Field declaredField) {
        try {

            declaredField.setAccessible(true);
            if (feildBoolean(declaredField,"byte")) {
                declaredField.set(o,ConvertUtil.convertToByte(value,(byte) 0));
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
}
