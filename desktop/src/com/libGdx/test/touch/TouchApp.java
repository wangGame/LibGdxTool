package com.libGdx.test.touch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class TouchApp extends LibGdxTestMain {
    public static void main(String[] args) {
        TouchApp touchApp = new TouchApp();
        touchApp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        //坐标转换
        ImageActor actor = new ImageActor();
        Image btnImg = new Image(Asset.getAsset().getTexture("assets/7.png"));
        addActor(btnImg);
        btnImg.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Vector2 temp = new Vector2(x,y);
                getStageViewport().unproject(temp);
                if (actor.hit(temp.x,temp.y,true)!=null) {
                    System.out.println("==================");
                }
            }
        });
    }
}
