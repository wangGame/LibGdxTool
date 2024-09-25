package com.test.down.http;

import com.kw.gdx.utils.log.StringUtils;
import com.test.down.constant.Constant;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class HttpUtils {
    /**
     * The target resource resides temporarily under a different URI and the user agent MUST NOT
     * change the request method if it performs an automatic redirection to that URI.
     *
     * 目标资源暂时驻留在不同的URI下，用户代理不得
     * *如果请求方法执行到该URI的自动重定向，则更改请求方法。
     */
    private static final int HTTP_TEMPORARY_REDIRECT = 307;
    /**
     * The target resource has been assigned a new permanent URI and any future references to this
     * resource ought to use one of the enclosed URIs.
     *
     * 目标资源已被分配了一个新的永久URI，以及未来对此的任何引用
     * 资源应该使用其中一个封闭的URI。
     */
    private static final int HTTP_PERMANENT_REDIRECT = 308;
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


    //检测重定向
    private static boolean isRedirect(int code) {
        return code == HttpURLConnection.HTTP_MOVED_PERM        //301
                || code == HttpURLConnection.HTTP_MOVED_TEMP    //302
                || code == HttpURLConnection.HTTP_SEE_OTHER     //303
                || code == HttpURLConnection.HTTP_MULT_CHOICE   //300
                || code == HTTP_TEMPORARY_REDIRECT              //307
                || code == HTTP_PERMANENT_REDIRECT;             //308
    }

    public static HttpURLConnection redirect(HttpURLConnection connection) throws IOException, IllegalAccessException {
        int code = connection.getResponseCode();
        String location = connection.getHeaderField("Location");
        HttpURLConnection redirectConnection = connection;
        int redirectTimes = 0;
        while (isRedirect(code)) {
            if (location == null) {
                throw new IllegalAccessException(StringUtils.formatString(
                        "receive %d (redirect) but the location is null with response [%s]",
                        code, redirectConnection.getHeaderFields()));
            }
            try {
                redirectConnection.getInputStream().close();
            } catch (IOException ignored) {
            }
            redirectConnection = buildRedirectConnection(location);
            redirectTimes++;
            if (redirectTimes > 10){
                throw new IllegalAccessException("==?");
            }
        }
        return redirectConnection;
    }

    private static HttpURLConnection buildRedirectConnection(
            String newUrl) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        client.createConnect(newUrl);
        return client.connect();
    }

    public static int getUniqueId(String url,
                                  String dirPath,
                                  String fileName,
                                  String block) {

        String string = url + File.separator + dirPath + File.separator + fileName+block;

        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString().hashCode();

    }
}
