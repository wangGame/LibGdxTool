package com.libGdx.test.colorchoose;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }
    private BitmapFont font;
    private static final int TARGET_LIGHT = 0;
    private static final int TARGET_DARK = 1;
    private Texture pixel;
    boolean isReady;
    private final SlotColorChooserPanel chooser = new SlotColorChooserPanel(new SlotColorChooserPanel.Listener() {
        @Override
        public void applyColor(int target, Color color) {
//            if (target == TARGET_DARK) {
//                slot.dark.set(color.r, color.g, color.b, 1f);
//                slot.tintBlack = true;
//            } else {
//                slot.light.set(color);
//            }
        }
    });
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        font = Asset.getAsset().loadBitFont("assets/font/Cali_75.fnt");
        chooser.layout(1100, 1500);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        isReady = true;


        Actor actor = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                if (isReady){
                    chooser.draw(getBatch(),font,pixel);
                }
            }
        };
        addActor(actor);
    }

    @Override
    public void render() {
        super.render();

    }
}
