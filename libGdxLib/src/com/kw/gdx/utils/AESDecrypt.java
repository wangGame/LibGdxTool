package com.kw.gdx.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESDecrypt {
    private static final String AES_KEY_BASE64 =
            "bHmJQ0X7EixyOe+LuW8ShBaXz1S4cNxL+Ebe3rQKHlA=";

    /**
     * AES-CBC 解密
     */
    public static String decryptImageUrl(String encryptedBase64) throws Exception {

        // Base64 解码 AES Key
        byte[] key = Base64.getDecoder().decode(AES_KEY_BASE64);

        // Base64 解码密文
        byte[] raw = Base64.getDecoder().decode(encryptedBase64);

        // 前 16 字节是 IV
        byte[] iv = new byte[16];
        System.arraycopy(raw, 0, iv, 0, 16);

        // 剩余部分是 AES 密文
        byte[] cipherText = new byte[raw.length - 16];
        System.arraycopy(raw, 16, cipherText, 0, cipherText.length);

        // AES/CBC/PKCS5Padding
        // Java 的 PKCS5Padding 对 AES 实际上就是 PKCS7 padding 的效果
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] plain = cipher.doFinal(cipherText);

        return new String(plain, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {

        String encrypted =
                "YkhtSlEwWDdFaXh5YkhtSiDleaT/mWkPNrTK09te8qDPte6ha7GLBfuf+NGV52rbRpnAZ7spTXUX7G6N0ma54eTqZgPC4otiG6TVwJxnpSTdav/J2S9S5ybFqn7/1nQr";

        String result = decryptImageUrl(encrypted);

        System.out.println(result);
    }

}
