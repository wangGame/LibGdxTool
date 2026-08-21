package com.libGdx.test.log;

import java.util.Locale;

public final class MethodTimeReporter {

    private static volatile boolean enabled = true;

    /**
     * 默认打印所有方法。
     */
    private static volatile long globalThresholdMs = 0;

    private static volatile MethodTimeListener listener =
            MethodTimeReporter::printToConsole;

    private MethodTimeReporter() {
    }

    public static void report(
            String customName,
            String className,
            String methodName,
            long durationNanos,
            long methodThresholdMs,
            boolean success,
            Throwable throwable
    ) {
        if (!enabled) {
            return;
        }

        long thresholdMs = methodThresholdMs >= 0
                ? methodThresholdMs
                : globalThresholdMs;

        if (durationNanos < thresholdMs * 1_000_000L) {
            return;
        }

        String displayName;

        if (customName == null || customName.trim().isEmpty()) {
            displayName = className + "#" + methodName;
        } else {
            displayName = customName;
        }

        MethodTimeRecord record = new MethodTimeRecord(
                displayName,
                className,
                methodName,
                durationNanos,
                Thread.currentThread().getName(),
                success,
                throwable
        );

        MethodTimeListener currentListener = listener;

        if (currentListener == null) {
            return;
        }

        try {
            currentListener.onMethodFinished(record);
        } catch (Throwable listenerError) {
            // 统计框架自身不能影响业务代码。
            listenerError.printStackTrace();
        }
    }

    private static void printToConsole(MethodTimeRecord record) {
        String message = String.format(
                Locale.ROOT,
                "[MethodTime] %s | %.3f ms | thread=%s | success=%s",
                record.getName(),
                record.getDurationMillis(),
                record.getThreadName(),
                record.isSuccess()
        );

        System.out.println(message);

        if (record.getThrowable() != null) {
            System.out.println(
                    "[MethodTime] exception="
                            + record.getThrowable().getClass().getName()
            );
        }
    }

    public static void setListener(MethodTimeListener listener) {
        MethodTimeReporter.listener = listener;
    }

    public static void resetListener() {
        MethodTimeReporter.listener =
                MethodTimeReporter::printToConsole;
    }

    public static void setEnabled(boolean enabled) {
        MethodTimeReporter.enabled = enabled;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setGlobalThresholdMs(long thresholdMs) {
        if (thresholdMs < 0) {
            throw new IllegalArgumentException(
                    "thresholdMs cannot be negative"
            );
        }

        globalThresholdMs = thresholdMs;
    }

    public static long getGlobalThresholdMs() {
        return globalThresholdMs;
    }
}