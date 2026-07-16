package com.kw.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;

public class CurvePanelScreen extends BaseScreen {
    private CurveEditorPanel panel;
    private ShapeRenderer shapes;
    private BitmapFont font;
    private Texture pixel;

    public CurvePanelScreen(BaseGame game) {
        super(game);
    }

    @Override
    protected void initData() {
        panel = new CurveEditorPanel();
        shapes = new ShapeRenderer();
        font = Asset.getAsset().loadBitFont("Ma-B_46.fnt");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    protected void initTouch() {
        super.initTouch();
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return panel.touchDown(x, y);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                panel.touchDragged(x, y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                panel.touchUp();
            }
        });
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        panel.layout(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        panel.update(delta);

        Gdx.gl.glClearColor(0.055f, 0.060f, 0.066f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapes.setProjectionMatrix(stage.getCamera().combined);
        game.getBatch().setProjectionMatrix(stage.getCamera().combined);
        panel.draw(game.getBatch(), shapes, font, pixel);
    }

    @Override
    public void dispose() {
        if (shapes != null) shapes.dispose();
        if (pixel != null) pixel.dispose();
        if (font != null) font.dispose();
        super.dispose();
    }
}
