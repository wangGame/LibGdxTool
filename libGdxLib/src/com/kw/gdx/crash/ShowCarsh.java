package com.kw.gdx.crash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ShowCarsh extends Group {

    private final BitmapFont font;
    private final ShapeRenderer shapeRenderer;
    private final Array<FileHandle> crashFiles = new Array<>();

    private int selectedIndex = -1;
    private String detail = "";
    private float scrollY = 0;

    private float buttonHeight = 44;
    private float listWidth;
    private float detailWidth;

    public ShowCarsh(BitmapFont font) {
        this.font = font;
        shapeRenderer = new ShapeRenderer();

        setSize(1000, 650);

        addActor(new ClickableArea(
                0, getHeight() - 50, 120, 40,
                () -> refreshCrashList()
        ));

        addActor(new ClickableArea(
                130, 8, 150, buttonHeight,
                () -> printSelectedLog()
        ));

        addActor(new ClickableArea(
                290, 8, 150, buttonHeight,
                () -> showOnPhone()
        ));

        addActor(new ClickableArea(
                getWidth() - 100, getHeight() - 50, 90, 40,
                () -> setVisible(false)
        ));

        refreshCrashList();
    }

    public void refreshCrashList() {
        crashFiles.clear();

        FileHandle dir = Gdx.files.local("LibUtilscrash/");
        if (dir.exists()) {
            for (FileHandle file : dir.list(".txt")) {
                if (file.file().isFile()) {
                    crashFiles.add(file);
                    System.out.println(file.name());
                }
            }
        }

        crashFiles.sort((a, b) -> b.name().compareTo(a.name()));

        selectedIndex = crashFiles.size > 0 ? 0 : -1;
        loadSelectedLog();
    }

    private void loadSelectedLog() {
        if (selectedIndex < 0 || selectedIndex >= crashFiles.size) {
            detail = "no crash log selected";
            return;
        }

        try {
            detail = crashFiles.get(selectedIndex).readString("UTF-8");
        } catch (Exception e) {
            detail = "Failed to read: " + e.getMessage();
        }
    }

    private void printSelectedLog() {
        if (selectedIndex < 0 || selectedIndex >= crashFiles.size) {
            Gdx.app.log("Crash", "no crash log selected");
            return;
        }

        FileHandle file = crashFiles.get(selectedIndex);
        String content = file.readString("UTF-8");

        Gdx.app.log("Crash", file.name() + "\n" + content);
        System.out.println(content);
    }

    private void showOnPhone() {
        // 手机端直接在当前面板显示日志详情。
        // 详情始终绘制在面板右侧，因此无需额外窗口。
        loadSelectedLog();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isVisible()) return;

        batch.end();

        float x = getX();
        float y = getY();
        float w = getWidth();
        float h = getHeight();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.08f, 0.09f, 0.12f, 1);
        shapeRenderer.rect(x, y, w, h);

        shapeRenderer.setColor(0.16f, 0.18f, 0.23f, 1);
        shapeRenderer.rect(x, y + h - 60, w, 60);
        shapeRenderer.rect(x + 10, y + 60, 280, h - 140);
        shapeRenderer.rect(x + 300, y + 60, w - 310, h - 140);

        drawButton(x + 10, y + h - 50, 120, 40);
        drawButton(x + w - 100, y + h - 50, 90, 40);
        drawButton(x + 130, y + 8, 150, buttonHeight);
        drawButton(x + 290, y + 8, 150, buttonHeight);

        shapeRenderer.end();

        batch.begin();
        font.setColor(Color.WHITE);

        font.draw(batch, "Crash Logs", x + 20, y + h - 25);

        font.draw(batch, "Count: " + crashFiles.size, x + 180, y + h - 25);

        font.draw(batch, "flush", x + 45, y + h - 25 - 25);
        font.draw(batch, "close", x + w - 80, y + h - 25 - 25);
        font.draw(batch, "print console", x + 145, y + 34);
        font.draw(batch, "show on phone", x + 305, y + 34);;

        // 左侧崩溃文件列表
        float itemY = y + h - 90 + scrollY;
        for (int i = 0; i < crashFiles.size; i++) {
            if (itemY < y + 65 || itemY > y + h - 65) {
                itemY -= 34;
                continue;
            }

            if (i == selectedIndex) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(0.18f, 0.35f, 0.55f, 1);
                shapeRenderer.rect(x + 15, itemY - 25, 270, 30);
                shapeRenderer.end();
            }

            font.draw(batch, crashFiles.get(i).name(), x + 22, itemY);
            itemY -= 34;
        }

        // 右侧日志详情
        float detailY = y + h - 85;
        float lineHeight = 20;
        String[] lines = detail.split("\n");

        for (String line : lines) {
            if (detailY < y + 65) break;

            font.draw(batch, line, x + 310, detailY, w - 330,
                    com.badlogic.gdx.utils.Align.left, true);
            detailY -= lineHeight;
        }

        batch.end();
        super.draw(batch, parentAlpha);
        batch.begin();
    }

    private void drawButton(float x, float y, float w, float h) {
        shapeRenderer.setColor(0.20f, 0.24f, 0.31f, 1);
        shapeRenderer.rect(x, y, w, h);
    }

    private class ClickableArea extends Actor {
        private final Runnable action;

        ClickableArea(float x, float y, float width, float height,
                      Runnable action) {
            setBounds(x, y, width, height);
            this.action = action;

            addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    ClickableArea.this.action.run();
                }
            });
        }
    }

    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }
}
