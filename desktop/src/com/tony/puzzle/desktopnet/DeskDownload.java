package com.tony.puzzle.desktopnet;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.kw.gdx.utils.log.NLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.downLoad.BaseDownLoadUtils;
import kw.artpuzzle.net.DownLoad;

public class DeskDownload extends DownLoad {
    com.tony.puzzle.net.NetJavaImpl2 downloadnet;
    @Override
    public void downloadOneFile(String siteusing, String fromPath,
                                String toPath,
                                BaseDownLoadUtils.DownLoadListener onComplete,
                                BaseDownLoadUtils.DownLoadListener onFail) {
        downloadnet = new com.tony.puzzle.net.NetJavaImpl2(4);
        System.out.println(siteusing+fromPath);
        startDownload(siteusing + fromPath, toPath, onComplete,onFail);
    }

//    http://gaoshanren.cdn-doodlemobile.com/Art_Puzzle/level_resource/verion1/level0.zip
    private void startDownload(String url, String path,
                               BaseDownLoadUtils.DownLoadListener runnable,
                               BaseDownLoadUtils.DownLoadListener onFail) {
        NLog.i("download uri: %s",url);
        final Net.HttpRequest down1 = new Net.HttpRequest();
        down1.setUrl(url);
        down1.setMethod("GET");
        down1.setTimeOut(90000);
        downloadnet.sendHttpRequest(down1, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    if (httpResponse.getStatus().getStatusCode() == 200) {
                        int todownsize = Integer.parseInt(httpResponse.getHeader("Content-Length"));
                        InputStream is = null;
                        RandomAccessFile randomFile = null;
                        try {
                            is = httpResponse.getResultAsStream();
                            FileHandle file = Gdx.files.local(path);
                            if (!file.parent().exists()) {
                                file.parent().mkdirs();
                            }

                            if (file.exists()) {
                                NLog.e("delete file " + path);
                                file.delete();
                                NLog.e("still exist " + file.exists());
                            }
                            randomFile = new RandomAccessFile(file.parent() + "/" + file.name(), "rwd");
                            randomFile.setLength(todownsize);
                            randomFile.seek(0);
                            byte[] buffer = new byte[1024];
                            int len = -1;
                            while ((len = is.read(buffer)) != -1) {

                                randomFile.write(buffer, 0, len);
                            }
                            randomFile.close();
                            is.close();
                            runnable.downLoadCallBack();

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("------------");
                            onFail.downLoadCallBack(-1);
                            try {
                                randomFile.close();
                                is.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }else {
                        byte[] result = httpResponse.getResult();
                        NLog.i(httpResponse.getStatus().getStatusCode());
                        onFail.downLoadCallBack(-1);
                        FileHandle local = Gdx.files.local("403.zip");
                        local.writeBytes(result,false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onFail.downLoadCallBack(-1);
                }
            }
            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
                onFail.downLoadCallBack();
            }

            @Override
            public void cancelled() {
                onFail.downLoadCallBack(-1);
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
//        for (int i = 0; i < 200; i++) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title="TriPeaks";
        config.x = 0;
        config.y = 0;
        config.height = (int) (640*1.5F);
        config.width = (int) (360*1.5F);
        new LwjglApplication(new Game() {
            @Override
            public void create() {
//                request.setUrl("http://gaoshanren.cdn-doodlemobile.com/Art_Puzzle/level_resource/version7/level/level160.zip");
                DeskDownload deskDownload = new DeskDownload();
    //                for (int i = 0; i < 100; i++) {
                    String pa = Gdx.files.getLocalStoragePath()+"level/level"+1+".zip";
    //                https://cloudflare-content.easybrain.com/com.easybrain.art.puzzle/Android/v3/remotepreview_assets_00001.bundle
                    deskDownload.downloadOneFile(
                            "https://gaoshanren.cdn-doodlemobile.com/Art_Puzzle/level_resource/",
                            "version7/level/level160.zip",
                            pa,
                            new BaseDownLoadUtils.DownLoadListener(){},
                            new BaseDownLoadUtils.DownLoadListener(){}
                            );


                    try {
                        int read = System.in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
//            }
        }, config);

    }
}
