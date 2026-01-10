package com.libGdx.test.event;

import com.badlogic.gdx.utils.Array;

import java.util.function.Consumer;

public class CoreEventBus {

    private static class Entry<T> {
        final Class<T> type;
        final Consumer<T> listener;
        final boolean once;

        Entry(Class<T> type, Consumer<T> listener, boolean once) {
            this.type = type;
            this.listener = listener;
            this.once = once;
        }
    }

    private static final Array<Entry<?>> entries = new Array<>();

    public static <T> void subscribe(Class<T> type, Consumer<T> listener) {
        subscribe(type, listener, false);
    }

    public static <T> void subscribe(Class<T> type, Consumer<T> listener, boolean once) {
        entries.add(new Entry<>(type, listener, once));
    }

    public static void unsubscribe(Consumer<?> listener) {
        for (int i = entries.size - 1; i >= 0; i--) { // 倒序遍历安全
            if (entries.get(i).listener == listener) {
                entries.removeIndex(i);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void post(T event) {
        Array<Entry<?>> toRemove = new Array<>();
        for (Entry<?> e : entries) {
            if (e.type == event.getClass()) {
                ((Consumer<T>) e.listener).accept(event);
                if (e.once) toRemove.add(e);
            }
        }
        for (Entry<?> e : toRemove) {
            entries.removeValue(e, true);
        }
    }
}
