package com.libGdx.test.sc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * 单条列表 item。
 *
 * 注意：
 * 这个 Actor 不会创建 500 个。
 * 它只会创建屏幕可见数量 + 少量缓冲数量。
 */
public class ItemActor extends Actor {

    private final Texture whiteTexture;
    private final BitmapFont font;

    private final Color oldBatchColor = new Color();

    private String text = "";
    private int dataIndex = -1;

    public ItemActor(Texture whiteTexture, BitmapFont font) {
        this.whiteTexture = whiteTexture;
        this.font = font;

        setTouchable(Touchable.enabled);
    }

    public void setData(String text, int dataIndex) {
        this.text = text;
        this.dataIndex = dataIndex;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        oldBatchColor.set(batch.getColor());

        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();

        if (dataIndex % 2 == 0) {
            batch.setColor(0.18f, 0.22f, 0.30f, 1f * parentAlpha);
        } else {
            batch.setColor(0.24f, 0.28f, 0.36f, 1f * parentAlpha);
        }

        batch.draw(whiteTexture, x, y + 2, width, height - 4);

        batch.setColor(0.10f, 0.12f, 0.16f, 1f * parentAlpha);
        batch.draw(whiteTexture, x, y, width, 2);

        font.setColor(1f, 1f, 1f, parentAlpha);
        font.draw(batch, text, x + 24, y + height / 2f + 8);

        font.setColor(0.7f, 0.8f, 1f, parentAlpha);
        font.draw(batch, "data index = " + dataIndex, x + 220, y + height / 2f + 8);

        batch.setColor(oldBatchColor);
    }
}
