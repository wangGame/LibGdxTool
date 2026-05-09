package com.libGdx.test.path;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.constant.Constant;

public class PathGroup extends Group {
    private ShapeRenderer shapeRenderer;
    private static final float MIN_DIST = 6f;
    private Array<Vector2> vector2s = new Array<>();
    private Array<Vector2> smoothPoints = new Array<>();
    private CatmullRomSpline<Vector2> spline;
    public PathGroup(){
        setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        shapeRenderer = new ShapeRenderer();

        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                vector2s.clear();
                vector2s.add(new Vector2(x,y));
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
//                vector2s.add(new Vector2(event.getStageX(),event.getStageY()));

                Vector2 last = vector2s.peek();
                float dx = x - last.x;
                float dy = y - last.y;

                if (dx * dx + dy * dy > MIN_DIST * MIN_DIST) {
                    vector2s.add(new Vector2(x, y));
                }
                if (vector2s.size < 4) return;

                spline = new CatmullRomSpline<>(vector2s.toArray(Vector2.class), false);

                smoothPoints.clear();

                Vector2 tmp = new Vector2();
                int sample = vector2s.size * 5; // 越大越平滑

                for (int i = 0; i < sample; i++) {
                    float t = i / (float)(sample - 1);
                    spline.valueAt(tmp, t);
                    smoothPoints.add(new Vector2(tmp));
                }

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (vector2s.size < 4) return;

                spline = new CatmullRomSpline<>(vector2s.toArray(Vector2.class), false);

                smoothPoints.clear();

                Vector2 tmp = new Vector2();
                int sample = vector2s.size * 5; // 越大越平滑

                for (int i = 0; i < sample; i++) {
                    float t = i / (float)(sample - 1);
                    spline.valueAt(tmp, t);
                    smoothPoints.add(new Vector2(tmp));
                }
            }
        });
    }

    /**
     * 设置宽度之后，它的接口处不是很齐
     *
     * 可以自己来组合顶点
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (smoothPoints.size < 2) return;

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(10);
        for (int i = 1; i < smoothPoints.size; i++) {
            shapeRenderer.line(smoothPoints.get(i - 1), smoothPoints.get(i));
        }

        shapeRenderer.end();

        batch.begin();
    }
}