package com.libGdx.test.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;



/**
 * @Auther jian xian si qi
 * @Date 2023/9/6 10:46
 */
class FileExsiTimeTest extends LibGdxTestMain {
    public static void main(String[] args) {
        FileExsiTimeTest fileExsiTimeTest = new FileExsiTimeTest();
        fileExsiTimeTest.start(fileExsiTimeTest);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        FileHandle absolute = Gdx.files.absolute("c:/");
//        fileTes(absolute);
        long currentTimeMillis = System.currentTimeMillis();
        FileHandle absolute = Gdx.files.absolute("E:\\artpuzzle\\Art Puzzle\\3-美术资源\\封面");
        fileTes(absolute);
        System.out.println(System.currentTimeMillis() - currentTimeMillis+"---------------------");
    }

    public void fileTes(FileHandle handle){
        if (handle.isDirectory()) {
            for (FileHandle fileHandle : handle.list()) {
                fileTes(fileHandle);
            }
        }else {
            if (handle.exists()) {

            }
        }
    }
}
