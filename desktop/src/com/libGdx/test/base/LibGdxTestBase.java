package com.libGdx.test.base;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kw.gdx.BaseGame;
import com.kw.gdx.utils.log.NLog;
import com.libGdx.test.effect.EffectTest;
import com.tony.puzzle.desktopnet.DeskDownload;

import org.junit.Test;

import kw.artpuzzle.ArtPuzzle;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.level.LevelView;
import kw.artpuzzle.listener.GameListener;
import kw.artpuzzle.pref.ArtPuzzlePreferece;
import kw.test.file.Bean;
import kw.test.file.ReadFileConfig;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/21 14:18
 */
public class LibGdxTestBase extends BaseGame {
    public void start(EffectTest test) {
        ReadFileConfig readFileConfig = new ReadFileConfig();
        Bean value = readFileConfig.getValue();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title=value.getName();
        config.x = 1000;
        config.y = 0;
        config.height = (int) (1920 * 0.25f);
        config.width = (int) (1080 * 0.3f);
        Gdx.isJiami = true;
        new LwjglApplication(test, config);
    }

}
