package kw.artpuzzle.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kw.gdx.listener.ButtonListener;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.view.dialog.base.BaseDialog;

@ScreenResource("cocos/adsTry.json")
public class AdsTry extends BaseDialog {
    public AdsTry() {
        show();
    }

    @Override
    public void show() {
        super.show();
        Actor ok = findActor("ok");
        ok.setTouchable(Touchable.enabled);
        ok.addListener(new ButtonListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                dialogManager.closeDialog(AdsTry.this);
            }
        });
    }
}