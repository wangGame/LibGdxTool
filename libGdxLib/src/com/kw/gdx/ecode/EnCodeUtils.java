package com.kw.gdx.ecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/6 19:46
 */
public class EnCodeUtils {
    public void ecodeBytes(
            byte[] bytes,
            byte code
    ){
        for (int r = 0; r < bytes.length; r++) {
            bytes[r] = (byte) (bytes[r] ^ code);
        }
    }

    public void dcodeBytes(
            byte[] bytes,
            byte dcode
    ){
        for (int r = 0; r < bytes.length; r++) {
            bytes[r] = (byte) (bytes[r] ^ dcode);
        }
    }

    private static void andOperationFile(String inputFile,String outputFile) {
        try (FileOutputStream outStream = new FileOutputStream(inputFile);
             FileInputStream stream = new FileInputStream(outputFile);){
            int c = -1;
            while ((c = stream.read()) != -1) {
                outStream.write(c^25);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
