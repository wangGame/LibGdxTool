package com.libGdx.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.kw.gdx.utils.PythonDict;
import com.libGdx.test.base.LibGdxTestMain;

public class ConvertSong extends LibGdxTestMain {
    public static void main(String[] args) {
        ConvertSong song = new ConvertSong();
        song.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        FileHandle absolute = Gdx.files.absolute("E:\\mp3\\879099832");
        String songname = null;
        for (FileHandle fileHandle : absolute.list()) {
            for (FileHandle handle : fileHandle.list()) {
                if (handle.name().equals("entry.json")) {
                    Json json = new Json();
                    String result = handle.readString();
                    PythonDict root = json.fromJson(PythonDict.class, result);
                    PythonDict pageData = (PythonDict) root.get("page_data");
                    String part = (String) pageData.get("part");
                    part = part.replace("《","");
                    part = part.replace("》","");
                    System.out.println(part + "--------");
                    songname = part;
                }
            }
            for (FileHandle handle : fileHandle.list()) {
                if (handle.isDirectory()){
                    for (FileHandle fileHandle1 : fileHandle.list()) {
                        for (FileHandle fileHandle2 : fileHandle1.list()) {

                            if (fileHandle2.name().equals("audio.m4s")) {
                                FileHandle absolute1 = Gdx.files.absolute("E:\\mp3\\out\\" + songname + ".mp3");
                                fileHandle2.copyTo(absolute1);
                            }
                        }
                    }
                }
            }
        }
    }
}
