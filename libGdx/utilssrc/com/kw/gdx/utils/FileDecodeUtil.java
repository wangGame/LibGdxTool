package com.kw.gdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class FileDecodeUtil {
   private static byte[] key;

   public static InputStream read(String name) throws FileNotFoundException {
      if (key == null) {
         InputStream is = new FileInputStream("data/data_00.var");
         byte[] buffer = new byte[1024];
         try {
            int length = is.read(buffer);
            key = new byte[length];
            for (int i = 0; i < length; i++) {
               key[i] = buffer[i];
            }
            is.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      try {

         // DES算法要求有一个可信任的随机数源
         SecureRandom sr = new SecureRandom();
         // 创建一个 DESKeySpec 对象,指定一个 DES 密钥
         DESKeySpec ks = new DESKeySpec(key);
         // 生成指定秘密密钥算法的 SecretKeyFactory 对象。
         SecretKeyFactory factroy = SecretKeyFactory.getInstance("DES");
         // 根据提供的密钥规范（密钥材料）生成 SecretKey 对象,利用密钥工厂把DESKeySpec转换成一个SecretKey对象
         SecretKey sk = factroy.generateSecret(ks);
         // 生成一个实现指定转换的 Cipher 对象。Cipher对象实际完成加解密操作
         Cipher c = Cipher.getInstance("DES");
         // 用密钥和随机源初始化此 cipher
         c.init(Cipher.DECRYPT_MODE, sk, sr);
         byte[] buffer = new byte[1024];
         InputStream in =  new FileHandle(name).read();
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         CipherOutputStream cout = new CipherOutputStream(out, c);
         int i;
         while (-1!=(i = in.read(buffer))) {
            cout.write(buffer, 0, i);
         }
         cout.flush();
         byte data[] = out.toByteArray();
         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
         cout.close();
         in.close();
         return byteArrayInputStream;
      }catch (Exception e){
         e.printStackTrace();
      }

      return null;

   }

   public static byte[] getBytes(String name) {
      FileHandle internal = Gdx.files.internal("key/data_00.var");
      if (internal==null)return new byte[100];
      if (key == null) {
         InputStream is = internal.read();
         byte[] buffer = new byte[1024];
         try {
            int length = is.read(buffer);
            key = new byte[length];
            for (int i = 0; i < length; i++) {
               key[i] = buffer[i];
            }
            is.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      try {
         byte[] buffer = new byte[1024];
         InputStream in =  Gdx.files.internal(name).read();
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         int i;
         while (-1!=(i = in.read(buffer))) {
            out.write(buffer, 0, i);
         }
         byte data[] = out.toByteArray();
         // DES算法要求有一个可信任的随机数源
         SecureRandom sr = new SecureRandom();
         // 创建一个 DESKeySpec 对象,指定一个 DES 密钥
         DESKeySpec ks = new DESKeySpec(key);
         // 生成指定秘密密钥算法的 SecretKeyFactory 对象。
         SecretKeyFactory factroy = SecretKeyFactory.getInstance("DES");
         // 根据提供的密钥规范（密钥材料）生成 SecretKey 对象,利用密钥工厂把DESKeySpec转换成一个SecretKey对象
         SecretKey sk = factroy.generateSecret(ks);
         // 生成一个实现指定转换的 Cipher 对象。Cipher对象实际完成加解密操作
         Cipher c = Cipher.getInstance("DES");
         // 用密钥和随机源初始化此 cipher
         c.init(Cipher.DECRYPT_MODE, sk, sr);
         in.close();
         return c.doFinal(data);
      }catch (Exception e){
         e.printStackTrace();
      }
      return null;
   }

}
