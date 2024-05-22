package com.libGdx.test.testRo;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Vector2 start = new Vector2(0,0);
        Vector2 end = new Vector2(10,10);
        float v = calculateAngle(start.x, start.y, end.x, end.y);
        Image i = new Image(Asset.getAsset().getTexture("7.png"));
        addActor(i);
        i.setOrigin(Align.center);
        i.setRotation(v);
    }

    public float calculateAngle(float x1, float y1, float x2, float y2) {
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;
        float angleRad = MathUtils.atan2(deltaY, deltaX);
        float angleDeg = MathUtils.radiansToDegrees * angleRad; // 转换为度数
        return angleDeg;
    }
}
