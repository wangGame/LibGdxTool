package com.libGdx.test.endless;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/10/23 22:41
 */
public class MSCTest extends LibGdxTestMain {
    public static void main(String[] args) {
        MSCTest test = new MSCTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        MainBannerGroupBig bannerGroupBig = new MainBannerGroupBig();
        addActor(bannerGroupBig);
    }
}
