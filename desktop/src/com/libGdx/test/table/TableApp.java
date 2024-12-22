package com.libGdx.test.table;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.alpha.AlphaTest;
import com.libGdx.test.base.LibGdxTestMain;

public class TableApp extends LibGdxTestMain {
    public static void main(String[] args) {
        TableApp app = new TableApp();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        stage.addActor(new Table(){{
            align(Align.center);
            {
                Image image = new Image(Asset.getAsset().getTexture("next.png"));
                add(image).colspan(3);
            }
            row();
            {
                for (int i = 0; i < 3; i++) {
                    Image image = new Image(Asset.getAsset().getTexture("next.png"));
                    add(image);
                }
            }
            row();
            {
                for (int i = 0; i < 2; i++) {
                    Image image = new Image(Asset.getAsset().getTexture("next.png"));
                    add(image);
                }
            }
            pack();
            setPosition(Constant.GAMEWIDTH/2.0f,Constant.GAMEHIGHT/2.f, Align.center);

        }});
    }
}
