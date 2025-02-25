package com.libGdx.test.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    private ShapeRenderer shapeRenderer;
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render() {
        super.render();
        if (shapeRenderer!=null){

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); // 使用填充模式绘制
            // 绘制一个圆，参数为圆心(x, y)，半径，颜色
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(1100, 300, 900,360); // 在(400, 300)处绘制半径为100的圆

            shapeRenderer.end();
        }
    }
}
