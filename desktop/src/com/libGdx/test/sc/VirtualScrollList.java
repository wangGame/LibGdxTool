package com.libGdx.test.sc;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

import java.util.ArrayList;
import java.util.List;

/**
 * 虚拟滚动列表。
 *
 * 核心思想：
 * 1. ScrollPane 里面放一个很高的 content。
 * 2. content 的高度 = 数据数量 * itemHeight。
 * 3. 但真正创建的 item Actor 只有屏幕可见数量 + 3。
 * 4. 滚动时根据 scrollY 计算当前应该显示哪些数据。
 * 5. 复用已有 Actor，重新 setData。
 */
public class VirtualScrollList<T> extends Group {

    public interface Adapter<T> {

        Actor createView();

        void bindView(Actor view, T data, int index);
    }

    private final ScrollPane scrollPane;
    private final Group content;

    private final Adapter<T> adapter;
    private final List<T> dataList = new ArrayList<>();
    private final List<Actor> viewPool = new ArrayList<>();

    private final float itemHeight;

    private int lastFirstIndex = -1;
    private int lastVisibleCount = -1;

    public VirtualScrollList(
            float width,
            float height,
            float itemHeight,
            Adapter<T> adapter
    ) {
        this.itemHeight = itemHeight;
        this.adapter = adapter;

        setSize(width, height);

        content = new Group();
        content.setSize(width, height);
        content.setTouchable(Touchable.childrenOnly);

        scrollPane = new ScrollPane(content);
        scrollPane.setBounds(0, 0, width, height);

        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFlickScroll(true);

        addActor(scrollPane);
    }

    public void setItems(List<T> items) {
        dataList.clear();

        if (items != null) {
            dataList.addAll(items);
        }

        float contentHeight = dataList.size() * itemHeight;

        content.setSize(getWidth(), contentHeight);

        scrollPane.setScrollY(0);
        scrollPane.updateVisualScroll();

        forceRefresh();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateVisibleItems(false);
    }

    private void forceRefresh() {
        lastFirstIndex = -1;
        lastVisibleCount = -1;
        updateVisibleItems(true);
    }

    private void updateVisibleItems(boolean force) {
        if (dataList.isEmpty()) {
            for (Actor actor : viewPool) {
                actor.setVisible(false);
            }
            return;
        }

        float scrollY = scrollPane.getScrollY();

        int firstIndex = (int) (scrollY / itemHeight);
        firstIndex = MathUtils.clamp(firstIndex, 0, dataList.size() - 1);

        int visibleCount = (int) Math.ceil(getHeight() / itemHeight) + 3;
        visibleCount = Math.min(visibleCount, dataList.size());

        if (!force
                && firstIndex == lastFirstIndex
                && visibleCount == lastVisibleCount) {
            return;
        }

        lastFirstIndex = firstIndex;
        lastVisibleCount = visibleCount;

        ensurePoolSize(visibleCount);

        for (int i = 0; i < viewPool.size(); i++) {
            Actor view = viewPool.get(i);

            int dataIndex = firstIndex + i;

            if (dataIndex >= dataList.size()) {
                view.setVisible(false);
                continue;
            }
            view.setVisible(true);
            float itemY = content.getHeight() - (dataIndex + 1) * itemHeight;
            view.setBounds(
                    0,
                    itemY,
                    content.getWidth(),
                    itemHeight
            );
            adapter.bindView(view, dataList.get(dataIndex), dataIndex);
        }
    }

    private void ensurePoolSize(int count) {
        while (viewPool.size() < count) {
            Actor view = adapter.createView();
            viewPool.add(view);
            content.addActor(view);
        }
    }
}