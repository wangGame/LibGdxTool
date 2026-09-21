package com.kw.freetype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FreeTypeHelper {

    private BitmapFont loadFont(int size) {
        try {
            FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nanhai-cjk.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
            p.size = size;
            p.color = Color.WHITE;
            p.borderWidth = 1.1f;
            p.borderColor = new Color(0f, 0f, 0f, 0.75f);
            p.characters = FreeTypeFontGenerator.DEFAULT_CHARS + uiChars();
            p.incremental = true; // Nicknames can introduce CJK characters outside the UI strings.
            BitmapFont f = gen.generateFont(p);
            return f;
        } catch (Exception ex) {
            Gdx.app.error("load font ", "font load failed", ex);
            BitmapFont fallback = new BitmapFont();
            fallback.getData().setScale(size / 16f);
            return fallback;
        }
    }

    private String uiChars() {
        try {
            return Gdx.files.internal("fonts/ui-chars.txt").readString("UTF-8")
                    .replace("\n", "").replace("\r", "");
        } catch (Throwable ignored) {
            return "";
        }
    }
}
