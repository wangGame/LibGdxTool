package com.libGdx.test.label;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }
    private Touchpad touchpad;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        {
            Label label = new Label("<<[BLUE]M[RED]u[YELLOW]l[GREEN]t[OLIVE]ic[]o[]l[]o[]r[]*[MAROON]Label[][] [Unknown Color]>>", new Label.LabelStyle() {{
                font = Asset.getAsset().loadBitFont("assets/font/inter-bold-20.fnt");
                font.getData().markupEnabled = true;
            }});
            addActor(label);
        }

        {
            System.out.println("\u0089\u0065\u0089\u0074");

            Label label = new Label("\u0065\u0074", new Label.LabelStyle() {{
                font = Asset.getAsset().loadBitFont("assets/font/inter-bold-20.fnt");
                font.getData().markupEnabled = true;
            }});
            label.setY(100);
            addActor(label);

//            "\u0089\u0065\u0089\u0074"
        }
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.knob = new BaseDrawable(new TextureRegionDrawable(new TextureRegion(Asset.getAsset().getTexture("assets/7.png"))));
        touchpadStyle.background = new BaseDrawable(new TextureRegionDrawable(new TextureRegion(Asset.getAsset().getTexture("assets/7.png"))));
        touchpad = new Touchpad(20,touchpadStyle);
        touchpad.setBounds(15, 15, 1000, 1000);
        stage.addActor(touchpad);
        touchpad.setPosition(300,300);
        touchpad.debug();
    }

    @Override
    public void render() {
        super.render();
        if (touchpad!=null)
         System.out.println(touchpad.getKnobPercentX() + " " + touchpad.getKnobPercentY());
    }
}
