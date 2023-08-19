package com.libGdx.test.trile.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.trile.utils.Test.Begin;

import kw.test.file.Bean;
import kw.test.file.ReadFileConfig;

/**
 * @Auther jian xian si qi
 * @Date 2023/8/8 12:29
 */
class XXXXXXX {
    public static void main(String[] args) {
        ReadFileConfig readFileConfig = new ReadFileConfig();
        Bean value = readFileConfig.getValue();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title=value.getName();
        config.x = 1000;
        config.y = 0;
        config.height = (int) (1920 * 0.25f);
        config.width = (int) (1080 * 0.3f);
        Gdx.isJiami = true;
        new LwjglApplication(new Begin(), config);
    }

}
