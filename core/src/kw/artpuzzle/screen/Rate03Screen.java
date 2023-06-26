package kw.artpuzzle.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.view.dialog.base.BaseDialog;

import kw.artpuzzle.constant.GameStaticInstance;

@ScreenResource("cocos/rate03.json")
public class Rate03Screen extends BaseDialog {
    private Runnable runnable;
    public Rate03Screen(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void show() {
        super.show();
        findActor("submit").setOrigin(Align.center);
        findActor("submit", Touchable.enabled).addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (Gdx.app.getVersion() > 21) {
                    GameStaticInstance.gameListener.newRate();
                }else {
                    GameStaticInstance.gameListener.rate();
                }
                runnable.run();
                closeDialog();
            }
        });
    }
}
