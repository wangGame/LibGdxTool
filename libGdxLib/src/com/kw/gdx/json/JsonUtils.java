package com.kw.gdx.json;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.kw.gdx.utils.PythonDict;

/**
 * read json
 */
public class JsonUtils {
    public static PythonDict readJsonByDict(String filePath, Files.FileType type){
        Json json = new Json();
        String data = "";
        if (type == Files.FileType.Classpath){
            data = Gdx.files.classpath(filePath).readString();
        }else if (type == Files.FileType.Internal){
            data = Gdx.files.classpath(filePath).readString();
        }else if (type == Files.FileType.External){
            data = Gdx.files.classpath(filePath).readString();
        }else if (type == Files.FileType.Absolute){
            data = Gdx.files.classpath(filePath).readString();
        }else if (type == Files.FileType.Local){
            data = Gdx.files.classpath(filePath).readString();
        }
        PythonDict root = json.fromJson(PythonDict.class, data);
        return root;
    }

    public static <T> T readjson( Class<T> tClass,String data){
        Json js = new Json();
//        T tl = (T) js.fromJson(tClass,"{\"popNum\":25,\"speed\":22,\"num\":60,\"pos\":[{\"x\":686,\"y\":354}],\"shootType\":0,\"ballNum\":3,\"levelType\":2,\"time\":0,\"star1Score\":300,\"star2Score\":600,\"star3Score\":900,\"scoreTarget\":0,\"bossTargetId\":0,\"energyMax\":0,\"stoneNum\":0,\"stoneIsTarget\":false,\"iceNum\":0,\"iceIsTarget\":false,\"cageNum\":0,\"cageIsTarget\":false,\"coverNum\":0,\"coverIsTarget\":false,\"birdNum\":0,\"birdIsTarget\":false,\"keyNum\":0,\"keyIsTarget\":false,\"lavaNum\":0,\"lavaIsTarget\":false,\"tricolorNum\":0,\"tricolorIsTarget\":false,\"coinNum\":0,\"coinMultiple\":0,\"butterFlyNum\":0,\"butterFlyIsTarget\":false,\"discolorNum\":0,\"discolorTime\":0,\"lightningNum\":0,\"blueStoneNum\":0,\"blueStoneIsTarget\":false,\"beeNum\":0,\"beeIsTarget\":false,\"spiderNum\":0,\"spiderIsTarget\":false,\"shellNum\":0,\"shellIsTarget\":false,\"shellTargetCount\":0,\"woodNum\":0,\"collectId\":0,\"collectProbability\":[100],\"ballPRs\":[100,90,60,20,0],\"isDifficult\":false,\"rail1\":[{\"x\":260,\"y\":657},{\"x\":324,\"y\":655},{\"x\":463,\"y\":655},{\"x\":738,\"y\":658},{\"x\":939,\"y\":639},{\"x\":1044,\"y\":557},{\"x\":1086,\"y\":446},{\"x\":1083,\"y\":298},{\"x\":1010,\"y\":171},{\"x\":871,\"y\":94},{\"x\":592,\"y\":78},{\"x\":360,\"y\":135},{\"x\":262,\"y\":275},{\"x\":270,\"y\":440},{\"x\":355,\"y\":533},{\"x\":529,\"y\":565},{\"x\":754,\"y\":562},{\"x\":903,\"y\":531},{\"x\":964,\"y\":439},{\"x\":958,\"y\":283},{\"x\":859,\"y\":186},{\"x\":612,\"y\":167},{\"x\":440,\"y\":222},{\"x\":385,\"y\":340},{\"x\":426,\"y\":405}]}");
        T tl = (T) js.fromJson(tClass,data);
        return tl;
    }
}
