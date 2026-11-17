package com.libGdx.test.fsm;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.net.LibGdxTest;

public class App extends LibGdxTest {
    public static void main(String[] args) {
        App a = new App();
        a.start();
    }
    private Player player;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        player = new Player();
        player.udpateDir(Direction.DOWN);
        Array<Direction> list = new Array<Direction>();
        for (Direction value : Direction.values()) {
            list.add(value);
        }
        stage.addAction(Actions.forever(Actions.sequence(
                Actions.delay(1),
                Actions.run(()->{
                    Direction random = list.random();
                    System.out.println(random);
                    player.udpateDir(random);
                })
        )));
    }
}
