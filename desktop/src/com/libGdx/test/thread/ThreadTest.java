package com.libGdx.test.thread;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.thread.Task;
import com.kw.gdx.thread.ThreadUtils;
import com.libGdx.test.base.LibGdxTestMain;

public class ThreadTest extends LibGdxTestMain {
    public static void main(String[] args) {
        ThreadTest test = new ThreadTest();
        test.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        ThreadUtils threadUtils = ThreadUtils.getThreadUtils();
        Image image = new Image(Asset.getAsset().getTexture("assets/7.png"));
        addActor(image);
        image.addAction(Actions.scaleTo(100,100,10));
        threadUtils.doTask(new Task<Boolean>() {
            @Override
            public Boolean doRunnable() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void success(Boolean texture) {
                Image image = new Image(Asset.getAsset().getTexture("assets/000.png"));
                stage.addActor(image);
            }
        });
    }
}
