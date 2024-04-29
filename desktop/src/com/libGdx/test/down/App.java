package com.libGdx.test.down;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 简单的下载案例
 */
public class App {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://gaoshanren.cdn-doodlemobile.com/Art_Puzzle/level_resource/version7/level/level160.zip");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
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
