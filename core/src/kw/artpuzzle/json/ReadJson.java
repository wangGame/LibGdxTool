package kw.artpuzzle.json;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import kw.artpuzzle.constant.LevelConfig;

public class ReadJson {
    public ArrayMap<String, TextureInfo> readFile(String jsonFileName){
        FileHandle fileHandle = Gdx.files.internal(jsonFileName);
        if (!LevelConfig.useInocal) {
            fileHandle = Gdx.files.local(jsonFileName);
        }
        if (LevelConfig.levelNum == 1){
            fileHandle = Gdx.files.internal(jsonFileName);
        }
        JsonValue root = new JsonReader().parse(fileHandle);
        ArrayMap<String,TextureInfo> textureInfoHashMap = new ArrayMap<>();
        for (int i = 0; i < root.size; i++) {
            JsonValue jsonValue = root.get(i);
            String name = jsonValue.name;
            float x = jsonValue.get("x").asFloat();
            float y = jsonValue.get("y").asFloat();
            float width = jsonValue.get("width").asFloat();
            float height = jsonValue.get("height").asFloat();
            TextureInfo info = new TextureInfo();
            info.setX(x);
            info.setY(y);
            info.setWidth(width);
            info.setHeight(height);
            textureInfoHashMap.put(name,info);
        }
        return textureInfoHashMap;
    }

    public void deal(String jsonFileName,String out){
        HashSet<String> hashSert  = new HashSet<>();
        FileHandle fileHandle = new FileHandle(jsonFileName);
        JsonValue root = new JsonReader().parse(fileHandle);
        ArrayMap<String,TextureInfo> textureInfoHashMap = new ArrayMap<>();
        root.remove("skeleton");
        root.remove("bones");
        root.remove("slots");
        root.remove("animations");
        JsonValue skins = root.get("skins");
        JsonValue aDefault = skins.get("default");
//        FileHandle fileHandle1 = Gdx.files.internal("level1/level1/temp.json");
        FileHandle internal = new FileHandle("out/"+out+"/layout.json");
        FileHandle filename = new FileHandle("out/"+out+"/fileName.txt");
        if (internal.exists()) {
            internal.delete();
        }
        if (filename.exists()) {
            filename.delete();
        }
        internal.writeString("{",true);
        ArrayMap<String,Array> arrayArrayMap = new ArrayMap<>();
        for (int i = 0; i < aDefault.size; i++) {
            JsonValue jsonValue = aDefault.get(aDefault.size - 1 - i);
            Array<String> array=null;
            if (jsonValue.name.startsWith("1")){
                if (arrayArrayMap.containsKey("xg1")) {
                    array = arrayArrayMap.get("xg1");
                }else {
                    array = new Array<>();
                    arrayArrayMap.put("xg1",array);
                }
            }else if (jsonValue.name.startsWith("2")){
                if (arrayArrayMap.containsKey("xg2")) {
                    array = arrayArrayMap.get("xg2");
                }else {
                    array = new Array<>();
                    arrayArrayMap.put("xg2",array);
                }
            }else if (jsonValue.name.startsWith("3")){
                if (arrayArrayMap.containsKey("xg3")) {
                    array = arrayArrayMap.get("xg3");
                }else {
                    array = new Array<>();
                    arrayArrayMap.put("xg3",array);
                }
            }else if (jsonValue.name.startsWith("4")){
                if (arrayArrayMap.containsKey("xg4")) {
                    array = arrayArrayMap.get("xg4");
                }else {
                    array = new Array<>();
                    arrayArrayMap.put("xg4",array);
                }
            }else if (jsonValue.name.startsWith("5")){
                if (arrayArrayMap.containsKey("xg5")) {
                    array = arrayArrayMap.get("xg5");
                }else {
                    array = new Array<>();
                    arrayArrayMap.put("xg5",array);
                }
            }
            if (array!=null) {
                if (hashSert.contains(jsonValue.name)){
                    throw new RuntimeException("name chong fu !");
                }
                hashSert.add(jsonValue.name);
                array.add(jsonValue.name);
            }
            filename.writeString(jsonValue.name,true);
            filename.writeString("\n",true);
            if (i == aDefault.size-1) {
                internal.writeString(jsonValue.get(jsonValue.name).toString(), true);
            }else {
                internal.writeString(jsonValue.get(jsonValue.name).toString()+",", true);
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append("id"+",");
        builder.append("resourceName"+",");
        builder.append("type"+",");
        builder.append("hintline"+",");
        builder.append("direct"+",");
        builder.append("sort"+",");
        builder.append("\n");

//        id,resourceName,type,hintline,direct,sort
        int index = 0;
        int type = 0;
        for (int i1 = 1; i1 <= arrayArrayMap.size; i1++) {
            String key = "xg" + i1;
            Array value = arrayArrayMap.get(key);
            type ++;
            for (int i = 0; i < value.size; i++) {
                index ++;
                builder.append(index+",");
                builder.append(value.get(i)+",");
                builder.append(type+",");
                builder.append(key+",");
                builder.append(""+",");
                builder.append(""+",");
                builder.append("\n");
            }
        }
        FileHandle csvTest = new FileHandle("out/"+out+"/layout.csv");
        csvTest.writeString(builder.toString(),false);
        internal.writeString("}",true);
    }
}
