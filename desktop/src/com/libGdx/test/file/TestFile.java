package com.libGdx.test.file;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.base.LibGdxTestMain;

public class TestFile extends LibGdxTestMain {
    public static void main(String[] args) {
        TestFile testFile = new TestFile();
        testFile.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        FileTest test = new FileTest();
        test.updateLevel("v");
        for (int i = 0; i < 10; i++) {
            test.updateLevel(i+"");
        }
        Array<String> strings1 = test.readFileArray();
        for (String s : strings1) {
            System.out.print(s+" ");
        }
        System.out.println("-------------------");
    }
}
