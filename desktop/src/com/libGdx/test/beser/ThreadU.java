package com.libGdx.test.beser;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.badlogic.gdx.utils.async.AsyncTask;
/**
 * @Auther jian xian si qi
 * @Date 2023/7/17 14:06
 */
class ThreadU {
    private AsyncExecutor executor;
    private static ThreadU _i = null;

    private ThreadU() {
        executor = new AsyncExecutor(8);
    }

    private static ThreadU i() {
        if (_i == null) {
            _i = new ThreadU();
        }
        return _i;
    }

    public static <T> AsyncResult<T> exec(AsyncTask<T> task) {
        return i().executor.submit(task);
    }

}
