package kw.tony.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import kw.tony.Constant;
import kw.tony.request.DownLoadRequest;

public class HttpClient {
    private URLConnection connection;
    public void download(DownLoadRequest request) throws Exception {
        connection = new URL(request.getUrl()).openConnection();
        connection.setReadTimeout(request.getReadTimeOut());
        connection.setConnectTimeout(request.getConnectTimeOut());
        final String range = String.format(Locale.ENGLISH,
                "bytes=%d-", request.getDownloadedBytes());
        connection.addRequestProperty(Constant.RANGE,range);
        connection.addRequestProperty(Constant.USER_AGENT, request.getUserAgent());
        addHeaders(request);
        connection.connect();
    }

    private void addHeaders(DownLoadRequest request) {
        final HashMap<String, List<String>> headers = request.getHeaders();
        if (headers != null) {
            Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
            for (Map.Entry<String, List<String>> entry : entries) {
                String name = entry.getKey();
                List<String> list = entry.getValue();
                if (list != null) {
                    for (String value : list) {
                        connection.addRequestProperty(name, value);
                    }
                }
            }
        }
    }
}
