package com.kw.gdx.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.io.File;

public class JsonUtils {
    private JsonUtils() {
        throw new RuntimeException();
    }
    public static<T> void save(String jsonPath,T t){
        Json json = new Json();
        // 创建一个 Player 对象并序列化为 JSON 字符串
        String jsonString = json.toJson(t);
        // 反序列化 JSON 字符串为 Player 对象
        FileHandle file = new FileHandle(jsonPath);
//        FileHandle file = Gdx.files.local(jsonPath);
        // 存储
        file.writeString(jsonString, false); // 写入文件
    }

    public static<T> T read(String jsonPath,T t){
        Json json = new Json();
        // 反序列化 JSON 字符串为 Player 对象
//        FileHandle file = Gdx.files.local(jsonPath);
        FileHandle file = new FileHandle(jsonPath);
        if (file == null)return null;
        if (!file.exists())return null;
        // 从文件读取
        String loadedJson = file.readString();
        return (T) json.fromJson(t.getClass(), loadedJson);
    }

    public static void delete(String jsonPath){
        FileHandle file = new FileHandle(jsonPath);
        file.delete();
    }
}
