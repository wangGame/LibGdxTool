package kw.artpuzzle.dialog;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.listener.ButtonListener;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.screen.BaseScreen;
import com.kw.gdx.view.dialog.base.BaseDialog;

import kw.artpuzzle.ArtPuzzle;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.group.ImageActor;
import kw.artpuzzle.screen.MainScreen;

public class ModelDialog extends BaseDialog {
    private BaseScreen game;
    public ModelDialog(BaseScreen game){
        this.game = game;
    }

    public void show(){
        super.show();
        new Image(Asset.getAsset().getTexture("shuoming.png")){{
            setPosition(dialogSize.x/2, dialogSize.y/2, Align.center);
            addActor(this);
        }};

        Image btn1 = new Image(Asset.getAsset().getTexture("fangshiyi.png"));
        Image btn2 = new Image(Asset.getAsset().getTexture("fangshier.png"));
        Image btn3 = new Image(Asset.getAsset().getTexture("fangshisan.png"));
        addActor(btn1);
        addActor(btn2);
        addActor(btn3);

        btn1.setOrigin(Align.center);
        btn2.setOrigin(Align.center);
        btn2.setPosition(getWidth()/2,getHeight()/2-400,Align.center);
        btn1.setPosition(getWidth()/2-200,getHeight()/2-400,Align.center);
        btn3.setPosition(getWidth()/2+200,getHeight()/2-400,Align.center);
        btn1.addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //use download
                LevelConfig.useInocal = false;
                game.setScreen(MainScreen.class);
            }
        });
        btn2.addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                LevelConfig.useInocal = true;
                game.setScreen(MainScreen.class);
            }
        });
        btn3.addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                LevelConfig.ISSHUFFLE = LevelConfig.SHUFFLE;
                LevelConfig.useInocal = false;
                game.setScreen(MainScreen.class);
            }
        });
    }
}
