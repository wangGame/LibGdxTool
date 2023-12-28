package com.libGdx.test.shader;

import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class ShaderDrawTest extends LibGdxTestMain {
    public static void main(String[] args) {
        ShaderDrawTest test = new ShaderDrawTest();
        test.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        ImmediateModeRenderer20 immediateModeRenderer20 = new ImmediateModeRenderer20(false,false,0);
//        immediateModeRenderer20.setShader();
    }
}
