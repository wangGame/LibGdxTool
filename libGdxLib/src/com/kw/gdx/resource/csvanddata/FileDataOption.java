//package com.kw.gdx.resource.csvanddata;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.utils.ArrayMap;
//
//import java.util.HashMap;
//
//public class FileDataOption {
//    private boolean alreadyUpdata = false;
//    public static FileDataOption instance;
//    private ArrayMap<String,String> arrayMap = new ArrayMap<>();
//    private int starNum;
//
//    private FileDataOption(){}
//
//    static {
//        instance = new FileDataOption();
//    }
//
//    public int getStarNum() {
//        return starNum;
//    }
//
//    public static FileDataOption getInstance() {
//        if (instance == null){
//            instance = new FileDataOption();
//        }
//        return instance;
//    }
//
//    public void saveLevelStar(String level, int starNum, String useTime){
//        this.alreadyUpdata = false;
//        arrayMap.put(level,starNum+"="+useTime);
//        String filename = "levelstar.txt";
//        FileHandle internal = Gdx.files.local(filename);
//        internal.writeString(arrayMap.toString(),false);
//        readLevelStar();
//    }
//
//    private HashMap<String,Integer> startMap = new HashMap<>();
//    private HashMap<String,Integer> everyLevelStar = new HashMap<>();
//
//    public void readLevelStar(){
//        this.alreadyUpdata = true;
//        starNum = 0;
//        arrayMap.clear();
//        startMap.clear();
//        String filename = "levelstar.txt";
//        FileHandle internal = Gdx.files.local(filename);
//        if (!internal.exists())return;
//        String string = internal.readString();
//        if (string.length()>2) {
//            String replace = string.replace("{", "");
//            replace = replace.replace("}", "");
//            replace = replace.replace(",", "");
//            String[] split = replace.split(" ");
//            for (String s : split) {
//                String[] split1 = s.split("=");
//                if (split1.length==3) {
//                    arrayMap.put(split1[0], split1[1]+"="+split1[2]);
//                    String sl0 = split1[0];
//                    String[] split2 = sl0.split("-");
//                    if (split2.length==2){
//                        String s1 = split2[0];
//                        int ss = ConvertUtil.convertToInt(split1[1],0);
//                        int num = 0;
//                        if (startMap.containsKey(s1)){
//                            num = startMap.get(s1);
//                        }
//                        startMap.put(s1,num+ss);
//                    }
//                }
//            }
//        }
//
//        for (String s : startMap.keySet()) {
//            Integer integer = startMap.get(s);
//            starNum += integer;
//        }
//    }
//
//    public HashMap<String, Integer> getStartMap() {
//        return startMap;
//    }
//
//    public ArrayMap<String, String> getArrayMap() {
//        if (!alreadyUpdata){
//            readLevelStar();
//        }
//        return arrayMap;
//    }
//
////
////    public void setInfo(String key) {
////        if (arrayMap==null)getArrayMap();
////        String info = arrayMap.get(key);
//////        System.out.println("----------------"+key);
////        for (int i = 1; i <= 3; i++) {
////            findActor("star_"+i).setVisible(false);
//////            findActor("star_"+i+"_0").setVisible(true);
////        }
////        if (info == null){
////            useTimeLabel.setText(TimeUtils.formatTime(ConvertUtil.convertToFloat(0,0F)));
////        }else {
////            String[] split = info.split("=");
////            if (split.length == 2) {
////                int starNum = ConvertUtil.convertToInt(split[0], 0);
////
////                int i =0;
////                for (i = 1; i <= starNum; i++) {
////                    findActor("star_"+i).setVisible(true);
//////                    findActor("star_"+i+"_0").setVisible(false);
////                }
//////                TimeUtils.formatTime(useTime)
////                useTimeLabel.setText(TimeUtils.formatTime(ConvertUtil.convertToFloat(split[1],0F)));
////            }
////        }
////    }
//}
