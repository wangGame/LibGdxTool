package com.libGdx.test.pet;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class PetGame extends LibGdxTestMain {
    public static void main(String[] args) {
        PetGame petGame = new PetGame();
        petGame.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        Image bg = new Image(Asset.getAsset().getTexture("white.png"));
        addActor(bg);
        bg.setOrigin(Align.center);
        bg.setScale(1000);

//        PetGroup petGroup = new PetGroup();
//        addActor(petGroup);
        GradientOutlineImage outlineImage = new GradientOutlineImage(Asset.getAsset().getTexture("dog_xuanguan.png"));
        addActor(outlineImage);
        outlineImage.setScale(3);
    }
}
