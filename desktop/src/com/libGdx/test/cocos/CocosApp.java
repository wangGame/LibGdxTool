package com.libGdx.test.cocos;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.resource.cocosload.CocosResource;
import com.libGdx.test.base.LibGdxTestMain;

public class CocosApp extends LibGdxTestMain {
    public static void main(String[] args) {
        CocosApp cocosApp = new CocosApp();
        cocosApp.start();

    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Group group = CocosResource.loadFile("cocos/level2.json");
        addActor(group);
    }
}
