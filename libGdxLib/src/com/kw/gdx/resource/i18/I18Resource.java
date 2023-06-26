package com.kw.gdx.resource.i18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 *
 */
public class I18Resource {
    private I18NBundle bundle;
    public void loadResource(String path){
        FileHandle internal = Gdx.files.internal(path);
        bundle = I18NBundle.createBundle(internal, Locale.US);
    }

    public String findValue(String key){
        if (bundle == null) {
            throw new RuntimeException();
        }
        return bundle.get(key);
    }

}
