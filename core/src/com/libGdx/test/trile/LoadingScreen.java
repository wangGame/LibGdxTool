package com.libGdx.test.trile;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.screen.BaseScreen;

public class LoadingScreen extends BaseScreen {
    private Image image;
    private Image imagex;
    public LoadingScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        addActor(image);
        image.setY(Constant.GAMEHIGHT, Align.top);
        image.setOrigin(Align.topLeft);
        image.setScale(3.4f);


        imagex = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        stage.addActor(imagex);
        imagex.setDebug(true);

        imagex.setPosition(Constant.GAMEWIDTH/2.f,Constant.GAMEHIGHT/2.0f,Align.center);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
//        image.setY(Constant.GAMEHIGHT);
        System.out.println("Constant.GAMEHIGHT"+Constant.GAMEHIGHT);
        rootView.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        rootView.setPosition(Constant.GAMEWIDTH/2.0f,Constant.GAMEHIGHT/2.0f,Align.center);
        image.setX(0);
        image.setY(Constant.GAMEHIGHT, Align.top);
        rootView.setDebug(true);
        imagex.setPosition(Constant.GAMEWIDTH/2.f,Constant.GAMEHIGHT/2.0f,Align.center);
        System.out.println(Constant.GAMEWIDTH +" --------- "+Constant.GAMEHIGHT);
    }
}
