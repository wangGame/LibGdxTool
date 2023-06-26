package com.tony.puzzle.desktopnet;


import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StreamUtils;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncTask;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class NetJavaImpl3 {

    public final AsyncExecutor asyncExecutor;
    final ObjectMap<Net.HttpRequest, HttpURLConnection> connections;
    final ObjectMap<Net.HttpRequest, Net.HttpResponseListener> listeners;

    int dnsTimeout;
    HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hv =
                    HttpsURLConnection.getDefaultHostnameVerifier();
            return true;
        }
    };

    public NetJavaImpl3(int threadnum) {
        asyncExecutor = new AsyncExecutor(threadnum);
        connections = new ObjectMap<Net.HttpRequest, HttpURLConnection>();
        listeners = new ObjectMap<Net.HttpRequest, Net.HttpResponseListener>();
    }

    public void sendHttpRequest(final Net.HttpRequest httpRequest, final Net.HttpResponseListener httpResponseListener) {
        if (httpRequest.getUrl() == null) {
            httpResponseListener.failed(new GdxRuntimeException("can't process a HTTP request without URL set"));
            return;
        }
        try {
            final String method = httpRequest.getMethod();
            final URL url;
            if (method.equalsIgnoreCase(Net.HttpMethods.GET)) {
                String queryString = "";
                String value = httpRequest.getContent();
                if (value != null && !"".equals(value)) queryString = "?" + value;
                url = new URL(httpRequest.getUrl() + queryString);
            } else {
                url = new URL(httpRequest.getUrl());
            }
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setHostnameVerifier(hostnameVerifier);
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                ((HttpsURLConnection) connection).setSSLSocketFactory(new TLSSocketFactory());
            }
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setHostnameVerifier(hostnameVerifier);
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                ((HttpsURLConnection) connection).setSSLSocketFactory(new TLSSocketFactory());
           }

            // should be enabled to upload data.
            final boolean doingOutPut = method.equalsIgnoreCase(Net.HttpMethods.POST) || method.equalsIgnoreCase(Net.HttpMethods.PUT);
            connection.setDoOutput(doingOutPut);
            connection.setDoInput(true);
            connection.setRequestMethod(method);
            HttpURLConnection.setFollowRedirects(httpRequest.getFollowRedirects());

            putIntoConnectionsAndListeners(httpRequest, httpResponseListener, connection);

            // Headers get set regardless of the method
            for (Map.Entry<String, String> header : httpRequest.getHeaders().entrySet())
                connection.addRequestProperty(header.getKey(), header.getValue());

            // Set Timeouts
            connection.setConnectTimeout(httpRequest.getTimeOut());
            connection.setReadTimeout(httpRequest.getTimeOut());

            dnsTimeout = httpRequest.getTimeOut();
            final long starttime = System.currentTimeMillis();

            asyncExecutor.submit(new AsyncTask<Void>() {
                @Override
                public Void call() throws Exception {
                    if (!connections.containsKey(httpRequest))
                        return null;

                    try {
                        boolean find = DNSTest.testDNS(url.getHost(), dnsTimeout);
                        if (!find) {
                            httpResponseListener.failed(new SocketTimeoutException());
                            return null;
                        }
                    } catch (Exception e) {
                        httpResponseListener.failed(new SocketTimeoutException());
                        return null;
                    }
                    try {
                        // Set the content for POST and PUT (GET has the information embedded in the URL)
                        if (doingOutPut) {
                            // we probably need to use the content as stream here instead of using it as a string.
                            String contentAsString = httpRequest.getContent();
                            if (contentAsString != null) {
                                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                                try {
                                    writer.write(contentAsString);
                                } finally {
                                    StreamUtils.closeQuietly(writer);
                                }
                            } else {
                                InputStream contentAsStream = httpRequest.getContentStream();
                                if (contentAsStream != null) {
                                    OutputStream os = connection.getOutputStream();
                                    try {
                                        StreamUtils.copyStream(contentAsStream, os);
                                    } finally {
                                        StreamUtils.closeQuietly(os);
                                    }
                                }
                            }
                        }

                        connection.connect();

                        final HttpClientResponse clientResponse = new HttpClientResponse(connection);
                        try {
                            Net.HttpResponseListener listener = getFromListeners(httpRequest);
                            long duration = System.currentTimeMillis() - starttime;
                            duration /= 100;
                            duration *= 100;

                            if (listener != null) {
                                listener.handleHttpResponse(clientResponse);
                            }
                            removeFromConnectionsAndListeners(httpRequest);
                        } finally {
                            connection.disconnect();
                        }
                    } catch (final Exception e) {
                        connection.disconnect();
                        try {
                            httpResponseListener.failed(e);
                        } finally {
                            removeFromConnectionsAndListeners(httpRequest);
                        }
                    }

                    return null;
                }
            });
        } catch (Exception e) {
            try {
                httpResponseListener.failed(e);
            } finally {
                removeFromConnectionsAndListeners(httpRequest);
            }
            return;
        }
    }

    public void cancelHttpRequest(Net.HttpRequest httpRequest) {
        Net.HttpResponseListener httpResponseListener = getFromListeners(httpRequest);

        if (httpResponseListener != null) {
            httpResponseListener.cancelled();
            removeFromConnectionsAndListeners(httpRequest);
        }
    }

    synchronized void removeFromConnectionsAndListeners(final Net.HttpRequest httpRequest) {
        connections.remove(httpRequest);
        listeners.remove(httpRequest);
    }

    synchronized void putIntoConnectionsAndListeners(final Net.HttpRequest httpRequest,
                                                     final Net.HttpResponseListener httpResponseListener, final HttpURLConnection connection) {
        connections.put(httpRequest, connection);
        listeners.put(httpRequest, httpResponseListener);
    }

    synchronized Net.HttpResponseListener getFromListeners(Net.HttpRequest httpRequest) {
        Net.HttpResponseListener httpResponseListener = listeners.get(httpRequest);
        return httpResponseListener;
    }
}
