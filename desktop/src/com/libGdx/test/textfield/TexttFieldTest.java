package com.libGdx.test.textfield;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/29 18:27
 */
public class TexttFieldTest extends LibGdxTestMain {
    public static void main(String[] args) {
        TexttFieldTest texttFieldTest = new TexttFieldTest();
        texttFieldTest.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        TextField field = new TextField("",new TextField.TextFieldStyle(){{
            font = Asset.getAsset().loadBitFont("frmb-40.fnt");
            background = new NinePatchDrawable(
                    new NinePatch(
                        Asset.getAsset().getSprite("textfield/textfieldbg.png"),
                            50,50,40,40));
            cursor = new TextureRegionDrawable(Asset.getAsset().getSprite("textfield/textcursor.png"));
            fontColor = Color.BLACK;
        }
        });
        addActor(field);
        field.setSize(400,100);
        field.setPosition(Constant.GAMEWIDTH/2.0f,Constant.GAMEHIGHT/2.0f, Align.center);
        field.setDebug(true);
        field.setMessageText("Enter your username");
    }
}
