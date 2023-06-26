/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.tony.puzzle.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponseListener;
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
import java.util.Map;

/**
 * Implements part of the {@link Net} API using {@link HttpURLConnection}, to be easily reused between the Android and Desktop
 * backends.
 *
 * @author acoppes
 */
public class NetJavaImpl2 implements Net2 {

	final ObjectMap<HttpRequest, HttpURLConnection> connections;
	final ObjectMap<HttpRequest, HttpResponseListener> listeners;
	private final AsyncExecutor asyncExecutor;
	int dnsTimeout;

	public NetJavaImpl2(int threadnum) {
		asyncExecutor = new AsyncExecutor(threadnum);
		connections = new ObjectMap<HttpRequest, HttpURLConnection>();
		listeners = new ObjectMap<HttpRequest, HttpResponseListener>();
	}

	@Override
	public void sendHttpRequest(final HttpRequest httpRequest, final HttpResponseListener httpResponseListener) {
		if (httpRequest.getUrl() == null) {
			httpResponseListener.failed(new GdxRuntimeException("can't process a HTTP request without URL set"));
			return;
		}

		try {
			final String method = httpRequest.getMethod();
			final URL url;

			if (method.equalsIgnoreCase(HttpMethods.GET)) {
				String queryString = "";
				String value = httpRequest.getContent();
				if (value != null && !"".equals(value))
					queryString = "?" + value;
				url = new URL(httpRequest.getUrl() + queryString);
			} else {
				url = new URL(httpRequest.getUrl());
			}

			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// should be enabled to upload data.
			final boolean doingOutPut = method.equalsIgnoreCase(HttpMethods.POST) || method.equalsIgnoreCase(HttpMethods.PUT);
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

						final HttpClientResponse2 clientResponse = new HttpClientResponse2(connection);
						try {
							HttpResponseListener listener = getFromListeners(httpRequest);
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

	@Override
	public void cancelHttpRequest(HttpRequest httpRequest) {
		HttpResponseListener httpResponseListener = getFromListeners(httpRequest);

		if (httpResponseListener != null) {
			httpResponseListener.cancelled();
			removeFromConnectionsAndListeners(httpRequest);
		}
	}

	synchronized void removeFromConnectionsAndListeners(final HttpRequest httpRequest) {
		connections.remove(httpRequest);
		listeners.remove(httpRequest);
	}

	synchronized void putIntoConnectionsAndListeners(final HttpRequest httpRequest,
													 final HttpResponseListener httpResponseListener, final HttpURLConnection connection) {
		connections.put(httpRequest, connection);
		listeners.put(httpRequest, httpResponseListener);
	}

	synchronized HttpResponseListener getFromListeners(HttpRequest httpRequest) {
		HttpResponseListener httpResponseListener = listeners.get(httpRequest);
		return httpResponseListener;
	}
}
