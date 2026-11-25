package emu.com.kw.gdx;

import com.github.xpenatan.gdx.backends.teavm.gen.Emulate;
import com.kw.gdx.anr.ANRInterceptor;
import com.kw.gdx.anr.ANRListener;
import com.kw.gdx.anr.ANRWatchDog;
import com.kw.gdx.anr.InterruptionListener;

/** TeaVM replacement: keep the watchdog API without creating a browser thread. */
@Emulate(ANRWatchDog.class)
public class ANRWatchDogEmu extends Thread {
    private final long timeoutInterval;

    public ANRWatchDogEmu() {
        this(5000);
    }

    public ANRWatchDogEmu(long timeoutInterval) {
        this.timeoutInterval = timeoutInterval;
    }

    public long getTimeoutInterval() { return timeoutInterval; }
    public ANRWatchDogEmu setANRListener(ANRListener listener) { return this; }
    public ANRWatchDogEmu setANRInterceptor(ANRInterceptor interceptor) { return this; }
    public ANRWatchDogEmu setInterruptionListener(InterruptionListener listener) { return this; }
    public ANRWatchDogEmu setReportThreadNamePrefix(String prefix) { return this; }
    public ANRWatchDogEmu setReportMainThreadOnly() { return this; }
    public ANRWatchDogEmu setReportAllThreads() { return this; }
    public ANRWatchDogEmu setLogThreadsWithoutStackTrace(boolean enabled) { return this; }
    public ANRWatchDogEmu setIgnoreDebugger(boolean enabled) { return this; }
    public void reset() { }

    @Override
    public synchronized void start() { }

    @Override
    public void run() { }
}
