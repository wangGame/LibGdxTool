package kw.tony.request;

import java.util.HashMap;
import java.util.List;

public class DownLoadRequest {
    private String url;
    private int readTimeOut;
    private int connectTimeOut;
    private long downloadedBytes;


    public String getUrl() {
        return null;
    }

    public int getReadTimeOut() {
        return 0;
    }

    public int getConnectTimeOut() {
        return 0;
    }

    public Object getDownloadedBytes() {
        return null;
    }

    public String getUserAgent() {
        return null;
    }

    public HashMap<String, List<String>> getHeaders() {
        return null;
    }
}
