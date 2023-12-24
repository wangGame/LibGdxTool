//package com.tony.puzzle;
//
//import com.kw.gdx.zip.PackZip;
//
//import kw.artpuzzle.constant.LevelConfig;
//
///**
// * origin  原始数据
// * genMd5Dir  生成md5
// * mdZip  打包zip
// *
// * downLoadFile  下载目录
// * temp 复制到另一个目录
// * unzipFile  解压
// * checkOut  检查完复制到这个目录
// * 复制到目标
// *
// *
// */
//public class GameLevelUtils {
//    public static void main(String[] args) {
//        PackZip zip = new PackZip();
//        //原始 目录资源复制到genMd5目录
//
//
//        zip.copyOutToTemp("origin/","genMd5Dir/");
//
//        //生成md5
//        zip.genMd5("genMd5Dir/");
//        //打包
//        zip.step3("genMd5Dir/","mdZip/");
//        zip.delete("genMd5Dir");
//        //下载
//        //复制到temp目录
////        zip.copyOutToTemp("mdZip/","temp/");
////        zip.delete("mdZip");
//        //解压
////        zip.unpackZip("temp/","unzipFile/");
////        zip.delete("temp");
//////        //检查
////        for (int i = 1; i <= 2; i++) {
////            zip.check("unzipFile/level"+i);
////        }
//////        //复制到目标目录
////        zip.copyOutToTemp("unzipFile/","level/");
////        zip.delete("unzipFile");
//    }
//}
