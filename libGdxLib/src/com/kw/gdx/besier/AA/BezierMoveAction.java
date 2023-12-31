package com.kw.gdx.besier.AA;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.trail.PictureTrail;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/25 21:08
 */

public class BezierMoveAction extends TemporalAction {
    private Bezier<Vector2> vector2Bezier;
    Vector2 tempPosition = new Vector2();
    PictureTrail pictureTrail;

    public void setBezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        setBezier(new Vector2(x1, y1), new Vector2(x2, y2), new Vector2(x3, y3), new Vector2(x4, y4));
    }

    public void setBezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int align) {
        setBezier(new Vector2(x1, y1), new Vector2(x2, y2), new Vector2(x3, y3), new Vector2(x4, y4), align);
    }

    public void setBezier(Vector2 v1, Vector2 v2, Vector2 v3, Vector2 v4) {
        setBezier(v1, v2, v3, v4, Align.bottomLeft);
    }

    public void setBezier(Vector2 v1, Vector2 v2, Vector2 v3, Vector2 v4, int align) {
        vector2Bezier = new Bezier<>();
        vector2Bezier.set(v1, v2, v3, v4);
    }

    float angele = 0;//变换的角度
    float delayTime;

    public void setDelayTime(float delayTime) {
        this.delayTime = delayTime;
    }

    public void setPictureTrail(PictureTrail pictureTrail) {
        this.pictureTrail = pictureTrail;
    }

    float time = 0;

    @Override
    protected void update(float percent) {
        tempPosition = vector2Bezier.valueAt(tempPosition, percent);
        actor.setPosition(tempPosition.x,tempPosition.y);
    }
}
