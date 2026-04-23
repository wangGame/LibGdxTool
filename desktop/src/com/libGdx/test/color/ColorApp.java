package com.libGdx.test.color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.libGdx.test.base.LibGdxTestMain;

public class ColorApp extends LibGdxTestMain {
    public static void main(String[] args) {
        ColorApp app = new ColorApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Table table = new Table() {{
            for (int i = 1; i <= 256; i++) {
                ImageColor imageColor = new ImageColor(i);
                add(imageColor).pad(2);
                if (i%12==0){
                    row();
                }
            }
            pack();
        }};
        addActor(table);
    }
}
