package com.tony.puzzle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class PicEncoder {
    public static void main(String[] args) {
//        yihuojiamijiemi();

        jiami();
    }

    private static void jiami() {
        try (FileOutputStream outStream = new FileOutputStream(new File("GirlToyShop.png"));
             FileInputStream stream = new FileInputStream(new File("Out_GirlToyShop.png"));){
            int c = -1;
            for (int i = 0; i < 10; i++) {
//                stream.read();
            }
            while ((c = stream.read()) != -1) {
                outStream.write(c^25);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void yihuojiamijiemi() {
        try (FileOutputStream outStream = new FileOutputStream(new File("out/level/testOut.png"));
             FileInputStream stream = new FileInputStream(new File("out/level/atlasBottom.png"));){
            int c = -1;
            while ((c = stream.read()) != -1) {
                outStream.write(c^25);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void print(int num) {
        System.out.println(Integer.toBinaryString(num));
    }
}
