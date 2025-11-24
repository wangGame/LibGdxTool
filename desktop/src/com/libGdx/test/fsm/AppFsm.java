package com.libGdx.test.fsm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.cocos.CocosApp;
import com.libGdx.test.fsm.state.IdleState;

public class AppFsm extends LibGdxTestMain {
    private Player player;
    public static void main(String[] args) {
        CocosApp cocosApp = new CocosApp();
        cocosApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        player = new Player("Hero");
        player.stateMachine.changeState(new IdleState(),"");
    }

    @Override
    public void render() {
        super.render();
        float delta = Gdx.graphics.getDeltaTime();
        player.update(delta);

        // 测试事件触发
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            player.handleEvent("run");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            player.handleEvent("stop");
        }
    }
}