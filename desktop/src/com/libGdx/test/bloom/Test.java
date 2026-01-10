package com.libGdx.test.bloom;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;
import com.solvitaire.app.T;

public class Test extends LibGdxTestMain {
    private Bloom bloom;
    public static void main(String[] args) {
        Test.run(Test.class);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        bloom = new Bloom(
                100,
                100,
                true,  // alpha mask
                true,  // blending
                true   // RGBA
        );
        // 设置 Bloom 参数
        bloom.setBloomIntesity(2.5f);      // 高亮强度
        bloom.setOriginalIntesity(0.8f);   // 原画面强度
        bloom.setTreshold(0.5f);           // 阈值
        bloom.setClearColor(0, 0, 0, 1);   // 背景黑色
        Image image = new Image(Asset.getAsset().getTexture("000.png"));
        addActor(image);
    }

    @Override
    public void render() {
        // -------------------- 1. 开始捕获 --------------------
        if (bloom!=null) {
            bloom.capture();
        }
        super.render();
        if (bloom!=null) {
            bloom.capturePause();
            bloom.render();
        }
    }
}
