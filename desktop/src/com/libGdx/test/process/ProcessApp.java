package com.libGdx.test.process;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.cirprogress.ProcessGroupLine;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * 好像没啥用
 */
public class ProcessApp extends LibGdxTestMain {
    public static void main(String[] args) {
        ProcessApp app = new ProcessApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        ProcessGroup processGroup = new ProcessGroup();
        addActor(processGroup);

        Image progressBg = new Image(Asset.getAsset().getTexture("assets/jx.png"));
        Image progressFg = new Image(Asset.getAsset().getTexture("assets/jx.png"));

        ProcessGroupLine processGroupLine = new ProcessGroupLine(progressBg, progressFg);
        processGroupLine.setPosition(100, 100);
        addActor(processGroupLine);
    }
}
