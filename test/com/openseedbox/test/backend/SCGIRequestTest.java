package com.openseedbox.test.backend;

import com.openseedbox.backend.rtorrent.xmlrpc.SCGIRequest;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import play.test.UnitTest;

import java.util.HashMap;
import java.util.Map;

public class SCGIRequestTest extends UnitTest {
	private final static String SCGI_PAYLOAD_BASE64 = "NDY6Q09OVEVOVF9MRU5HVEgAMjA0AFNDR0kAMQBSRVFVRVNUX01FVEhPRABQT1NUACw8P3htbCB2ZXJzaW9uPSIxLjAiID8+PG1ldGhvZENhbGw+PG1ldGhvZE5hbWU+ZXhlY3V0ZV9jYXB0dXJlPC9tZXRob2ROYW1lPjxwYXJhbXM+PHBhcmFtPjx2YWx1ZT48c3RyaW5nPmxzPC9zdHJpbmc+PC92YWx1ZT48L3BhcmFtPjxwYXJhbT48dmFsdWU+PHN0cmluZz4tYWw8L3N0cmluZz48L3ZhbHVlPjwvcGFyYW0+PC9wYXJhbXM+PC9tZXRob2RDYWxsPgo=";
	private final static String PAYLOAD = "<?xml version=\"1.0\" ?><methodCall><methodName>execute_capture</methodName><params><param><value><string>ls</string></value></param><param><value><string>-al</string></value></param></params></methodCall>\n";

	@Test
	public void testPayload() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("REQUEST_METHOD", "POST");
		String scgiPayload = SCGIRequest.addRequiredSCGIHeaders(PAYLOAD, headers);
		assertEquals(new String(Base64.decodeBase64(SCGI_PAYLOAD_BASE64)), scgiPayload);
	}
}
