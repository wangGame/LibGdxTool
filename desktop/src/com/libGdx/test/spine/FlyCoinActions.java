package com.libGdx.test.spine;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class FlyCoinActions {

    public interface TargetGetter {
        Vector2 getTargetCenter();
    }

    private static class CoinKey {
        float startTime;
        float p1Time;
        float p2Time;
        float downEndTime;
        float endTime;

        float p1x, p1y;
        float p2x, p2y;

        CoinKey(
                float startTime,
                float p1Time,
                float p2Time,
                float downEndTime,
                float endTime,
                float p1x,
                float p1y,
                float p2x,
                float p2y
        ) {
            this.startTime = startTime;
            this.p1Time = p1Time;
            this.p2Time = p2Time;
            this.downEndTime = downEndTime;
            this.endTime = endTime;
            this.p1x = p1x;
            this.p1y = p1y;
            this.p2x = p2x;
            this.p2y = p2y;
        }
    }

    private static final CoinKey[] KEYS = new CoinKey[] {
            new CoinKey(0.0000f, 0.4000f, 1.0000f, 1.1333f, 1.4333f,   51.85f,  36.69f,   11.09f,  19.44f),
            new CoinKey(0.0667f, 0.4667f, 0.9667f, 1.1000f, 1.4000f, -102.46f, -83.43f,  -51.26f, -54.34f),
            new CoinKey(0.1333f, 0.5333f, 0.9333f, 1.0667f, 1.3667f,  104.64f, -78.71f,   84.26f, -58.33f),
            new CoinKey(0.2000f, 0.6000f, 0.9000f, 1.0333f, 1.3333f,  -54.56f,  99.51f,  -38.89f,  71.29f),
            new CoinKey(0.2667f, 0.6667f, 0.8667f, 1.0000f, 1.3000f,  107.98f,  62.82f,   90.74f,  51.85f),
            new CoinKey(0.3333f, 0.7333f, 0.8333f, 0.9667f, 1.2667f, -145.09f,  43.49f, -123.14f,  35.65f),
            new CoinKey(0.4000f, 0.8000f, 0.8000f, 0.9333f, 1.2333f,    9.72f, -93.98f,    9.72f, -93.98f)
    };

    /**
     * 播放飞金币动画
     *
     * @param layer 金币要添加到的层
     * @param coinImages 7个金币Image
     * @param startCenterX 起点中心x
     * @param startCenterY 起点中心y
     * @param targetGetter 目标中心点，飞行时动态获取
     */
    public static void play(
            Group layer,
            Image[] coinImages,
            float startCenterX,
            float startCenterY,
            TargetGetter targetGetter
    ) {
        for (int i = 0; i < coinImages.length && i < KEYS.length; i++) {
            Image coin = coinImages[i];
            CoinKey key = KEYS[i];

            coin.clearActions();
            coin.setOrigin(Align.center);
            coin.setScale(0.5f);
            coin.getColor().a = 0f;
            coin.setVisible(true);

            coin.setPosition(startCenterX, startCenterY, Align.center);

            if (coin.getParent() == null) {
                layer.addActor(coin);
            }

            coin.addAction(Actions.parallel(
                    createAlphaAction(key),
                    createScaleAction(key),
                    createMoveAction(coin, key, startCenterX, startCenterY, targetGetter)
            ));
        }
    }

    private static Action createAlphaAction(CoinKey key) {
        return Actions.sequence(
                Actions.delay(key.startTime),
                Actions.fadeIn(0.0667f, Interpolation.sineOut)
        );
    }

    private static Action createScaleAction(CoinKey key) {
        float scaleUpTime = 0.1667f;
        float scaleBackTime = 0.2333f;

        float scaleFinishTime = key.startTime + scaleUpTime + scaleBackTime;
        float waitBeforeFlyScale = Math.max(0f, key.downEndTime - scaleFinishTime);

        return Actions.sequence(
                Actions.delay(key.startTime),

                Actions.scaleTo(0.5f, 0.5f),

                // 0.5 -> 1.593，弹出
                Actions.scaleTo(1.593f, 1.593f, scaleUpTime, Interpolation.swingOut),

                // 1.593 -> 1.3，回弹
                Actions.scaleTo(1.3f, 1.3f, scaleBackTime, Interpolation.sine),

                Actions.delay(waitBeforeFlyScale),

                // 飞走时缩小
                Actions.scaleTo(
                        0.885f,
                        0.885f,
                        key.endTime - key.downEndTime,
                        Interpolation.sineIn
                )
        );
    }

    private static Action createMoveAction(
            Actor coin,
            CoinKey key,
            float startCenterX,
            float startCenterY,
            TargetGetter targetGetter
    ) {
        float p1x = startCenterX + key.p1x;
        float p1y = startCenterY + key.p1y;

        float p2x = startCenterX + key.p2x;
        float p2y = startCenterY + key.p2y;

        float d1 = key.p1Time - key.startTime;
        float d2 = key.p2Time - key.p1Time;
        float d3 = key.downEndTime - key.p2Time;
        float d4 = key.endTime - key.downEndTime;

        return Actions.sequence(
                Actions.delay(key.startTime),

                moveToCenter(coin, p1x, p1y, d1, Interpolation.sineOut),

                d2 > 0f
                        ? moveToCenter(coin, p2x, p2y, d2, Interpolation.sine)
                        : Actions.run(() -> coin.setPosition(p2x, p2y, Align.center)),

                // Spine里飞之前有一个向下压的动作，大概 -80.27
                Actions.moveBy(0f, -80.27f, d3, Interpolation.sineIn),

                // 最后飞向动态目标
                moveToDynamicTargetCenter(targetGetter, d4, Interpolation.sineIn),

                Actions.fadeOut(0.05f),
                Actions.removeActor()
        );
    }

    private static Action moveToCenter(
            Actor actor,
            float centerX,
            float centerY,
            float duration,
            Interpolation interpolation
    ) {
        return Actions.moveTo(
                centerX - actor.getWidth() / 2f,
                centerY - actor.getHeight() / 2f,
                duration,
                interpolation
        );
    }

    /**
     * 动态目标点移动。
     * 适合你的“目标位置是变化的”的情况。
     * 飞行开始后，每一帧都会重新取目标位置。
     */
    private static Action moveToDynamicTargetCenter(
            TargetGetter targetGetter,
            float duration,
            Interpolation interpolation
    ) {
        return new TemporalAction(duration, interpolation) {
            private float startX;
            private float startY;

            @Override
            protected void begin() {
                Actor actor = getActor();
                startX = actor.getX();
                startY = actor.getY();
            }

            @Override
            protected void update(float percent) {
                Actor actor = getActor();
                Vector2 target = targetGetter.getTargetCenter();

                float targetX = target.x - actor.getWidth() / 2f;
                float targetY = target.y - actor.getHeight() / 2f;

                float x = startX + (targetX - startX) * percent;
                float y = startY + (targetY - startY) * Interpolation.		pow2In.apply(percent);

                actor.setPosition(x, y);
            }
        };
    }
}