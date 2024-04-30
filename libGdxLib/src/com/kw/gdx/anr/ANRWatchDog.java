package com.kw.gdx.anr;

import com.badlogic.gdx.Gdx;
import com.kw.gdx.utils.log.NLog;

/**
 * A watchdog timer thread that detects when the UI thread has frozen.
 *
 * 从https://github.com/SalomonBrys/ANR-WatchDog改写来的
 */
@SuppressWarnings("UnusedReturnValue")
public class ANRWatchDog extends Thread {
    private Thread targetThread;
    private ANRListener anrListener = DEFAULT_ANR_LISTENER;
    private ANRInterceptor anrInterceptor = DEFAULT_ANR_INTERCEPTOR;
    private InterruptionListener interruptionListener = DEFAULT_INTERRUPTION_LISTENER;
    private final int timeoutInterval;
    private String namePrefix = "";
    private boolean _logThreadsWithoutStackTrace = false;
    private volatile long tick = 0;
    private volatile boolean reported = false;
    private static final int DEFAULT_ANR_TIMEOUT = 5000;
    private boolean ignoreDebugger = false;
    private static final ANRListener DEFAULT_ANR_LISTENER = new ANRListener() {
        @Override public void onAppNotResponding(ANRError error) {
            try {
                throw error;
            } catch (ANRError anrError) {
                anrError.printStackTrace();
            }
        }
    };

    private static final ANRInterceptor DEFAULT_ANR_INTERCEPTOR = new ANRInterceptor() {
        @Override public long intercept(long duration) {
            NLog.e("intercet duration :"+duration);
            return -1;
        }
    };

    private static final InterruptionListener DEFAULT_INTERRUPTION_LISTENER = new InterruptionListener() {
        @Override public void onInterrupted(InterruptedException exception) {
            NLog.d("ANRWatchdog", "Interrupted: " + exception.getMessage());
        }
    };

    /**
     * Constructs a watchdog that checks the ui thread every {@value #DEFAULT_ANR_TIMEOUT} milliseconds
     */
    public ANRWatchDog() {
        this(DEFAULT_ANR_TIMEOUT);
    }

    public ANRWatchDog(int timeoutInterval) {
        super();
        //多久报告
        this.timeoutInterval = timeoutInterval;
        //得到的是调用的那个线程
        targetThread = Thread.currentThread();
    }

    /**
     * @return The interval the WatchDog
     */
    public int getTimeoutInterval() {
        return timeoutInterval;
    }

    /**
     * Sets an interface for when an ANR is detected.
     * If not set, the default behavior is to throw an error and crash the application.
     *
     * @param listener The new listener or null
     * @return itself for chaining.
     */
    public ANRWatchDog setANRListener( ANRListener listener) {
        if (listener == null) {
            anrListener = DEFAULT_ANR_LISTENER;
        } else {
            anrListener = listener;
        }
        return this;
    }

    /**
     * Sets an interface to intercept ANRs before they are reported.
     * If set, you can define if, given the current duration of the detected ANR and external context, it is necessary to report the ANR.
     *
     * @param interceptor The new interceptor or null
     * @return itself for chaining.
     */
    public ANRWatchDog setANRInterceptor( ANRInterceptor interceptor) {
        if (interceptor == null) {
            anrInterceptor = DEFAULT_ANR_INTERCEPTOR;
        } else {
            anrInterceptor = interceptor;
        }
        return this;
    }

    /**
     * Sets an interface for when the watchdog thread is interrupted.
     * If not set, the default behavior is to just log the interruption message.
     *
     * @param listener The new listener or null.
     * @return itself for chaining.
     */
    public ANRWatchDog setInterruptionListener( InterruptionListener listener) {
        if (listener == null) {
            interruptionListener = DEFAULT_INTERRUPTION_LISTENER;
        } else {
            interruptionListener = listener;
        }
        return this;
    }

    /**
     * Set the prefix that a thread's name must have for the thread to be reported.
     * Note that the main thread is always reported.
     * Default "".
     *
     * @param prefix The thread name's prefix for a thread to be reported.
     * @return itself for chaining.
     */
    public ANRWatchDog setReportThreadNamePrefix( String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        namePrefix = prefix;
        return this;
    }

    /**
     * Set that only the main thread will be reported.
     *
     * @return itself for chaining.
     */
    public ANRWatchDog setReportMainThreadOnly() {
        namePrefix = null;
        return this;
    }

    /**
     * Set that all threads will be reported (default behaviour).
     *
     * @return itself for chaining.
     */

    public ANRWatchDog setReportAllThreads() {
        namePrefix = "";
        return this;
    }

    /**
     * Set that all running threads will be reported,
     * even those from which no stack trace could be extracted.
     * Default false.
     *
     * @param logThreadsWithoutStackTrace Whether or not all running threads should be reported
     * @return itself for chaining.
     */

    public ANRWatchDog setLogThreadsWithoutStackTrace(boolean logThreadsWithoutStackTrace) {
        _logThreadsWithoutStackTrace = logThreadsWithoutStackTrace;
        return this;
    }

    /**
     * Set whether to ignore the debugger when detecting ANRs.
     * When ignoring the debugger, ANRWatchdog will detect ANRs even if the debugger is connected.
     * By default, it does not, to avoid interpreting debugging pauses as ANRs.
     * Default false.
     *
     * @param ignoreDebugger Whether to ignore the debugger.
     * @return itself for chaining.
     */

    public ANRWatchDog setIgnoreDebugger(boolean ignoreDebugger) {
        this.ignoreDebugger = ignoreDebugger;
        return this;
    }

    public void reset(){
        tick = 0;
        reported = false;
    }

    @SuppressWarnings("NonAtomicOperationOnVolatileField")
    @Override
    public void run() {
        setName("|ANR-WatchDog|");
        long interval = timeoutInterval;
        while (!isInterrupted()) {
            //开始先加值，让结果不为0，使用ui线程去恢复值
            tick += interval;
            Gdx.app.postRunnable(()->{
                reset();
            });
            //他先休息，然后使用允许线程来恢复值、如果还没恢复就报告
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // interput exception
                interruptionListener.onInterrupted(e);
                return ;
            }
            // If the main thread has not handled _ticker, it is blocked. ANR.
            if (tick != 0 && !reported) {
                if (ignoreDebugger){
                    NLog.i("_ignoreDebugger -------------------");
                    reported = true;
                    return;
                }
                //noinspection ConstantConditions
                interval = anrInterceptor.intercept(tick);
                if (interval > 0) {
                    continue;
                }
                final ANRError error;
                if (namePrefix != null) {
                    error = ANRError.New(tick, namePrefix, _logThreadsWithoutStackTrace,targetThread);
                } else {
                    error = ANRError.NewMainOnly(tick);
                }
                anrListener.onAppNotResponding(error);
                interval = timeoutInterval;
                reported = true;
            }
        }
    }

    public static void main(String[] args) {
        ANRWatchDog dog = new ANRWatchDog(100);
        dog.start();
        while (true) {

        }

    }
}

