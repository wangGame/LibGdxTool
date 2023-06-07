package com.kw.gdx.emo;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/6 19:20
 */
public class EmoUtils {
    public static String[] active = {
            getEmo(0x1F5E3)+"Test emo"+getEmo(0x1F334)
    };

    private static String getEmo(int code) {
        char[] chars = Character.toChars(code);
        return new String(chars);
    }

    public static void main(String[] args) {
        for (String s : active) {
            System.out.println(s);
        }
    }
}
