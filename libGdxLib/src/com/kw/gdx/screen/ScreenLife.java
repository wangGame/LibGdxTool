package com.kw.gdx.screen;

import com.badlogic.gdx.Screen;

public interface ScreenLife extends Screen {
    void onCreate();
    void onResume();
    void onPause();
    void onDestroy();
}
