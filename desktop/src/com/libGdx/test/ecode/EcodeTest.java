package com.libGdx.test.ecode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.ecode.EnCodeUtils;
import com.libGdx.test.base.LibGdxTestMain;
/**
 * @Auther jian xian si qi
 * @Date 2023/7/14 16:04
 */
public class EcodeTest extends LibGdxTestMain {
    public static void main(String[] args) {
        EcodeTest csvTest = new EcodeTest();
        csvTest.start(csvTest);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        FileHandle internal = Gdx.files.internal("dog_xuanguan.png");
        byte[] bytes = internal.readBytes();
        EnCodeUtils codeUtils = new EnCodeUtils();
        codeUtils.ecodeBytes(bytes,(byte) 3);
        codeUtils.dcodeBytes(bytes,(byte) 3);
    }
}
