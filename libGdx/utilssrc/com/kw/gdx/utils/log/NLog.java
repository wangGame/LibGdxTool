package com.kw.gdx.utils.log;

import com.badlogic.gdx.Application;

import java.util.Vector;

public class NLog {
    private static final Vector<Printer> sPrinters = new Vector<>();
    private static int sStackDepth = -1;
    public static boolean isLog = true;
    public interface Printer {
        void print(int level, String tag, String message);
    }

    /** Implementation of Printer which logs to System.err */
    public static class DefaultPrinter implements Printer {
        final long mStartTime;

        DefaultPrinter() {
            mStartTime = System.currentTimeMillis();
        }

        @Override
        public void print(int level, String tag, String message) {
            if (!isLog)return;
            String levelString;
            if (level == Application.LOG_DEBUG) {
                levelString = "D";
                long timeSpent = System.currentTimeMillis() ;
                System.err.printf("%s - %s - %s - %s\n", StringUtils.formatRaceTime(timeSpent), levelString, tag, message);
            } else if (level == Application.LOG_INFO) {
                levelString = "I";
                long timeSpent = System.currentTimeMillis() ;
                System.out.printf("%s - %s - %s - %s\n", StringUtils.formatRaceTime(timeSpent), levelString, tag, message);
            } else { // LOG_ERROR
                levelString = "E";
                long timeSpent = System.currentTimeMillis() ;
                System.err.printf("%s - %s - %s - %s\n", StringUtils.formatRaceTime(timeSpent), levelString, tag, message);
            }
//            long timeSpent = System.currentTimeMillis() - mStartTime;
//            long timeSpent = System.currentTimeMillis() ;
//            System.err.printf("%s - %s - %s - %s\n", StringUtils.formatRaceTime(timeSpent), levelString, tag, message);
        }
    }

    public static void d(Object obj, Object... args) {
        print(Application.LOG_DEBUG, obj, args);
    }

    public static void i(Object obj, Object... args) {
        print(Application.LOG_INFO, obj, args);
    }

    public static void e(Object obj, Object... args) {
        print(Application.LOG_ERROR, obj, args);
    }

    public static void backtrace() {
        StackTraceElement[] lst = Thread.currentThread().getStackTrace();
        for (int idx = 2, n = lst.length; idx < n; ++idx) {
            NLog.d("bt: %s", lst[idx]);
        }
    }

    public static void addPrinter(Printer printer) {
        sPrinters.add(printer);
    }

    private static synchronized void print(int level, Object obj, Object... args) {
        if (sStackDepth < 0) {
            initStackDepth();
        }
        final String tag = getCallerMethod();
        String message;
        if (obj == null) {
            message = "(null)";
        } else {
            String format = obj.toString();
            message = args.length > 0 ? StringUtils.format(format, args) : format;
        }
        if (sPrinters.isEmpty()) {
            sPrinters.add(new DefaultPrinter());
        }
        for (Printer printer : sPrinters) {
            printer.print(level, tag, message);
        }
    }

    private static void initStackDepth() {
        final StackTraceElement[] lst = Thread.currentThread().getStackTrace();
        for (int i = 0, n = lst.length; i < n; ++i) {
            if (lst[i].getMethodName().equals("initStackDepth")) {
                sStackDepth = i;
                return;
            }
        }
    }

    private static String getCallerMethod() {
        final StackTraceElement stackTraceElement =
                Thread.currentThread().getStackTrace()[sStackDepth + 3];
        final String fullClassName = stackTraceElement.getClassName();
        final String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        final String method = stackTraceElement.getMethodName();
        return className + "." + method;
    }

    private static String getCallerInfoMethod() {
        final StackTraceElement stackTraceElement =
                Thread.currentThread().getStackTrace()[sStackDepth + 4];
        final String fullClassName = stackTraceElement.getClassName();
        final String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        final String method = stackTraceElement.getMethodName();
        return className + "." + method;
    }


    public static void check(Object obj, Object... args) {
        printCheck(Application.LOG_INFO, obj, args);
    }

    private static void printCheck(int level, Object obj, Object[] args) {
        if (sStackDepth < 0) {
            initStackDepth();
        }
        final String tag = getCallerInfoMethod();
        String message;
        if (obj == null) {
            message = "(null)";
        } else {
            String format = obj.toString();
            message = args.length > 0 ? StringUtils.format(format, args) : format;
        }
        if (sPrinters.isEmpty()) {
            sPrinters.add(new DefaultPrinter());
        }
        for (Printer printer : sPrinters) {
            printer.print(level, tag, message);
        }
    }

}

