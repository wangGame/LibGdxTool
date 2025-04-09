package com.libGdx.test.label;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.view.label.Label4;
import com.libGdx.test.base.LibGdxTestMain;

public class Label4App extends LibGdxTestMain {
    public static void main(String[] args) {
        Label4App app = new Label4App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Label a = new Label("",new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("font/Krub-Bold_52_1.fnt");
        }}){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
            }
        };
        addActor(a);
        a.setAlignment(Align.center);
        a.setText("ACfffffff");
        a.setPosition(100,100);

        a.setModkerning(10);



    }
}
