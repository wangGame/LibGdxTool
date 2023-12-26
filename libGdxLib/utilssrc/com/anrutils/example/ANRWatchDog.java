package com.anrutils.example;

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Salomon BRYS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


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
    private ANRListener _anrListener = DEFAULT_ANR_LISTENER;
    private ANRInterceptor _anrInterceptor = DEFAULT_ANR_INTERCEPTOR;
    private InterruptionListener _interruptionListener = DEFAULT_INTERRUPTION_LISTENER;
    private final int _timeoutInterval;
    private String _namePrefix = "";
    private boolean _logThreadsWithoutStackTrace = false;
    private volatile long _tick = 0;
    private volatile boolean _reported = false;
    private static final int DEFAULT_ANR_TIMEOUT = 5000;
    public interface ANRListener {
        void onAppNotResponding(ANRError error) ;
    }

    public interface ANRInterceptor {
        long intercept(long duration);
    }

    public interface InterruptionListener {
        void onInterrupted(InterruptedException exception);
    }

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
//            return duration;
            return -1;
        }
    };

    private static final InterruptionListener DEFAULT_INTERRUPTION_LISTENER = new InterruptionListener() {
        @Override public void onInterrupted(InterruptedException exception) {
            NLog.d("ANRWatchdog", "Interrupted: " + exception.getMessage());
        }
    };

//    private final Runnable _ticker = new Runnable() {
//        @Override public void run() {
//
//        }
//    };

    /**
     * Constructs a watchdog that checks the ui thread every {@value #DEFAULT_ANR_TIMEOUT} milliseconds
     */
    public ANRWatchDog() {
        this(DEFAULT_ANR_TIMEOUT);
    }

    public ANRWatchDog(int timeoutInterval) {
        super();
        _timeoutInterval = timeoutInterval;
        //得到的是调用的那个线程
        targetThread = Thread.currentThread();
    }

    /**
     * @return The interval the WatchDog
     */

    public int getTimeoutInterval() {
        return _timeoutInterval;
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
            _anrListener = DEFAULT_ANR_LISTENER;
        } else {
            _anrListener = listener;
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
            _anrInterceptor = DEFAULT_ANR_INTERCEPTOR;
        } else {
            _anrInterceptor = interceptor;
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
            _interruptionListener = DEFAULT_INTERRUPTION_LISTENER;
        } else {
            _interruptionListener = listener;
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
        _namePrefix = prefix;
        return this;
    }

    /**
     * Set that only the main thread will be reported.
     *
     * @return itself for chaining.
     */
  
    public ANRWatchDog setReportMainThreadOnly() {
        _namePrefix = null;
        return this;
    }

    /**
     * Set that all threads will be reported (default behaviour).
     *
     * @return itself for chaining.
     */
  
    public ANRWatchDog setReportAllThreads() {
        _namePrefix = "";
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
    private boolean _ignoreDebugger = false;
    public ANRWatchDog setIgnoreDebugger(boolean ignoreDebugger) {
        _ignoreDebugger = ignoreDebugger;
        return this;
    }

    public void reset(){
        _tick = 0;
        _reported = false;
    }

    @SuppressWarnings("NonAtomicOperationOnVolatileField")
    @Override
    public void run() {
        setName("|ANR-WatchDog|");

        long interval = _timeoutInterval;
        while (!isInterrupted()) {
            //开始先加值，让结果不为0，使用ui线程去恢复值
            _tick += interval;
            Gdx.app.postRunnable(()->{
                reset();
            });
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // interput exception
                _interruptionListener.onInterrupted(e);
                return ;
            }
            // If the main thread has not handled _ticker, it is blocked. ANR.
            if (_tick != 0 && !_reported) {

                if (_ignoreDebugger){
                    NLog.i("_ignoreDebugger -------------------");
                    _reported = true;
                    return;
                }
                //noinspection ConstantConditions
                interval = _anrInterceptor.intercept(_tick);
                if (interval > 0) {
                    continue;
                }
                final ANRError error;
                if (_namePrefix != null) {
                    error = ANRError.New(_tick, _namePrefix, _logThreadsWithoutStackTrace,targetThread);
                } else {
                    error = ANRError.NewMainOnly(_tick);
                }
                _anrListener.onAppNotResponding(error);
                interval = _timeoutInterval;
                _reported = true;
            }
        }
    }
}
