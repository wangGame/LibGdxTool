package com.tony.puzzle.net;

import java.net.InetAddress;

public class DNSTest {
	public static boolean testDNS(String hostname, int timeout) {
		try {
			DNSResolver dnsRes = new DNSResolver(hostname);
			Thread t = new Thread(dnsRes);
			t.start();
			t.join(timeout);
			InetAddress inetAddr = dnsRes.get();
			System.out.println(inetAddr);
			return inetAddr != null;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
