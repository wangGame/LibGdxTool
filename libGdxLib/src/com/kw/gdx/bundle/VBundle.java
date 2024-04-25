package com.kw.gdx.bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * @Auther jian xian si qi
 * @Date 2024/1/1 22:02
 */
public class VBundle {
    private I18NBundle bundle;
    private FileHandle languagePath;
    private Locale userLocal;

    public VBundle(FileHandle languagePath) {
        this.languagePath = languagePath;
    }

    public VBundle(FileHandle languagePath,Locale userLocal) {
        this.languagePath = languagePath;
        this.userLocal = userLocal;
    }

    public final String get(String key) {
        return getString(key);
    }

    private boolean isNum(String txt) {
        try {
            Integer.parseInt(txt);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public String getString(String key) {
        String out = null;
        if (isNum(key))
            return key;
        if (bundle == null) {
            try {
                String language = GameLocale.getGameLocale().getLanguage();
                if (userLocal!=null){
                    language = userLocal.getLanguage();
                }
                bundle = I18NBundle.createBundle(languagePath, new Locale(language));
                out = bundle.get(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                out = bundle.get(key);
            } catch (Exception e) {
            }
        }
        return out == null ? key : out;
    }
}
