package com.joker.domos.screen;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.kw.gdx.BaseGame;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.screen.BaseScreen;

public class LoadScreen extends BaseScreen {
    public LoadScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        TextField textField = new TextField("",new TextField.TextFieldStyle());
        textField.setSize(Constant.GAMEWIDTH,200);
        addActor(textField);
        textField.setDebug(true);
    }
}
