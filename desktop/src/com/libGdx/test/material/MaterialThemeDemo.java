package com.libGdx.test.material;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** Minimal runnable Scene2D demo. */
public final class MaterialThemeDemo extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private MaterialTheme theme;
    private boolean dark;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont();
        skin = new Skin();
        theme = new MaterialTheme(font);

        applyThemeAndBuildUi();
    }

    private void applyThemeAndBuildUi() {
        MaterialColorScheme scheme = dark
            ? MaterialColorScheme.baselineDark()
            : MaterialColorScheme.baselineLight();
        theme.apply(skin, scheme);

        stage.clear();

        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(theme.surface());
        root.pad(32f);
        stage.addActor(root);

        Table card = new Table();
        card.setBackground(theme.surfaceContainer());
        card.pad(24f);

        Label title = new Label("Material-like Color Theme", skin, MaterialTheme.STYLE_LABEL);
        TextButton filled = new TextButton("Filled button", skin, MaterialTheme.STYLE_FILLED_BUTTON);
        TextButton tonal = new TextButton("Tonal button", skin, MaterialTheme.STYLE_TONAL_BUTTON);
        TextButton toggle = new TextButton(dark ? "Switch to Light" : "Switch to Dark",
            skin, MaterialTheme.STYLE_TEXT_BUTTON);

        toggle.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dark = !dark;
                applyThemeAndBuildUi();
            }
        });

        card.defaults().growX().height(52f).padBottom(12f);
        card.add(title).left().height(40f).row();
        card.add(filled).row();
        card.add(tonal).row();
        card.add(toggle).row();

        root.add(card).width(420f);
    }

    @Override
    public void render() {
        ScreenUtils.clear(theme.getScheme().background);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        theme.dispose();
        font.dispose();
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.width = 1000;
        configuration.height = 1000;
        LwjglApplication lwjglApplication = new LwjglApplication(new MaterialThemeDemo(), configuration);
    }
}
