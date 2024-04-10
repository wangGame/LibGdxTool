package com.kw.gdx.bundle;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.Locale;

/**
 * @Auther jian xian si qi
 * @Date 2024/1/1 22:05
 */
public class GameLocale {
    private static GameLocale gameLocale;

    public static GameLocale getGameLocale() {
        if (gameLocale == null){
            gameLocale = new GameLocale();
        }
        return gameLocale;
    }

    public String getLanguage(){
        return Locale.getDefault().getLanguage();
    }

    public Locale getDefault(){
        return Locale.getDefault();
    }

}
