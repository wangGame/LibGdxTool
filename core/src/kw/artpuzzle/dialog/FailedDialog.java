package kw.artpuzzle.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import com.kw.gdx.listener.ButtonListener;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.view.dialog.base.BaseDialog;

@ScreenResource("cocos/failedSceen.json")
public class FailedDialog extends BaseDialog {
    private Runnable runnable;
    public FailedDialog(Runnable runnable){
        this.runnable = runnable;
        show();
    }

    @Override
    public void show() {
        super.show();
        Actor btn_bg_6 = findActor("next_1");
        btn_bg_6.setTouchable(Touchable.enabled);
        btn_bg_6.addListener(new ButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                runnable.run();
            }
        });

    }
}
