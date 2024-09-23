package com.test.down.http;

import com.test.down.constant.Constant;

import java.net.HttpURLConnection;
import java.util.Locale;

public class HttpUtils {
    public static void setHeader(HttpURLConnection connection){
        connection.setReadTimeout(Constant.readTimeOut);
        connection.setConnectTimeout(Constant.connectTimeOut);
        connection.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
        connection.setRequestProperty("Accept-Language", String.valueOf(Locale.getDefault()));
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Accept-Encoding", "identity");
    }
}
