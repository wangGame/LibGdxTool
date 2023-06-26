package kw.artpuzzle.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.view.dialog.DialogManager;
import com.kw.gdx.view.dialog.base.BaseDialog;

@ScreenResource("cocos/NoInterNet.json")
public class NoInterNetDialog extends BaseDialog {

    @Override
    public void show() {
        super.show();
        findActor("nointernet_button").addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                dialogManager.closeDialog(NoInterNetDialog.this);
            }
        });
    }
}
