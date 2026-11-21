package com.libGdx.test.watersort;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

/** Run this class, then click a source tube and a destination tube. */
public class WaterPourDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        new WaterPourDemo().start();
    }

    @Override
    public void useShow(Stage stage) {
        WaterPourActor tool = new WaterPourActor();
        tool.addTube(245, 690, 1, 2, 2, 2);
        tool.addTube(535, 690, 3, 3, 1);
        tool.addTube(825, 690);
        tool.addTube(390, 190, 4, 5, 4, 5);
        tool.addTube(690, 190);
        stage.addActor(tool);
    }
}
