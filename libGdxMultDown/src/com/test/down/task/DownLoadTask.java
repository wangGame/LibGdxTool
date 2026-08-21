package com.test.down.task;

import com.badlogic.gdx.utils.Array;
import com.kw.gdx.file.JsonUtils;
import com.test.down.bean.DownLoadInfo;
import com.test.down.http.DefaultHttpClient;
import com.test.down.http.HttpClient;
import com.test.down.http.HttpUtils;
import com.test.down.listener.DownloadListener;
import com.test.down.status.DownLoadStatus;
import com.test.down.stream.FileDownloadRandomAccessFile;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
public class DownLoadTask {
    private String taskId;

    private static final int DEFAULT_THREAD_NUM = 3;
    private static final long PROGRESS_INTERVAL_MS = 500;

    private HttpClient client = new DefaultHttpClient();
    private int threadNum = DEFAULT_THREAD_NUM;
    private Array<SplitTask> downLoadThreads = new Array<>();
    private String url;
    private DownloadListener downloadListener;

    public DownLoadTask(String md5) {
        this.taskId = md5;
    }

    public void down(String url, String saveDir, String saveFile) throws IOException, IllegalAccessException {

        this.url = url;
        String filePath = saveDir + "/" + saveFile;

        HttpURLConnection conn = client.createConnect(url);
        conn.connect();
        conn = HttpUtils.redirect(conn);

        long contentLength = conn.getContentLengthLong();
        if (contentLength <= 0) {
            throw new IOException("Invalid content length: " + contentLength);
        }

        // 创建目录
        File targetFile = new File(filePath);
        File parent = targetFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        // 删除旧文件
        if (targetFile.exists()) {
            targetFile.delete();
        }

        // ⭐️ 预分配文件（一定要关闭）
        try (FileDownloadRandomAccessFile raf =
                     new FileDownloadRandomAccessFile(targetFile)) {
            raf.setLength(contentLength);
        }

        // ================= 断点信息 =================

        int uniqueId = HttpUtils.getUniqueId(url, saveDir, saveFile, "3");
        String metaDir = parent + "/" + uniqueId;
        String metaFile = metaDir + "/temp/partfile" + uniqueId;

        DownLoadInfo info = JsonUtils.read(metaFile, DownLoadInfo.class);
        if (info != null && info.getContentLengthLong() != contentLength) {
            JsonUtils.delete(metaFile);
            info = null;
        }

        if (info == null) {
            info = new DownLoadInfo();
            info.setUrl(url);
            info.setThreadNum(threadNum);
            info.setFilePath(targetFile.getAbsolutePath());
            info.setContentLength(contentLength);
            JsonUtils.save(metaFile, info);
        }

        // ================= 分片 =================

        long blockSize = contentLength / threadNum;
        long start = 0;
        int blockId = 0;

        for (int i = 0; i < threadNum - 1; i++) {
            downLoadThreads.add(
                    new SplitTask(url, start, blockSize,
                            filePath, blockId++, metaFile)
            );
            start += blockSize;
        }

        // 最后一块
        downLoadThreads.add(
                new SplitTask(url, start,
                        contentLength - start,
                        filePath, blockId, metaFile)
        );

        // 启动下载线程
        for (Thread t : downLoadThreads) {
            t.start();
        }

        startProgressMonitor(contentLength, metaDir);
    }

    // ================= 进度监控 =================

    private void startProgressMonitor(long total, String metaDir) {

        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(PROGRESS_INTERVAL_MS);

                    boolean finished = true;
                    long current = 0;

                    for (SplitTask task : downLoadThreads) {
                        if (task.getDownloadStatus() != DownLoadStatus.FINISH) {
                            finished = false;
                        }
                        current += task.getDownLoadInfo().getCurrentPosition();
                    }

                    if (downloadListener != null) {
                        downloadListener.process(total, current);
                    }

                    if (finished) {
                        if (downloadListener != null) {
                            downloadListener.downFinish();
                        }
                        deleteDirectory(new File(metaDir));
                        break;
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }, "download-progress").start();
    }

    // ================= utils =================

    private static void deleteDirectory(File dir) {
        if (dir == null || !dir.exists()) return;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) deleteDirectory(f);
                f.delete();
            }
        }
        dir.delete();
    }

    public void addListener(DownloadListener listener) {
        this.downloadListener = listener;
    }

    public String getUrl() {
        return url;
    }

    public String getTaskId() {
        return taskId;
    }
}
