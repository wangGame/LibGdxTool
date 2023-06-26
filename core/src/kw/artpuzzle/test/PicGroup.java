package kw.artpuzzle.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;

public class PicGroup extends Group {
    public PicGroup(){
        FileHandle fileHandle = Gdx.files.internal("");
        for (FileHandle handle : fileHandle.list()) {

        }
    }
}
