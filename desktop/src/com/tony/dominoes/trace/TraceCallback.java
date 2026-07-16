package com.tony.dominoes.trace;

public interface TraceCallback {
    void onTraceFinished(TraceResult result);

    default void onTraceFailed(Throwable error) {
        error.printStackTrace();
    }

    default void onTraceCancelled() {
    }
}
