package com.kw.gdx.cirprogress;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ShortArray;

public class CircleProgress extends Actor {
    private TextureRegion textureRegion;//裁剪画的纹理
    private TextureRegion point;
    private Vector2 center;
    private Vector2 centerTop;//从上面中间开始
    private Vector2 leftTop;
    private Vector2 leftBottom;
    private Vector2 rightBottom;
    private Vector2 rightTop;
//    private Vector2 progressPoint;
    private float[] fv;//裁剪画图使用的点阵{point1.x,point1.y,point2.x,point2.y  ......}
    private Vector2 intersectPoint;//当前切割在边上的点
    private float percent = 0;
    //当前正在切割的位置
    private IntersectAt intersectAt;
    private boolean revert = false;
    private float radius;
    private float targetPercent;
    private float speed = 1;
    private ProgressListener listener;
    private boolean start = false;

    //当前切割位置的枚举
    public enum IntersectAt {
        NONE, TOP, BOTTOM, LEFT, RIGHT;
    }

    //前两个参数边框，
    public CircleProgress(TextureRegion ground, TextureRegion point, float radius) {
        setName("circle");
        setSize(ground.getRegionWidth(), ground.getRegionHeight());
        this.textureRegion = ground;
        this.point = point;
        this.radius = radius;

        //计算各点内部坐标
        center = new Vector2(this.getWidth() / 2, this.getHeight() / 2);
        centerTop = new Vector2(this.getWidth() / 2, this.getHeight());
        leftTop = new Vector2(0, this.getHeight());
        leftBottom = new Vector2(0, 0);
        rightBottom = new Vector2(this.getWidth(), 0);
        rightTop = new Vector2(this.getWidth(), this.getHeight());
//        progressPoint = new Vector2(this.getWidth() / 2, this.getHeight() / 2);
    }

    /**
     * 反转
     * @param revert
     */
    public void setRevert(boolean revert) {
        this.revert = revert;
    }

    //计算切线的最远点
    private Vector2 IntersectPoint(Vector2 line) {
        Vector2 v = new Vector2();
        boolean isIntersect;

        //check top
        isIntersect = Intersector.intersectSegments(leftTop, rightTop, center, line, v);//切割线和上边的交点v

        //check bottom
        if (isIntersect) {
            intersectAt = IntersectAt.TOP;
            return v;
        } else isIntersect = Intersector.intersectSegments(leftBottom, rightBottom, center, line, v);

        //check left
        if (isIntersect) {
            intersectAt = IntersectAt.BOTTOM;
            return v;
        } else isIntersect = Intersector.intersectSegments(leftTop, leftBottom, center, line, v);

        //check bottom
        if (isIntersect) {
            intersectAt = IntersectAt.LEFT;
            return v;
        } else isIntersect = Intersector.intersectSegments(rightTop, rightBottom, center, line, v);

        if (isIntersect) {
            intersectAt = IntersectAt.RIGHT;
            return v;
        } else {
            intersectAt = IntersectAt.NONE;
            return null;
        }
    }


    public void setTargetPercent(float targetPercent,boolean now,float speed,ProgressListener listener) {
        this.targetPercent = targetPercent;
        this.listener = listener;
        this.speed = speed;
        if(now){
            setPercentage(targetPercent);
            if(listener!=null) {
                listener.end();
            }
        }else{
            start = true;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(start){
            if(speed>0){
                if(percent + speed<targetPercent){
                    setPercentage(percent+ speed);
                }else{
                    setPercentage(targetPercent);
                    if(listener!=null){
                        start = false;
                        listener.end();
                    }
                }
            }else{
                if(percent+speed> targetPercent){
                    setPercentage(percent+speed);
                }else{
                    setPercentage(targetPercent);
                    if(listener!=null){
                        start = false;
                        listener.end();
                    }
                }
            }
        }
    }

    //设置百分比，顺时针
    public void setPercentage(float percent) {
        if (percent < 0.01f){
            percent = 0;
        }
        if (percent >= 100) {
            percent = 100F;
        }
        this.percent = percent;
        float angle = convertToRadians(90); //percent = 0 => angle = -90
        angle -= convertToRadians(percent * 360 / 100);

        float len = this.getWidth() > this.getHeight() ? this.getWidth() : this.getHeight();
        float dy = (float) (Math.sin(angle) * len);
        float dx = (float) (Math.cos(angle) * len);
        Vector2 line = new Vector2(center.x + dx, center.y + dy);
        intersectPoint = IntersectPoint(line);
        if (intersectAt == IntersectAt.TOP) {
            if (intersectPoint.x >= this.getWidth() / 2) {
                fv = new float[]{
                        center.x,
                        center.y,
                        centerTop.x,
                        centerTop.y,
//                        leftTop.x,
//                        leftTop.y,
//                        leftBottom.x,
//                        leftBottom.y,
//                        rightBottom.x,
//                        rightBottom.y,
//                        rightTop.x,
//                        rightTop.y,
                        intersectPoint.x,
                        intersectPoint.y
                };
            } else {
                fv = new float[]{ // cắt bên trái cạnh
                        center.x,
                        center.y,
                        centerTop.x,
                        centerTop.y,
                        rightTop.x,
                        rightTop.y,
                        rightBottom.x,
                        rightBottom.y,
                        leftBottom.x,
                        leftBottom.y,
                        leftTop.x,
                        leftTop.y,
                        intersectPoint.x,
                        intersectPoint.y
                };
            }
        } else if (intersectAt == IntersectAt.BOTTOM) {
            fv = new float[]{
                    center.x,
                    center.y,
                    centerTop.x,
                    centerTop.y,
                    rightTop.x,
                    rightTop.y,
                    rightBottom.x,
                    rightBottom.y,
                    intersectPoint.x,
                    intersectPoint.y
            };

        } else if (intersectAt == IntersectAt.LEFT) {
            fv = new float[]{
                    center.x,
                    center.y,
                    centerTop.x,
                    centerTop.y,
                    rightTop.x,
                    rightTop.y,
                    rightBottom.x,
                    rightBottom.y,
                    leftBottom.x,
                    leftBottom.y,
                    intersectPoint.x,
                    intersectPoint.y
            };

        } else if (intersectAt == IntersectAt.RIGHT) {
            fv = new float[]{
                    center.x,
                    center.y,
                    centerTop.x,
                    centerTop.y,
                    rightTop.x,
                    rightTop.y,
//                    leftBottom.x,
//                    leftBottom.y,
//                    rightBottom.x,
//                    rightBottom.y,
                    intersectPoint.x,
                    intersectPoint.y
            };
        } else {
            fv = null;
        }
        EarClippingTriangulator e = new EarClippingTriangulator();
        ShortArray sv = e.computeTriangles(fv);
        PolygonRegion polyReg = new PolygonRegion(textureRegion, fv, sv.toArray());
        //创建 polySprite.
        if (poly == null) {
            poly = new PolygonSprite(polyReg);
        } else {
            poly.setRegion(polyReg);
        }
        if (revert) {
            poly.setScale(-1, 1);
        } else {
            poly.setScale(1, 1);
        }


    }

    PolygonSprite poly;

    //重新绘制函数
    @Override
    public void draw(Batch batch, float parentAlpha) {
//        //裁剪

        //创建 polygonRegion.

        //(position, origin, rotation, color)
        //设置中心点
        if (percent <= 0) {

        } else if (percent >= 99.9F) {
            Color batchColor = batch.getColor();
            batch.setColor(getColor());
            batch.draw(textureRegion, getX(), getY());
            batch.setColor(batchColor);
        } else if (poly != null) {
            poly.setOrigin(this.getOriginX(), this.getOriginY());
            //设置位置
            if (revert) {
                poly.setPosition(this.getX() + centerTop.x * 2, this.getY());
            } else {
                poly.setPosition(this.getX(), this.getY());
            }
            poly.setRotation(this.getRotation());
            poly.setColor(this.getColor());
            //绘制

            float an = percent / 100 * 360;
            float pointX = (float) (radius * Math.sin(Math.PI * an / 180.0));
            float pointY = (float) (radius * Math.cos(Math.PI * an / 180.0));
            if(revert){
                batch.draw(point, getX() + getWidth() / 2 - pointX + -point.getRegionWidth() / 2, getY() + getHeight() / 2 + pointY - point.getRegionHeight() / 2);
                batch.draw(point, getX() + getWidth() / 2 + -point.getRegionWidth() / 2, getY() + getHeight() / 2 + radius - point.getRegionHeight() / 2);
            }else {
                batch.draw(point, getX() + getWidth() / 2 + pointX + -point.getRegionWidth() / 2, getY() + getHeight() / 2 + pointY - point.getRegionHeight() / 2);
                batch.draw(point, getX() + getWidth() / 2 + -point.getRegionWidth() / 2, getY() + getHeight() / 2 + radius - point.getRegionHeight() / 2);
            }
            poly.draw((PolygonSpriteBatch) batch);
//            batch.draw(point);
//            if (starPoint!=null){
//                starPoint.setPosition(
//                        (float) (progress.getX(Align.center)+r*Math.sin(Math.PI*0/180.0)),
//                        (float) (progress.getY(Align.center)+r*Math.cos(Math.PI*0/180.0)),
//                        Align.center);
//            }
        }
    }

    float convertToDegrees(float angleInRadians) {
        float angleInDegrees = angleInRadians * 57.2957795f;
        return angleInDegrees;
    }

    float convertToRadians(float angleInDegrees) {
        float angleInRadians = angleInDegrees * 0.0174532925f;
        return angleInRadians;
    }

    public interface ProgressListener{
        void end();
    }

}