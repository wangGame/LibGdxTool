package com.libGdx.test.pic;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class App {


    // 等价于 StringLiteral_7979
    private static final String PREFIX = "你的固定前缀字符串";

    public static String calcHash(String textureName) {
        try {
            // 1. 拼接字符串
//            String input = "gamincat202530_21_146";
            String input = "gamincat20252_0_heritage5";


            // 2. ASCII 编码
            byte[] bytes = input.getBytes(StandardCharsets.US_ASCII);

            // 3. MD5
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(bytes);

            // 4. 转 16 进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            System.out.println(sb);
            // 5. 取前 8 位
            return sb.substring(0, 8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(calcHash("chen"));
    }
}
