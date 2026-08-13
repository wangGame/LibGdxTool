package com.joker.domos.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.joker.domos.GameTest;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.screen.BaseScreen;

public class LoadScreen extends BaseScreen {
    public LoadScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();

        stage.addAction(Actions.forever(Actions.delay(1f, Actions.run(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }))));

//        TextField textField = new TextField("",new TextField.TextFieldStyle(){{
//            font = Asset.getAsset().loadBitFont("font/Manrope-Bold_56_1.fnt");
//            cursor = new TextureRegionDrawable(Asset.getAsset().getSprite("textfield/textc.png"));
//            fontColor = Color.WHITE;
//        }});
//        textField.setSize(Constant.GAMEWIDTH,200);
//        addActor(textField);
//        textField.setDebug(true);
//        textField.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT*3/5, Align.center);
        Label label = new Label("Loading...",new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("font/Manrope-Bold_56_1.fnt");
            fontColor = Color.WHITE;
        }});
        addActor(label);
        label.setWidth(Constant.GAMEWIDTH);
        label.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT*3/5, Align.center);
        label.setText("new player ");
        label.setDebug(true);
        label.setAlignment(Align.left);
        label.addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameTest.getUserInputListener().showHandleInput("请输入玩家名称", new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        label.setText(text);
                    }

                    @Override
                    public void canceled() {

                    }
                });
            }
        });
    }
}
