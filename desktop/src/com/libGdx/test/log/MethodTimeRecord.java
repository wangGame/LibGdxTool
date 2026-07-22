package com.libGdx.test.log;

public final class MethodTimeRecord {

    private final String name;
    private final String className;
    private final String methodName;
    private final long durationNanos;
    private final String threadName;
    private final boolean success;
    private final Throwable throwable;

    public MethodTimeRecord(
            String name,
            String className,
            String methodName,
            long durationNanos,
            String threadName,
            boolean success,
            Throwable throwable
    ) {
        this.name = name;
        this.className = className;
        this.methodName = methodName;
        this.durationNanos = durationNanos;
        this.threadName = threadName;
        this.success = success;
        this.throwable = throwable;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public long getDurationNanos() {
        return durationNanos;
    }

    public double getDurationMillis() {
        return durationNanos / 1_000_000.0;
    }

    public String getThreadName() {
        return threadName;
    }

    public boolean isSuccess() {
        return success;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String toString() {
        return "MethodTimeRecord{" +
                "name='" + name + '\'' +
                ", method='" + className + "#" + methodName + '\'' +
                ", durationMs=" + String.format("%.3f", getDurationMillis()) +
                ", thread='" + threadName + '\'' +
                ", success=" + success +
                '}';
    }
}