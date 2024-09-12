package com.libGdx.test.cocos;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.GameInfo;
import com.kw.gdx.resource.cocosload.CocosResource;
import com.libGdx.test.base.LibGdxTestMain;

public class CocosApp extends LibGdxTestMain {
    public static void main(String[] args) {
        CocosApp cocosApp = new CocosApp();
        cocosApp.start();

    }

    private Group group;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        group = CocosResource.loadFile("cocos/level2.json");
        addActor(group);
        group.setDebug(true);
        group.setPosition(Constant.GAMEWIDTH/2.0f,Constant.GAMEHIGHT/2.0f, Align.center);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (group!=null) {
            group.setPosition(Constant.GAMEWIDTH/2.0f,Constant.GAMEHIGHT/2.0f, Align.center);
        }
    }
}
