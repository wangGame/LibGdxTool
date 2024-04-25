package com.libGdx.test.app;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    private Image image;
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        image = new Image(Asset.getAsset().getTexture("3_34_24.png")){
            @Override
            public void act(float delta) {
                super.act(delta);
                while (true){
                    if (image!=null) {
                        image.setX(image.getX()+10);
                        System.out.println(image.getX());
                    }
                }
            }
        };
        addActor(image);
    }

    @Override
    public void render() {
        super.render();

    }
}
