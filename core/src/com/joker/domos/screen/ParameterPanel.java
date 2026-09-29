package com.joker.domos.screen;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;
import com.kw.gdx.asset.Asset;


public class ParameterPanel extends Actor implements Disposable {
    private static final float[] MIN = {0f, 1f, 0f, 0f};
    private static final float[] MAX = {100f, 15f, 215f, 1f};
    private static final String[] LABEL = {"Blur radius", "Blur rounds", "Corner radius", "White overlay"};
    private final BlurGlass target;
    private final Texture pixel = makePixel();
    private final BitmapFont font = Asset.getAsset().loadBitFont("font/Cali_75.fnt");
    private final float[] values = {45f, 2f, 65f, 128f / 255f};
    private int dragging = -1;

    public ParameterPanel(BlurGlass target) {
        this.target = target;
        font.getData().setScale(1.05f);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dragging = rowAt(y);
                if (dragging < 0) return false;
                update(dragging, x);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (dragging >= 0) update(dragging, x);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dragging = -1;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(0.035f, 0.045f, 0.08f, 0.92f * parentAlpha);
        batch.draw(pixel, getX(), getY(), getWidth(), getHeight());
        float left = getX() + 36f;
        float trackWidth = getWidth() - 72f;
        for (int row = 0; row < 4; row++) {
            float y = getY() + getHeight() - 76f - row * 88f;
            String value = row == 3 ? MathUtils.round(values[row] * 100f) + "%" : Integer.toString(MathUtils.round(values[row]));
            font.setColor(1f, 1f, 1f, parentAlpha);
            font.draw(batch, LABEL[row] + "  " + value, left, y + 47f);
            batch.setColor(1f, 1f, 1f, 0.22f * parentAlpha);
            batch.draw(pixel, left, y, trackWidth, 10f);
            float ratio = (values[row] - MIN[row]) / (MAX[row] - MIN[row]);
            batch.setColor(0.43f, 0.72f, 1f, parentAlpha);
            batch.draw(pixel, left, y, trackWidth * ratio, 10f);
            batch.draw(pixel, left + trackWidth * ratio - 13f, y - 13f, 26f, 36f);
        }
        batch.setColor(Color.WHITE);
    }

    private int rowAt(float y) {
        for (int row = 0; row < 4; row++) {
            float trackY = getHeight() - 76f - row * 88f;
            if (y >= trackY - 28f && y <= trackY + 38f) return row;
        }
        return -1;
    }

    private void update(int row, float x) {
        float ratio = MathUtils.clamp((x - 36f) / (getWidth() - 72f), 0f, 1f);
        values[row] = MIN[row] + (MAX[row] - MIN[row]) * ratio;
        if (row == 0) target.setBlurRadius(values[row]);
        else if (row == 1) {
            values[row] = MathUtils.round(values[row]);
            target.setBlurRounds(values[row]);
        } else if (row == 2) target.setCornerRadius(values[row]);
        else target.setOverlayAlpha(values[row]);
    }

    @Override
    public void dispose() {
        font.dispose();
        pixel.dispose();
    }

    private static Texture makePixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
