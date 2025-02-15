package com.libGdx.test.bianyuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

import java.util.ArrayList;
import java.util.List;

public class App extends LibGdxTestMain {
    private ShapeRenderer shapeRenderer;

    private List<Vector2> contourPoints;
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void create() {
        super.create();
        shapeRenderer = new ShapeRenderer();

        // 加载图像并提取轮廓
        Pixmap pixmap = new Pixmap(Gdx.files.internal("assets/tuceng16.png"));
        contourPoints = extractContours(pixmap);
        pixmap.dispose();
    }

    private List<Vector2> extractContours(Pixmap pixmap) {
        List<Vector2> contours = new ArrayList<>();

        // 遍历图像像素，识别轮廓（边界）
        for (int y = 1; y < pixmap.getHeight() - 1; y++) {
            for (int x = 1; x < pixmap.getWidth() - 1; x++) {
                Color currentColor = new Color(pixmap.getPixel(x, y));
                Color rightColor = new Color(pixmap.getPixel(x + 1, y));
                Color downColor = new Color(pixmap.getPixel(x, y + 1));

                // 比较当前像素与右边、下方像素的透明度差异
                if (Math.abs(currentColor.a - rightColor.a) > 0.1f || Math.abs(currentColor.a - downColor.a) > 0.1f) {
                    contours.add(new Vector2(x, y));
                }
            }
        }
        return contours;
    }

    @Override
    public void render() {
        super.render();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // 绘制轮廓线
        shapeRenderer.setColor(Color.RED);

        // 绘制连接轮廓点的线
        for (int i = 0; i < contourPoints.size() - 1; i++) {
            Vector2 pointA = contourPoints.get(i);
            Vector2 pointB = contourPoints.get(i + 1);
            shapeRenderer.line(pointA.x, pointA.y, pointB.x, pointB.y);
        }

        shapeRenderer.end();
    }
}
