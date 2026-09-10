package com.joker.domos.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.joker.domos.GameTest;
import com.joker.domos.audio.AudioManager;
import com.joker.domos.audio.Cue;
import com.joker.domos.audio.Tone;
import com.joker.domos.ui.TextButton2;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.event.simpleevent.EventListener;
import com.kw.gdx.event.simpleevent.EventManager;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.screen.BaseScreen;
import com.kw.gdx.sign.SignListener;
import com.kw.gdx.view.dialog.base.BaseDialog;

public class LoadScreen extends BaseScreen {
    private Dialog modal;
    public LoadScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();

        stage.addAction(Actions.forever(Actions.delay(1f, Actions.run(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }))));

//        TextField textField = new TextField("",new TextField.TextFieldStyle(){{
//            font = Asset.getAsset().loadBitFont("font/Manrope-Bold_56_1.fnt");
//            cursor = new TextureRegionDrawable(Asset.getAsset().getSprite("textfield/textc.png"));
//            fontColor = Color.WHITE;
//        }});
//        textField.setSize(Constant.GAMEWIDTH,200);
//        addActor(textField);
//        textField.setDebug(true);
//        textField.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT*3/5, Align.center);
        Label label = new Label("Loading...",new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("font/Manrope-Bold_56_1.fnt");
            fontColor = Color.WHITE;
        }});
        addActor(label);
        label.setWidth(Constant.GAMEWIDTH);
        label.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT*3/5, Align.center);
        label.setText("new player ");
        label.setDebug(true);
        label.setAlignment(Align.left);
        label.addListener(new OrdinaryButtonListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameTest.getUserInputListener().showHandleInput("请输入玩家名称", new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        label.setText(text);
                    }

                    @Override
                    public void canceled() {

                    }
                });
            }
        });

        addActor(new Table(){{
            for (int i = 1; i <= Cue.values().length; i++) {
                Cue value = Cue.values()[i-1];
                TextButton2 textButton = new TextButton2(value.name());
                textButton.setEventName(value);
                textButton.setSignListener(new SignListener<Cue>(){
                    @Override
                    public void sign(Cue cue) {
                        AudioManager.getInstance().playCue(cue);
                    }
                });
                add(textButton);
                if (i>0 && i%3==0){
                    row();
                }
            }
            pack();
        }});
    }

    private Drawable frame(String name, String fill) {
        Pixmap p = new Pixmap(48, 48, Pixmap.Format.RGBA8888);
        p.setColor(Color.valueOf("D8AE60")); p.fillRectangle(8, 0, 32, 48);
        p.fillRectangle(0, 8, 48, 32);
        p.setColor(Color.valueOf("FFF0B5")); p.fillRectangle(10, 0, 28, 2);
        p.setColor(Color.valueOf("70502C")); p.fillRectangle(8, 44, 32, 4);
        // Replace pixels so the panel's translucent center isn't composited over opaque gold.
        p.setBlending(Pixmap.Blending.None);
        p.setColor(Color.valueOf(fill)); p.fillRectangle(6, 8, 36, 32); p.fillRectangle(8, 6, 32, 36);
        Texture texture = new Texture(p); p.dispose();
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        NinePatch patch = new NinePatch(texture, 12, 12, 12, 12);
        patch.setPadding(24, 24, 8, 8);
        return new NinePatchDrawable(patch);
    }

    @Override
    protected BaseDialog back() {
        System.out.println("----------------------------xxxxxxxxxxxxxxback ");
        return null;
//        return super.back();
    }
}
