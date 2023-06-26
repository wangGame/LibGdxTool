package kw.artpuzzle.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.view.dialog.base.BaseDialog;

@ScreenResource("cocos/rate02.json")
public class Rate02Screen extends BaseDialog {
    public Rate02Screen(){
        show();
    }

    @Override
    public void show() {
        super.show();
        findActor("ok", Touchable.enabled).addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                closeDialog();

            }
        });
    }
}
