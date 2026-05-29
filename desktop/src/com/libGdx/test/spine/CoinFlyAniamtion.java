package com.libGdx.test.spine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class CoinFlyAniamtion extends LibGdxTestMain {
    public static void main(String[] args) {
        CoinFlyAniamtion coinFlyAniamtion = new CoinFlyAniamtion();
        coinFlyAniamtion.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        flyCoin(stage);
    }

    public void flyCoin(Stage stage){
        Image[] coins = new Image[7];

        for (int i = 0; i < coins.length; i++) {
            coins[i] = new Image(Asset.getAsset().getTexture("assets/7.png"));
            coins[i].setSize(105, 107);
        }

        float startX = 100;
        float startY = 100;


        FlyCoinActions.play(
                stage.getRoot(),
                coins,
                startX,
                startY,
                () -> {
                    // 比如右上角金币UI的中心点
                    return new Vector2(720,1720);
                }
        );
    }
}
