package com.test.down.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DefaultHttpClient implements HttpClient{
    private HttpURLConnection connection;
    public HttpURLConnection createConnect(String downloadUrl) throws IOException {
        URL url =  new URL(downloadUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Referer", downloadUrl);
        HttpUtils.setHeader(connection);
        return connection;
    }

    public HttpURLConnection connect() throws IOException {
        connection.connect();
        return connection;
    }


}
