package com.tony.puzzle.net;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;

public class NetFactory {
	//	private static final String LAMBDASITE = "https://www.google.com.hk";
//	private static final String LAMBDASITE = "https://www.akjdfhiuwqeqe.casjfhkuwqje";
	public static final String LAMBDASITE = "https://slot.galaxyaura.com/test/slot_main";
	public static final String LAMBDASITELOW = "https://slot-regional.galaxyaura.com/test/slot_main";
	public static boolean uselownet = false;
	public static boolean uselowsite = false;

	static {
		if (Gdx.app.getType().equals(Application.ApplicationType.Android) && Gdx.app.getVersion() < 9) {
			uselownet = true;
			uselowsite = true;
		}
	}

	public static Net2 createImpl(int i) {
		if (uselownet)
			return new NetJavaImpl3(i);
		else
			return new NetJavaImpl2(i);
	}

	private static String getLambdaUrl() {
		if (uselowsite)
			return LAMBDASITELOW;
		else
			return LAMBDASITE;
//		else
//			return FUCKWEB;
	}

	public static void setUrl(Net.HttpRequest httpRequest, int high, int low) {
		if (!uselowsite) {
			httpRequest.setUrl(LAMBDASITE);
			httpRequest.setTimeOut(high);
		} else {
			httpRequest.setUrl(LAMBDASITELOW);
			httpRequest.setTimeOut(low);
		}
	}
}
