package kw.artpuzzle.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.view.dialog.DialogManager;
import com.kw.gdx.view.dialog.base.BaseDialog;

import kw.artpuzzle.constant.GameStaticInstance;
import kw.artpuzzle.pref.ArtPuzzlePreferece;

@ScreenResource("cocos/rate01.json")
public class Rate01Screen extends BaseDialog {
    private Runnable runnable;
    public Rate01Screen(Runnable runnable){
        type = DialogManager.Type.closeOldShowCurr;
        this.runnable = runnable;
    }

    @Override
    public void show() {
        super.show();
        findActor("love", Touchable.enabled).addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
//                dialogManager.showDialog(new Rate03Screen(runnable));
                if (Gdx.app.getVersion() > 21 && !ArtPuzzlePreferece.getInstance().isHideLevelRate()) {
                    ArtPuzzlePreferece.getInstance().hideLevelRate();
                    GameStaticInstance.gameListener.newRate();
                }else {
                    GameStaticInstance.gameListener.rate();
                }
                runnable.run();
                closeDialog();
            }
        });
        findActor("notlove", Touchable.enabled).addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                closeDialog();
                runnable.run();
            }
        });
    }
}
