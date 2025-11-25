package com.libGdx.test.fsm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.cocos.CocosApp;
import com.libGdx.test.fsm.state.IdleState;
import com.libGdx.test.fsm.state.RunningState;

public class AppFsm extends LibGdxTestMain {
    private Player player;
    public static void main(String[] args) {
        AppFsm appFsm = new AppFsm();
        appFsm.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        player = new Player("Jack");
        player.addState(new IdleState());
        player.addState(new RunningState());
    }

    @Override
    public void render() {
        super.render();
        if (player==null)return;
        float delta = Gdx.graphics.getDeltaTime();
        player.update(delta);

        // 测试事件触发
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            player.handleEvent(RunningState.class.getSimpleName());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            player.handleEvent(IdleState.class.getSimpleName());
        }
    }
}