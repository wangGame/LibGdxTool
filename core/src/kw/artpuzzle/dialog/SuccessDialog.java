package kw.artpuzzle.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.view.dialog.base.BaseDialog;
import com.kw.gdx.listener.ButtonListener;

import kw.artpuzzle.pref.ArtPuzzlePreferece;

public class SuccessDialog extends BaseDialog {
    private Runnable runnable;
    public SuccessDialog(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void show() {
        super.show();
        Image image = new Image(Asset.getAsset().getTexture("success.png"));
        addActor(image);
        image.setPosition(getWidth()/2,getHeight()/2, Align.center);

        Image btn = new Image(Asset.getAsset().getTexture("next.png"));
        addActor(btn);
        btn.setPosition(getWidth()/2,getHeight()/2,Align.center);
        btn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                runnable.run();
            }
        });

    }
}
