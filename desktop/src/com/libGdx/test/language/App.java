package com.libGdx.test.language;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.bundle.VBundle;
import com.libGdx.test.base.LibGdxTestMain;

import java.util.Locale;

/**
 * @Auther jian xian si qi
 * @Date 2024/1/1 21:33
 */
public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        System.out.println(Locale.US);
        //用户指定  如果没有指定使用设备
        VBundle vBundle = new VBundle(Gdx.files.internal("bundle/Lang"),Locale.US);
        System.out.println(vBundle.get("key"));
    }
}
