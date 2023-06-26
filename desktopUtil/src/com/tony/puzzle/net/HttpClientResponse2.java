package com.tony.puzzle.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

class HttpClientResponse2 implements Net.HttpResponse {
	private final HttpURLConnection connection;
	private HttpStatus status;

	public HttpClientResponse2(HttpURLConnection connection) throws IOException {
		this.connection = connection;
		try {
			this.status = new HttpStatus(connection.getResponseCode());
		} catch (IOException e) {
			this.status = new HttpStatus(-1);
		}
	}

	@Override
	public byte[] getResult() {
		InputStream input = getInputStream();

		// If the response does not contain any content, input will be null.
		if (input == null) {
			return StreamUtils.EMPTY_BYTES;
		}

		try {
			return StreamUtils.copyStreamToByteArray(input, connection.getContentLength());
		} catch (IOException e) {
			return StreamUtils.EMPTY_BYTES;
		} finally {
			StreamUtils.closeQuietly(input);
		}
	}

	@Override
	public String getResultAsString() {
		InputStream input = getInputStream();

		// If the response does not contain any content, input will be null.
		if (input == null) {
			return "";
		}

		try {
			return StreamUtils.copyStreamToString(input, connection.getContentLength());
		} catch (IOException e) {
			return "";
		} finally {
			StreamUtils.closeQuietly(input);
		}
	}

	@Override
	public InputStream getResultAsStream() {
		return getInputStream();
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getHeader(String name) {
		return connection.getHeaderField(name);
	}

	@Override
	public Map<String, List<String>> getHeaders() {
		return connection.getHeaderFields();
	}

	private InputStream getInputStream() {
		try {
			return connection.getInputStream();
		} catch (IOException e) {
			return connection.getErrorStream();
		}
	}
}
