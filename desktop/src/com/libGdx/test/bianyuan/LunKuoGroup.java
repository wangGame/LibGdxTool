package com.libGdx.test.bianyuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

import java.util.ArrayList;
import java.util.List;

public class LunKuoGroup extends Group {
    private ShapeRenderer shapeRenderer;
    private List<int[]> contours;

    public LunKuoGroup() {
        shapeRenderer = new ShapeRenderer();
        Pixmap pixmap = new Pixmap(Gdx.files.internal("assets/tuceng16.png"));
        contours = extractContours(pixmap);
        pixmap.dispose();

        Texture texture = Asset.getAsset().getTexture("assets/tuceng16.png");
        TextureRegion region = new TextureRegion(texture);
        region.flip(false, true);
        Image image = new Image(region);
        addActor(image);
    }


    private List<int[]> extractContours(Pixmap pixmap) {
        List<int[]> contours = new ArrayList<>();
        // 遍历图像像素，识别轮廓（边界）
        for (int y = 1; y < pixmap.getHeight() - 1; y++) {
            for (int x = 1; x < pixmap.getWidth() - 1; x++) {
                Color currentColor = new Color(pixmap.getPixel(x, y));
                Color rightColor = new Color(pixmap.getPixel(x + 1, y));
                Color downColor = new Color(pixmap.getPixel(x, y + 1));

                // 比较当前像素与右边、下方像素的透明度差异
                if (Math.abs(currentColor.a - rightColor.a) > 0.1f || Math.abs(currentColor.a - downColor.a) > 0.1f) {
                    contours.add(new int[]{x, y});
                }
            }
        }
        return contours;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // 绘制轮廓点
        for (int[] point : contours) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(point[0], point[1], 4);
        }

        shapeRenderer.end();
        batch.begin();
        super.draw(batch, parentAlpha);

    }
}

