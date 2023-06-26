package com.tony.puzzle;

import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * try catch 会在生命周期结束的时候
 */
public class App {
    public static void main(String[] args) {
//        App app = new App();
//        app.test();

//        for (int i = 46; i <= 46; i++) {
//            File file = new File("level"+i);
//            System.out.println(file.mkdirs());
//            File file1 = new File("level"+i+"-1");
//            System.out.println(file1.mkdirs());
//        }

        for (int i = 26; i <= 46; i++) {
            FileHandle file = new FileHandle("out/level" +i);
            file.mkdirs();
            for (FileHandle fileHandle : file.list()) {
                if (fileHandle.name().endsWith(".json")){
                    FileHandle fileHandle1 = new FileHandle("../../json/"+file.name()+"/layout.json");
                    fileHandle.copyTo(fileHandle1);
                    break;
                }
            }
            File file1 = new File("out/level" +i+"-1");
        }

    }

    public void test(){
        FileInputStream stream = getStream();
        try {
            int read = stream.read();
            System.out.println(read);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileInputStream getStream(){
        try {
            FileInputStream fileInputStream = new FileInputStream(".gitignore");
            return fileInputStream;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**J java.io.IOException: Stream Closed
     * 	at java.io.FileInputStream.read0(Native Method)
     * 	at java.io.FileInputStream.read(FileInputStream.java:207)
     * 	at com.tony.puzzle.App.test(App.java:17)
     * 	at com.tony.puzzle.App.main(App.java:11)
     * @return
     */
    public FileInputStream getStream1(){
        try (FileInputStream fileInputStream = new FileInputStream(".gitignore");){
            return fileInputStream;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
