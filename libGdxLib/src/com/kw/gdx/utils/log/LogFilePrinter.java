package com.kw.gdx.utils.log;


import com.badlogic.gdx.Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LogFilePrinter implements NLog.Printer {
    public static final String BACKUP_SUFFIX = ".0";
    private final String mPath;
    private final LogFileOpener mOpener;
    private final long mMaxSize;
    private MessageFormatter mFormatter;
    private FileOutputStream mStream;
    private long mCurrentSize;

    public interface LogFileOpener {
        FileOutputStream openLogFile(String filename);
    }

    public interface MessageFormatter {
        String formatMessage(int level, String tag, String message);
    }

    public String getPath() {
        return mPath;
    }

    public void flush() {
        try {
            mStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final MessageFormatter sDefaultMessageFormatter =
            new MessageFormatter() {
                private final StringBuilder mStringBuilder = new StringBuilder();

                @Override
                public String formatMessage(int level, String tag, String message) {
                    String levelString;
                    if (level == Application.LOG_DEBUG) {
                        levelString = "D";
                    } else if (level == Application.LOG_INFO) {
                        levelString = "I";
                    } else { // LOG_ERROR
                        levelString = "E";
                    }
                    mStringBuilder.setLength(0);
                    appendDate();
                    mStringBuilder.append(' ');
                    mStringBuilder.append(levelString);
                    mStringBuilder.append(' ');
                    mStringBuilder.append(tag);
                    mStringBuilder.append(' ');
                    mStringBuilder.append(message);
                    return mStringBuilder.toString();
                }

                private void appendDate() {
                    long timeSpent = System.currentTimeMillis();
                    long secondsInDay = (timeSpent / 1000) % 86400;
                    long hour = secondsInDay / 3600;
                    long minutes = (secondsInDay % 3600) / 60;
                    long seconds = secondsInDay % 60;
                    long millis = timeSpent % 1000;
                    appendNumber(hour, 2);
                    mStringBuilder.append(':');
                    appendNumber(minutes, 2);
                    mStringBuilder.append(':');
                    appendNumber(seconds, 2);
                    mStringBuilder.append('.');
                    appendNumber(millis, 3);
                }

                private void appendNumber(long value, int width) {
                    int digitCount = 1;
                    for (long v = value; v > 9; v /= 10) {
                        digitCount += 1;
                    }
                    for (int idx = 0; idx < width - digitCount; ++idx) {
                        mStringBuilder.append('0');
                    }
                    mStringBuilder.append(value);
                }
            };

    public LogFilePrinter(String path, long maxSize) {
        this(
                path,
                maxSize,
                filename -> {
                    try {
                        return new FileOutputStream(filename, true /* append */);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    public LogFilePrinter(String path, long maxSize, LogFileOpener opener) {
        mPath = path;
        mMaxSize = maxSize;
        mOpener = opener;
        mFormatter = sDefaultMessageFormatter;
        File file = new File(path);
        mCurrentSize = file.exists() ? file.length() : 0;
        openFile();
    }

    public void setMessageFormatter(MessageFormatter messageFormatter) {
        mFormatter = messageFormatter;
    }

    @Override
    public void print(int level, String tag, String message) {
        if (mStream == null) {
            return;
        }
        message = mFormatter.formatMessage(level, tag, message);
        // + 1 for the '\n'
        if (mCurrentSize + message.length() + 1 > mMaxSize) {
            rotateLogFile();
        }
        try {
            mStream.write(message.getBytes());
            mStream.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rotateLogFile() {
        try {
            mStream.flush();
            mStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(mPath);
        file.renameTo(new File(mPath + BACKUP_SUFFIX));

        mCurrentSize = 0;
        openFile();
    }

    private void openFile() {
        if (mStream != null) {
            try {
                mStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mStream = mOpener.openLogFile(mPath);
    }
}
