package com.tony.puzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.kw.gdx.utils.log.NLog;
import com.tony.puzzle.desktopnet.DeskDownload;

import java.awt.Dimension;
import java.awt.Toolkit;

import kw.artpuzzle.ArtPuzzle;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.level.LevelView;
import kw.artpuzzle.listener.GameListener;
import kw.artpuzzle.pref.ArtPuzzlePreferece;
import kw.test.file.Bean;
import kw.test.file.ReadFileConfig;

public class DesktopLauncher {
    public static void main(String[] args) {
        ReadFileConfig readFileConfig = new ReadFileConfig();
        Bean value = readFileConfig.getValue();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title=value.getName();
        config.x = 1000;
        config.y = 0;
        config.height = (int) (1920 * 0.25f);
        config.width = (int) (1080 * 0.3f);
        new LwjglApplication(new ArtPuzzle(new DeskDownload(), new GameListener() {
            @Override
            public void vibrate(long milliseconds,int amplitude) {

            }

            @Override
            public boolean isNetConnect() {
                return true;
            }

            @Override
            public void vibrate1() {

            }

            @Override
            public void auto(LevelView view) {
//                Autoplayer autoplayer = new Autoplayer();
//                autoplayer.auto(view);
            }

            @Override
            public void rate() {

            }

            @Override
            public void newRate() {

            }

            @Override
            public void showBanner() {
                NLog.e("show banner ---------------------");
            }

            @Override
            public void hideBanner() {
                NLog.e("hide banner ---------------------");
            }

            @Override
            public void showInterstitial() {
                NLog.e("show interstitial ---------------------");
                LevelConfig.countTime = 0;
            }

            @Override
            public void showInterstitial(long time) {
                NLog.e("show intertitial ---------------------");
                LevelConfig.countTime = 0;
            }

            @Override
            public void showAdsVideo(Runnable runnable) {
                ArtPuzzlePreferece.getInstance().updateHintStatus(true);
            }

            @Override
            public void videoClosed() {
                ArtPuzzlePreferece.getInstance().updateHintStatus(true);
            }

            @Override
            public void home() {

            }

            @Override
            public boolean isVideoAlready() {
                return false;
            }

        }), config);
    }

    private static <T> T getHeight(String[] split, int i, T i2) {
        T height;
        try {
            String[] split1 = split[i].split(":");
            height = (T) split1[1];
        } catch (Exception e) {
            height = i2;
        }
        return height;
    }
}