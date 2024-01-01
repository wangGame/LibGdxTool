package com.kw.gdx.encryption;

/**
 * @Auther jian xian si qi
 * @Date 2024/1/1 22:20
 */
public class Encryption {
    public static String getEncryption(String para,int key){
        char[] charArray = para.toCharArray();
        for(int i =0;i<charArray.length;i++){
            charArray[i]=(char)(charArray[i]^key);
        }
        return String.valueOf(charArray);
    }
}
