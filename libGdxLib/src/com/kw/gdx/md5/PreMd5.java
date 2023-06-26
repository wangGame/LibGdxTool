package com.kw.gdx.md5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class PreMd5 {
    public static String apkmd5 =           "cocos/md5apkold.txt";
    public static String apklocalpath =     "cocos/";
    public static void main (String[] args) throws Exception {
        File md5file = new File(apkmd5);
        FileWriter fileWriter = new FileWriter(md5file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        outMd5(bufferedWriter, new File(apklocalpath));
        bufferedWriter.close();
        fileWriter.close();
    }

    //复制整个目录
    public static void outMd5 (BufferedWriter bufferedWriter, File from){
        try {
            File[] nfs = from.listFiles();
            for (int i = 0; i < nfs.length; i++) {
                if (nfs[i].isDirectory()) {
                    outMd5(bufferedWriter, nfs[i]);
                } else {

                    String md5ByFile = TestMD5.getMd5ByFile(nfs[i]);
                    if (nfs[i].getName().equals("md5apkold.txt")||nfs[i].getName().endsWith(".csv"))continue;
                    String path = nfs[i].getPath();
                    bufferedWriter.write(path.substring(path.indexOf("\\",10)+1) + "\t" + md5ByFile + "\n");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
