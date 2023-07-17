package com.libGdx.test.json;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/27 17:12
 */
public class App2 extends LibGdxTestMain {
    public static void main(String[] args) {
        App2 app2 = new App2();
        app2.start(app2);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        {
            Json json = new Json();
            FileHandle internal = Gdx.files.internal("1_59_40.json");
            String string = internal.readString();
            json.toJson(string);
        }
        {
            Json json = new Json();
            FileHandle internal = Gdx.files.internal("1_59_40.json");
            json.fromJson(Bean.class,internal);
        }
    }
}
