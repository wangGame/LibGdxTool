package com.libGdx.test.click;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Table table = new Table(){{
            Color color[] = {
                    Color.RED,
                    Color.BLACK,
                    Color.GRAY
            };
            for (int i = 0; i < 3; i++) {
                Group group = new Group();
                group.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
                Image image = new Image(Asset.getAsset().getTexture("white.png"));
                image.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
                group.addActor(image);
                add(group);
                image.setColor(color[i]);
            }
            pack();
        }};
        ScrollPane pane = new ScrollPane(table){
            boolean ss = false;
            @Override
            public void act(float delta) {
                super.act(delta);
//                if (isFlickScrollTouchUp()){
//                    setScrollX(0);
//                }
//                if (ss)return;
//                float scrollX = getScrollX();
//                if (scrollX>330){
//                    ss = true;
//                    System.out.println("dragge -------------------");
//                    Image shader = new Image(Asset.getAsset().getTexture("white.png"));
//                    stage.addActor(shader);
//                    shader.setColor(Color.BLACK);
//                    shader.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
//                    shader.getColor().a = 0.2f;
//                }
//
//

            }
        };

        stage.addActor(pane);
        pane.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        pane.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                System.out.println("touch up ----------- ");
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                System.out.println(x+" ----------- "+y);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch down ----------- ");
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
}
