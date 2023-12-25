package com.libGdx.test;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/25 19:19
 */
class TextureAt extends LibGdxTestMain {
    public static void main(String[] args) {
        TextureAt a = new TextureAt();
        a.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Asset.getAsset().getAtlas("levelAtlas.atlas");
    }
}
