package com.libGdx.test.sc.method1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kw.gdx.asset.Asset;

public class App extends ScreenAdapter {

    static class EmailData {
        String from;
        String subject;
        String preview;

        EmailData(String from, String subject, String preview) {
            this.from = from;
            this.subject = subject;
            this.preview = preview;
        }
    }

    private final Stage stage = new Stage(new ScreenViewport());
    private final Table listTable = new Table();
    private final ScrollPane scrollPane = new ScrollPane(listTable);

    private final float rowHeight = 110f;
    private final int buffer = 3;

    private final Array<EmailData> emails = new Array<>();
    private final Array<EmailRow> visibleRows = new Array<>();

    public App() {
        Gdx.input.setInputProcessor(stage);

        fillTestEmails();

        Table root = new Table();
        root.setFillParent(true);
        root.add(scrollPane).expand().fill();
        stage.addActor(root);

        createVisibleRows();
        updateRows();

        scrollPane.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateRows();
            }
        });
    }

    // ---------------- 行复用 ----------------
    private void createVisibleRows() {
        int screenHeight = Gdx.graphics.getHeight();
        int need = (int)(screenHeight / rowHeight) + buffer;

        for (int i = 0; i < need; i++) {
            EmailRow row = new EmailRow();
            visibleRows.add(row);
            listTable.add(row).height(rowHeight).expandX().fillX().row();
        }
    }

    private void updateRows() {
        float scrollY = scrollPane.getVisualScrollY();
        int firstIndex = (int)(scrollY / rowHeight);

        for (int i = 0; i < visibleRows.size; i++) {
            int index = firstIndex + i;
            EmailRow row = visibleRows.get(i);

            if (index >= 0 && index < emails.size) {
                row.setVisible(true);
                row.bind(emails.get(index));
            } else {
                row.setVisible(false);
            }
        }
    }

    // ---------------- Row UI ----------------
    class EmailRow extends Table {

        Label fromLabel = new Label("",new Label.LabelStyle(){{
            font = Asset.getAsset().loadBitFont("font/Krub-Bold_52_1.fnt");
        }});
        EmailRow() {
            pad(10f);
            align(Align.left);

            add(fromLabel).left().row();
            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                }
            });
        }

        void bind(EmailData data) {
            fromLabel.setText("From: " + data.from);
        }
    }

    // ---------------- 测试数据 ----------------
    private void fillTestEmails() {
        for (int i = 1; i <= 500; i++) {
            emails.add(new EmailData(
                    "Sender " + i,
                    "Email Subject " + i,
                    "This is preview content of email number " + i + " ..."
            ));
        }

        listTable.setHeight(emails.size * rowHeight);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.stencil=8;
        config.y = 0;
        new LwjglApplication(new Game() {
            @Override
            public void create() {
                setScreen(new App());
            }
        }, config);
    }

}