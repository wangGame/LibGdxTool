package com.libGdx.test.cirprogres;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.cirprogress.CircleProgress;
import com.kw.gdx.ecode.EnCodeUtils;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.ecode.EcodeTest;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/18 13:50
 */
public class CirProgress extends LibGdxTestMain {
    public static void main(String[] args) {
        CirProgress csvTest = new CirProgress();
        csvTest.start(csvTest);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        MainResource.atlas.findRegion("combo_progress"),MainResource.atlas.findRegion("combo_progress_point"),46F
        CircleProgress circleProgress = new CircleProgress(
                new TextureRegion(Asset.getAsset().getTexture("ad_progress.png")),
                new TextureRegion(Asset.getAsset().getTexture("ad_progress.png")),
                100);
        stage.addActor(circleProgress);
//        circleProgress.setPercentage(50);
        circleProgress.setTargetPercent(100,false,0.1f,null);
        circleProgress.setPosition(100,100);
    }
}
