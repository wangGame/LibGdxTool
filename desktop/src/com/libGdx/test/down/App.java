package com.libGdx.test.down;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 简单的下载案例
 */
public class App {
    public static void main(String[] args) {
        download2();
    }

    public static void download2(){

        //Builder构建者模式 可参考https://www.jianshu.com/p/afe090b2e19c
        //创建 okHttpClient 实例
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //创建 Request 实例
        Request request = new Request.Builder()
                .url("https://gaoshanren.cdn-doodlemobile.com/Art_Puzzle/level_resource/version7/level/level160.zip")
                .get()
                .build();
        //同步请求
        new Thread( new Runnable(){
            @Override
            public void run() {
                //发起同步请求的方式，返回一个 Response类的值
                Response response = null;
                try {
                    //🌟发起同步请求
                    response = okHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    String result = response.body().string();
                    System.out.println(result);
                    //如果需要更新主线程的 UI 如果使用 rxjava 以及 retrofit 就不用这样处理了
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////                            binding.title.setText(result);
//                        }
//                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private static void download() {
        try {
            URL url = new URL("https://gaoshanren.cdn-doodlemobile.com/Art_Puzzle/level_resource/version7/level/level160.zip");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            if (connection instanceof HttpsURLConnection){
                ((HttpsURLConnection)(connection)).setSSLSocketFactory(new TLSSocketFactory1());
            }
            connection.setRequestMethod("GET");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 获取响应码
                    int responseCode = 0;
                    try {
                        responseCode = connection.getResponseCode();

                        // 检查响应码是否成功
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            // 获取输入流
                            InputStream inputStream = connection.getInputStream();
                            // 创建一个缓冲读取器来读取输入流
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            // 用于保存读取的数据
                            StringBuilder response = new StringBuilder();
                            // 读取响应数据
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            // 关闭读取器和输入流
                            reader.close();
                            inputStream.close();
                            // 将响应数据转换为字符串
                            String responseData = response.toString();
                            System.out.println(responseData);
                            // 现在您可以使用responseData进行后续处理
                        } else {
                            // 处理非成功响应码
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Thread.yield();
    }
}
