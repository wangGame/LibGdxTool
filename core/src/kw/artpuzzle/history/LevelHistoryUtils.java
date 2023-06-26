package kw.artpuzzle.history;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.kw.gdx.utils.PythonDict;
import com.kw.gdx.utils.log.StringUtils;

import java.util.HashMap;
import java.util.HashSet;

import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.history.bean.HistoryBean;
import kw.artpuzzle.history.bean.HistoryNum;
import kw.artpuzzle.level.LevelView;

public class LevelHistoryUtils {
   public static void saveHistory(HistoryBean historyData){
      if (historyData == null)return;
      Json json = new Json();
      PythonDict root = new PythonDict();
      root.put("alreadySuccessPic",historyData.getAlreadyPic());
      root.put("cengshu",historyData.getCengshu());
      FileHandle historyFile =
              Gdx.files.local(
                      StringUtils.append(
                              LevelConfig.historyBasePath,
                              "level",
                              LevelConfig.levelNumIndex));
//      FileHandle historyFile = Gdx.files.local("level1");
      historyFile.writeString(json.prettyPrint(root), false);
   }

   public static HistoryBean readHistory(){
      FileHandle data = Gdx.files.local(
              StringUtils.append(
                      LevelConfig.historyBasePath,
                      "level",
                      LevelConfig.levelNumIndex));
//      FileHandle data = Gdx.files.local("level1");
      if (!data.exists())return null;
      try {
         String s = data.readString();
         Json json = new Json();
         PythonDict root = json.fromJson(PythonDict.class, s);
         Array<String> array = (Array<String>) root.get("alreadySuccessPic");
         float cengshu = (Float)root.get("cengshu");
         HistoryBean bean = new HistoryBean();
         bean.setCengshu((int) cengshu);
         bean.setAlreadyPic(array);
         return bean;
      }catch (Exception e){
         e.printStackTrace();
      }
      return null;
   }

   public static void deleteFile() {
      Gdx.files.local(LevelConfig.historyBasePath+"level"+ LevelConfig.levelNumIndex).delete();
      Gdx.files.local(LevelConfig.historyNumBathPath+"level"+ LevelConfig.levelNumIndex).delete();
   }

   public static void historyPercent(HistoryNum percent) {
      if (percent == null)return;
      Json json = new Json();
      PythonDict root = new PythonDict();
      root.put("allCount",percent.getAllCount());
      root.put("currentCount", percent.getCurrentCount());
      FileHandle historyFile =
              Gdx.files.local(
                      StringUtils.append(
                              LevelConfig.historyNumBathPath,
                              "level",
                              LevelConfig.levelNumIndex));
//      FileHandle historyFile = Gdx.files.local("level1");
      historyFile.writeString(json.prettyPrint(root), false);
   }


   public static HistoryNum readPercent(int level){
      FileHandle data = Gdx.files.local(
              StringUtils.append(
                      LevelConfig.historyNumBathPath,
                      "level",
                      level));
      if (!data.exists())return null;
      try {
         String s = data.readString();
         Json json = new Json();
         PythonDict root = json.fromJson(PythonDict.class, s);
         float allCount = (Float)root.get("allCount");
         float currentCount = (Float) root.get("currentCount");
         HistoryNum bean = new HistoryNum();
         bean.setCurrentCount((int)currentCount);
         bean.setAllCount((int)allCount);
         return bean;
      }catch (Exception e){
         e.printStackTrace();
      }
      return null;
   }


   public static void saveFlurryTotal(){
      FileHandle saveFlurryTotal =
              Gdx.files.local("flurryTotal");
      StringBuilder builder = new StringBuilder();
      HashSet<String> hashSet = readFlurryTotal();
      for (String s : hashSet) {
         builder.append(s+" ");
      }
      builder.append(LevelConfig.levelNum+" ");
      saveFlurryTotal.writeString(builder.toString(), false);
   }

   public static HashSet<String> readFlurryTotal(){
      FileHandle saveFlurryTotal =
              Gdx.files.local("flurryTotal");
      HashSet<String> hashSet = new HashSet<>();
      if (!saveFlurryTotal.exists())return hashSet;
      String s = saveFlurryTotal.readString();
      String[] s1 = s.split(" ");
      for (String s2 : s1) {
         hashSet.add(s2);
      }
      return hashSet;
   }


}
