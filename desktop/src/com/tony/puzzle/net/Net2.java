package com.tony.puzzle.net;

import com.badlogic.gdx.Net;

public interface Net2 {
	void sendHttpRequest(Net.HttpRequest httpRequest, Net.HttpResponseListener httpResponseListener);

	void cancelHttpRequest(Net.HttpRequest httpRequest);
}
