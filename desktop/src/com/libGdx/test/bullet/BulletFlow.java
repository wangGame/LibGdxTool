package com.libGdx.test.bullet;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;

import org.lwjgl.openal.AL;

import java.util.AbstractList;

public class BulletFlow extends Group {
    private Image targetA;
    private Image sourceA;
    private Vector2 targetAV2;
    private Vector2 sourceV2;
    public BulletFlow(){
        targetA = new Image(Asset.getAsset().getTexture("7.png"));
        sourceA = new Image(Asset.getAsset().getTexture("ball.png"));
        targetA.setTouchable(Touchable.disabled);
        sourceA.setTouchable(Touchable.disabled);
        addActor(targetA);
        addActor(sourceA);
        targetAV2 = new Vector2();
        sourceV2 = new Vector2();
        setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        sourceA.setOrigin(Align.center);
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                targetA.setPosition(x,y);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                targetA.setPosition(x,y);
            }
        });
    }

    private float bulletSpeed = 300;

    @Override
    public void act(float delta) {
        super.act(delta);
//        targetAV2.set(targetA.getX(Align.center),targetA.getY(Align.center));
//        sourceV2.set(sourceA.getX(Align.center),sourceA.getY(Align.center));
//
//
//        Vector2 normalizeVec = targetAV2.sub(sourceV2).nor();
//        this.sourceA.setX(this.sourceA.getX(Align.center) + normalizeVec.x * this.bulletSpeed * delta);
//        this.sourceA.setY(this.sourceA.getY(Align.center) + normalizeVec.y * this.bulletSpeed * delta);
//        // 角度变化以y轴正方向为起点，逆时针角度递增
//        Vector2 vs = new Vector2(0,1);
//        this.sourceA.setRotation((float) (vs.angle(normalizeVec) * 180 / Math.PI));

//        let rect = this.target.getBoundingBox();
//        if (rect.contains(bulletPos)) this.hitTheTarget();

        // 取中心点
        targetAV2.set(targetA.getX(Align.center), targetA.getY(Align.center));
        sourceV2.set(sourceA.getX(Align.center), sourceA.getY(Align.center));

        // 新建方向向量（不能用 targetAV2.sub(...)，会修改自身）
        Vector2 direction = new Vector2(
                targetAV2.x - sourceV2.x,
                targetAV2.y - sourceV2.y
        ).nor();

        // 移动子弹
        sourceA.moveBy(direction.x * bulletSpeed * delta,
                direction.y * bulletSpeed * delta);

        // 旋转（以 Y+ 为基准，逆时针递增）
        Vector2 up = new Vector2(0, 1);
        float angle = up.angle(direction);  // LibGDX 返回的就是角度
        sourceA.setRotation(angle);

        // 碰撞判断（用 contains）
//        if (targetA.getBoundingRectangle().contains(sourceA.getX(Align.center), sourceA.getY(Align.center))) {
//            hitTarget();
//        }
    }
}
