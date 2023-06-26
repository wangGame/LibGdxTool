package kw.artpuzzle.group;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;

import kw.artpuzzle.screen.GameScreen;
import kw.artpuzzle.screen.MainScreen;
import kw.artpuzzle.theme.GameTheme;

public class ShadowImageUtils {
    public ShadowImageUtils(Action runnable, Group prent){
        Image image = new Image(Asset.getAsset().getTexture("common/white_bg.png"));
        image.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        prent.addActor(image);
        image.setColor(GameTheme.bgColor);
        image.addAction(runnable);
    }
}
