package kw.test.file;

import com.badlogic.gdx.files.FileHandle;

public class CopyFile {
    public static void main(String[] args) {
        FileHandle fileHandle = new FileHandle("D:\\nginx\\artpuzzle\\levelpre\\level104.zip");
        for (int i = 105; i < 300; i++) {
            FileHandle fileHandleOut = new FileHandle("D:\\nginx\\artpuzzle\\levelpre\\level"+i+".zip");
            fileHandle.copyTo(fileHandleOut);
        }
    }
}
