package com.kw.gdx.utils.ads;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;

public class BannerManager {
    private Stage stage;

    private static boolean isVisible;
    private boolean currentVisible = false;

    public BannerManager(Stage stage) {
        this.stage = stage;
    }

    public void toFront() {
        if (currentVisible != isVisible){
            currentVisible = isVisible;

        }

    }

    public static void setVisible(boolean visible){
        isVisible = visible;
    }

    public void init(float v){

    }




    public void showBanner(boolean visible){
    }
}
