package com.test.down.http;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface HttpClient {

    HttpURLConnection createConnect(String downloadUrl) throws IOException;
    HttpURLConnection connect() throws IOException;
}
