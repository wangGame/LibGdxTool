package com.kw.gdx.utils;

import com.badlogic.gdx.files.FileHandle;

/**
 * 图片加解密工具
 */
public class ImageUtil {

    /**
     * 解密
     *
     * @param sourcePath
     * @param desPath
     * @return
     */
    public static boolean decrypt(String sourcePath, String desPath) {
        byte[] bytes = FileDecodeUtil.getBytes("data/password.txt");
        String s = new String(bytes);
        int mask = 0;
        int start = 0;
        int length = 0;
        if (s!=null){
            String[] s1 = s.split(" ");
            if (s1.length>2){
                mask = ConvertUtil.convertToInt(s1[0],0);
                start = ConvertUtil.convertToInt(s1[1],0);
                length = ConvertUtil.convertToInt(s1[2],0);
            }
        }

        FileHandle fileHandle = new FileHandle(sourcePath);
        byte[] bt = fileHandle.readBytes();
        if (bt != null && bt.length > start) {
            //进行遍历加密
            for (int i = start; i < start + length; i++) {//从中间异或运算
                bt[i] = (byte) (bt[i] ^ (int) mask);  //进行异或运算
            }
        }
        FileHandle fileHandle1 = new FileHandle(desPath);
        fileHandle1.writeBytes(bt,false);
        return true;
    }


    public static boolean decrypt(byte[] bt) {
        byte[] bytes = FileDecodeUtil.getBytes("key/password.txt");
        String s = new String(bytes);
        int mask = 0;
        int start = 0;
        int length = 0;
        if (s!=null){
            String[] s1 = s.split(" ");
            if (s1.length>2){
                mask = ConvertUtil.convertToInt(s1[0],0);
                start = ConvertUtil.convertToInt(s1[1],0);
                length = ConvertUtil.convertToInt(s1[2],0);
            }
        }

        if (bt != null && bt.length > start) {
            //进行遍历加密
            for (int i = start; i < start + length; i++) {//从中间异或运算
                bt[i] = (byte) (bt[i] ^ (int) mask);  //进行异或运算
            }
        }
        return true;
    }
}