package com.joker.domos.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.screen.BaseScreen;

/**
 * Android-ready real-time frosted glass demo.
 */
public class BlurScreen extends BaseScreen {
    private DemoBackground background;
    private BlurGlass blurGlass;
    private ParameterPanel parameterPanel;
    private FpsDisplay fpsDisplay;

    public BlurScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        background = new DemoBackground();
        background.setBounds(0, 0, Constant.GAMEWIDTH, Constant.GAMEHIGHT);
        addActor(background);
        Image image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        background.addActor(image);
        image.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                float maxX = Math.max(0f, background.getWidth() - image.getWidth());
                float maxY = Math.max(0f, background.getHeight() - image.getHeight());
                image.setPosition(MathUtils.clamp(image.getX() + x - getTouchDownX(), 0f, maxX), MathUtils.clamp(image.getY() + y - getTouchDownY(), 0f, maxY));
            }
        });
        blurGlass = new BlurGlass(background);
        blurGlass.setSize(1430, 1430);
        blurGlass.setPosition((Constant.GAMEWIDTH - blurGlass.getWidth()) / 2f, (Constant.GAMEHIGHT - blurGlass.getHeight()) / 2f);
        blurGlass.setTouchable(Touchable.disabled);
        addActor(blurGlass);

        parameterPanel = new ParameterPanel(blurGlass);
        parameterPanel.setBounds(110, 50, Constant.GAMEWIDTH - 220, 390);
        addActor(parameterPanel);

        fpsDisplay = new FpsDisplay();
        fpsDisplay.setBounds(Constant.GAMEWIDTH - 330, Constant.GAMEHIGHT - 110, 290, 80);
        addActor(fpsDisplay);
    }

    @Override
    public void dispose() {
        if (blurGlass != null) blurGlass.dispose();
        if (background != null) background.dispose();
        if (parameterPanel != null) parameterPanel.dispose();
        if (fpsDisplay != null) fpsDisplay.dispose();
        super.dispose();
    }



}
