package com.libGdx.test.listener;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class ListenerDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        ListenerDemo listenerDemo = new ListenerDemo();
        listenerDemo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Texture texture = Asset.getAsset().getTexture("assets/0_1_41_512.jpg");
        Image image = new Image(texture);
        addActor(image);
//        image.addListener(new ClickListener(){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                System.out.println("---------------------------");
//            }
//        });
        image.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("ChangeListener");
            }
        });
    }
}
