package com.libGdx.test.task;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.chain.BaseChainTask;
import com.kw.gdx.chain.BaseChainTaskGroup;
import com.kw.gdx.chain.ChainManager;

/**
 *
 */
public class TaskOne extends BaseChainTaskGroup {
    private ChainManager chainManager;
    public TaskOne() {
        Image image = new Image(Asset.getAsset().getTexture("assets/7.png"));
        addActor(image);
        setSize(image.getWidth(), image.getHeight());
    }

    @Override
    public void runTask() {
        addAction(Actions.sequence(Actions.moveTo(300, 300, 2), Actions.run(() -> {
                            System.out.println("TaskOne finish");
                            finish();
                        })
                )
        );
    }

    @Override
    public void finish() {
        clearActions();
        if (chainManager != null) {
            chainManager.taskFinish();
        }
    }

    @Override
    public void setChainManager(ChainManager chainManager) {
        this.chainManager = chainManager;
    }
}