package com.kw.gdx.group;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.action.NumAction;
import com.kw.gdx.action.NumActionListener;

public class PageView extends Group {

    private final ScrollPane pane;
    private final LayoutGroup table;

    private float lastScrollY;
    private float lastScrollX;
    private boolean wasScrolling = false;
    private boolean snapping = false;
    /**
     * true  = 纵向滑动
     * false = 横向滑动
     */
    private boolean isVerticalScrolling;

    /**
     * 中间最大的缩放
     */
    private float maxScale = 1.4f;

    /**
     * 两边最小的缩放
     */
    private float minScale = 1f;

    /**
     * 影响范围。
     * <= 0 时自动使用 ScrollPane 宽/高的一半
     */
    private float scaleDistance = -1;

    private final Vector2 actorCenter = new Vector2();
    private final Vector2 paneCenter = new Vector2();
    private float widthView;
    public PageView(float width, float height) {
        this(width, height, false);
    }

    public PageView(float width, float height, boolean vertical) {
        this.isVerticalScrolling = vertical;
        this.widthView = width;
        table = new LayoutGroup();
        table.setPadding(20);
        table.setWidth(0);
        table.setHeight(height);
        pane = new ScrollPane(table) {

            @Override
            public void act(float delta) {
                super.act(delta);
                if (isFlickScrollTouchUp()) {
                    if (isVerticalScrolling) {
                        float scrollY = getVisualScrollY();

                        if (!MathUtils.isEqual(lastScrollY, scrollY)) {
                            lastScrollY = scrollY;
                            updateVisualScrollY();
                            wasScrolling = true;
                        }

                    } else {
                        float scrollX = getVisualScrollX();

                        if (!MathUtils.isEqual(lastScrollX, scrollX)) {
                            lastScrollX = scrollX;
                            updateVisualScrollX();
                            wasScrolling = true;
                        }
                    }
                }
//                if (wasScrolling
//                        && !isPanning()
//                        && !isFlinging()
//                        && !snapping) {
//
//                    wasScrolling = false;
//
//                    snapToNearest();
//                }

                if (wasScrolling && !isFlickScrollTouchUp()){
                    wasScrolling = false;
                    snapToNearest();
                }

                /*
                 * 判断自动吸附是否完成
                 */
                if (snapping) {

                    if (isVerticalScrolling) {

                        if (MathUtils.isEqual(
                                getVisualScrollY(),
                                getScrollY(),
                                0.5f
                        )) {
                            snapping = false;
                        }

                    } else {

                        if (MathUtils.isEqual(
                                getVisualScrollX(),
                                getScrollX(),
                                0.5f
                        )) {
                            snapping = false;
                        }
                    }
                }
            }
        };

        pane.setSize(width, height);
        // 根据方向禁止另一方向滚动
        if (vertical) {
            pane.setScrollingDisabled(true, false);
        } else {
            pane.setScrollingDisabled(false, true);
        }
        addActor(pane);
        table.setY(pane.getHeight() / 2f,Align.center);
        pane.setDebug(true);
    }

    private void snapToNearest() {

        if (table.getChildren().isEmpty()) {
            return;
        }

        Actor nearest = findNearestActor();

        if (nearest == null) {
            return;
        }

        pane.setFlingTime(0);
        snapping = true;
//        pane.validate();
        System.out.println(nearest.getX());
        pane.setScrollX(nearest.getX()-widthView/2f + nearest.getWidth()/2f);
//        pane.updateVisualScroll();

//        NumAction action = new NumAction();
//        action.setNumActionListener(new NumActionListener() {
//            @Override
//            public void update(float value) {
//                pane.validate();
//                pane.setScrollX(value);
//                pane.updateVisualScroll();
//            }
//        });
//        pane.addAction(action);

    }

    private Actor findNearestActor() {

        paneCenter.set(
                pane.getWidth() / 2f,
                pane.getHeight() / 2f
        );

        pane.localToStageCoordinates(paneCenter);

        Actor nearest = null;

        float minDistance = Float.MAX_VALUE;

        for (Actor actor : table.getChildren()) {

            actorCenter.set(
                    actor.getWidth() / 2f,
                    actor.getHeight() / 2f
            );

            actor.localToStageCoordinates(actorCenter);

            float distance;

            if (isVerticalScrolling) {

                distance = Math.abs(
                        actorCenter.y - paneCenter.y
                );

            } else {

                distance = Math.abs(
                        actorCenter.x - paneCenter.x
                );
            }

            if (distance < minDistance) {

                minDistance = distance;
                nearest = actor;
            }
        }

        return nearest;
    }

    private float actorBaseHight= 0;
    private float actorBaseWidth = 0;
    /**
     * 添加元素
     */
    public void add(Actor actor) {
        this.actorBaseWidth = actor.getWidth();
        this.actorBaseHight = actor.getHeight();
        // 缩放时以中心点作为原点
        actor.setOrigin(
                actor.getWidth() / 2f,
                actor.getHeight() / 2f
        );
        table.add(actor);
    }

    /**
     * 横向滚动时更新缩放
     */
    public void updateVisualScrollX() {
        if (table.getChildren().isEmpty()) {
            return;
        }

        // ScrollPane 中心转换到 Stage 坐标
        paneCenter.set(
                pane.getWidth() / 2f,
                pane.getHeight() / 2f
        );
        pane.localToStageCoordinates(paneCenter);

        float distanceRange = scaleDistance > 0
                ? scaleDistance
                : pane.getWidth() / 2f;

        for (Actor actor : table.getChildren()) {

            // Actor 中心转换到 Stage 坐标
            actorCenter.set(
                    actor.getWidth() / 2f,
                    actor.getHeight() / 2f
            );
            actor.localToStageCoordinates(actorCenter);

            float distance = Math.abs(
                    actorCenter.x - paneCenter.x
            );

            applyScale(actor, distance, distanceRange);
        }
        layout();
    }

    /**
     * 纵向滚动时更新缩放
     */
    public void updateVisualScrollY() {
        if (table.getChildren().isEmpty()) {
            return;
        }

        paneCenter.set(
                pane.getWidth() / 2f,
                pane.getHeight() / 2f
        );
        pane.localToStageCoordinates(paneCenter);

        float distanceRange = scaleDistance > 0
                ? scaleDistance
                : pane.getHeight() / 2f;

        for (Actor actor : table.getChildren()) {

            actorCenter.set(
                    actor.getWidth() / 2f,
                    actor.getHeight() / 2f
            );
            actor.localToStageCoordinates(actorCenter);

            float distance = Math.abs(
                    actorCenter.y - paneCenter.y
            );

            applyScale(actor, distance, distanceRange);
        }
        layout();
    }

    /**
     * 根据距离设置缩放
     */
    private void applyScale(
            Actor actor,
            float distance,
            float distanceRange
    ) {

        // 0 = 正中心
        // 1 = 达到缩放影响范围边缘

        float progress = MathUtils.clamp(
                distance / distanceRange,
                0f,
                1f
        );


        /*
         * 中心：
         * progress = 0
         * scale = maxScale
         *
         * 两侧：
         * progress = 1
         * scale = minScale
         */
        float scale = MathUtils.lerp(
                maxScale,
                minScale,
                progress
        );
        actor.setWidth(scale * actorBaseWidth);
        actor.setHeight(scale * actorBaseHight);
    }

    public void layout(){
        table.layoutChild();
    }

    // =========================
    // 配置
    // =========================

    public PageView setScaleRange(float minScale, float maxScale) {
        this.minScale = minScale;
        this.maxScale = maxScale;
        return this;
    }

    public PageView setScaleDistance(float distance) {
        this.scaleDistance = distance;
        return this;
    }

    public void setVerticalScrolling(boolean verticalScrolling) {
        isVerticalScrolling = verticalScrolling;

        if (verticalScrolling) {
            pane.setScrollingDisabled(true, false);
        } else {
            pane.setScrollingDisabled(false, true);
        }
    }

    public ScrollPane getScrollPane() {
        return pane;
    }

    public LayoutGroup getTable() {
        return table;
    }
}