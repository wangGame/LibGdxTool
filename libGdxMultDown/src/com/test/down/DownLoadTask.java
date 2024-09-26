package com.test.down;

import com.badlogic.gdx.utils.Array;
import com.kw.gdx.file.JsonUtils;
import com.test.down.bean.DownLoadInfo;
import com.test.down.http.DefaultHttpClient;
import com.test.down.http.HttpClient;
import com.test.down.http.HttpUtils;
import com.test.down.stream.FileDownloadRandomAccessFile;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

public class DownLoadTask {
    private HttpClient client;
    private int threadNum = 3;
    private Array<Thread> downLoadThread;

    public void down(String url,String out) throws IOException, IllegalAccessException {
        //存储目录
        String tempPath = out;
        this.downLoadThread = new Array<>();
        client = new DefaultHttpClient();
        HttpURLConnection connect = client.createConnect(url);
        connect.connect();
        connect = HttpUtils.redirect(connect);
        long contentLengthLong = connect.getContentLengthLong();
        System.out.println(contentLengthLong);
        //如果文件存在就删除
        File file = new File(tempPath);
        if (file.exists()){
//            file.delete();

        }
        FileDownloadRandomAccessFile randomAccessFile = new FileDownloadRandomAccessFile(file);
        randomAccessFile.setLength(contentLengthLong);
        long splitSizie = contentLengthLong / threadNum;
        long startpostion = 0;
        int uniqueId = HttpUtils.getUniqueId(url, file.getAbsoluteFile().getParent(), out, "3");
        String outFileTemp = file.getAbsoluteFile().getParent() +"/"+uniqueId +"/"+
                "temp/partfile" + uniqueId;
        DownLoadInfo read = JsonUtils.read(outFileTemp, DownLoadInfo.class);
        if (read != null){
            //文件更改就删除
            if (read.getContentLengthLong() != contentLengthLong) {
                JsonUtils.delete(outFileTemp);
            }
        }
        DownLoadInfo downLoadInfo = new DownLoadInfo();
        downLoadInfo.setUrl(url);
        downLoadInfo.setThreadNum(threadNum);
        downLoadInfo.setFilePath(file.getAbsoluteFile().getPath());
        downLoadInfo.setContentLength(contentLengthLong);
        JsonUtils.save(outFileTemp,downLoadInfo);
        int blackNum = 0;
        for (int i = 0; i < 2; i++) {
            new Thread(new SplitTask(url,startpostion,splitSizie,tempPath,blackNum++,outFileTemp))
                    .start();
            startpostion += splitSizie;
        }
        new Thread(new SplitTask(url,startpostion,contentLengthLong - startpostion,tempPath, blackNum++, outFileTemp))
                .start();
    }
}
