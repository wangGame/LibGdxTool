//package com.tony.puzzle;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.utils.Array;
//import com.tony.puzzle.bean.CongfigBean;
//
//import java.lang.reflect.Field;
//import java.util.HashSet;
//
//public class WriteCsv {
//    private FileHandle out;
//    public WriteCsv(FileHandle out) {
//        this.out = out;
//    }
//
//    public void write(Array<CongfigBean> array){
//        StringBuilder builder = new StringBuilder();
//        Field[] declaredFields = CongfigBean.class.getDeclaredFields();
//        for (Field declaredField : declaredFields) {
//            String name = declaredField.getName();
//            builder.append(name);
//            builder.append(",");
//        }
//        builder.deleteCharAt(builder.length()-1);
//        builder.append("\r\n");
//        HashSet<String> hashSet = new HashSet<>();
//        Array<String> repeatString = new Array<>();
//        for (CongfigBean congfigBean : array) {
//            int id = congfigBean.getId();
//            String resourceName = congfigBean.getResourceName();
//            if (hashSet.contains(resourceName)){
//                repeatString.add(resourceName);
//               // throw new ErrorException("repeat msg name "+resourceName +"  file name"+out.name());
//            }
//            hashSet.add(resourceName);
//            String hintline = congfigBean.getHintline();
//            int direct = congfigBean.getDirect();
//            int type = congfigBean.getType();
//            builder.append(id+",");
//            builder.append(ChineseToEnglish.getPingYin(resourceName)+",");
//            builder.append(type+",");
//            builder.append(ChineseToEnglish.getPingYin(hintline)+",");
//            builder.append(direct+"\r\n");
//
//        }
//        if (repeatString.size > 0 ){
//            FileHandle local = Gdx.files.local(out.path()+"/error.txt");
//            for (String s : repeatString) {
//                local.writeString(s+"\t",true);
//
//            }
//        }
//        FileHandle local = Gdx.files.local(out.path()+"/config.csv");
//        local.writeString(builder.toString(),false);
//    }
//}
