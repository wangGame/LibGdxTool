package com.libGdx.test.pan;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class GdxPanTest extends LibGdxTestMain {

    public static void main(String[] args) {
        GdxPanTest test = new GdxPanTest();
        test.start();
    }

    private Image testImg;

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        testImg = new Image(
                Asset.getAsset().getTexture("ball.png")
        );

        testImg.setPosition(400, 300);

        addActor(testImg);

        stage.addCaptureListener(new ActorGestureListener() {

            @Override
            public void pan(InputEvent event,
                            float x,
                            float y,
                            float deltaX,
                            float deltaY) {

                super.pan(event, x, y, deltaX, deltaY);

                // =========================
                // 跟随移动
                // =========================

                testImg.clearActions();

                testImg.addAction(
                        Actions.moveToAligned(
                                x,
                                y,
                                Align.center,
                                0.05f
                        )
                );

                // =========================
                // 计算速度
                // =========================

                float speed = (float)Math.sqrt(
                        deltaX * deltaX +
                                deltaY * deltaY
                );

                // =========================
                // 限制范围
                // =========================

                float t = MathUtils.clamp(
                        speed / 50f,
                        0f,
                        1f
                );

                // =========================
                // 颜色变化
                // 速度越快越黑
                // =========================

                float c = 1f - t;

                testImg.setColor(
                        1 * c,
                        0,
                        0,
                        1f
                );
            }

            @Override
            public void panStop(InputEvent event,
                                float x,
                                float y,
                                int pointer,
                                int button) {

                super.panStop(event, x, y, pointer, button);

                // 松手恢复颜色
                testImg.addAction(
                        Actions.color(
                                Color.WHITE,
                                1f
                        )
                );
            }
        });
    }
}