package com.kw.gdx.resource.csvanddata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.refleat.ClassUtils;

import java.lang.reflect.Field;
import java.util.HashSet;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/6 19:33
 */
public class WriteCsv {
    private FileHandle out;
    public WriteCsv(FileHandle out) {
        this.out = out;
    }

    public void write(Array array,Class object){
        StringBuilder builder = new StringBuilder();
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String name = declaredField.getName();
            builder.append(name);
            builder.append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("\r\n");
        Array<String> repeatString = new Array<>();
        for (Object data : array) {
            Field[] declaredFields1 = data.getClass().getDeclaredFields();
            for (Field field : declaredFields1) {
                try {
                    // 设置可访问性
                    ClassUtils.mathodGetValue(data,field);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (repeatString.size > 0 ){
            FileHandle local = Gdx.files.local(out.path()+"/error.txt");
            for (String s : repeatString) {
                local.writeString(s+"\t",true);

            }
        }
        FileHandle local = Gdx.files.local(out.path()+"/config.csv");
        local.writeString(builder.toString(),false);
    }
}
