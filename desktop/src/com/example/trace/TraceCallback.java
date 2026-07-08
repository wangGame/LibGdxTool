package com.example.trace;

public interface TraceCallback {
    void onTraceFinished(TraceResult result);

    default void onTraceFailed(Throwable error) {
        error.printStackTrace();
    }

    default void onTraceCancelled() {
    }
}
