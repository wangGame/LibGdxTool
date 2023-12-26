package com.libGdx.test.log;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.utils.log.LogFilePrinter;
import com.kw.gdx.utils.log.NLog;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/27 14:10
 */
public class NlogTest extends LibGdxTestMain {
    public static void main(String[] args) {
        NlogTest test = new NlogTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        NLog.isLog = true;
        NLog.d("tag d %s","log info ------");
        NLog.i("tag i %s","log info ------");
        NLog.e("tag e %s","log info ------");
        NLog.addPrinter(new LogFilePrinter("log",1024));
        NLog.d("tag d %s","log info ------");
        NLog.i("tag i %s","log info ------");
        NLog.e("tag e %s","log info ------");

    }
}
