package com.openseedbox.backend.rtorrent.xmlrpc;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * echo "NDY6Q09OVEVOVF9MRU5HVEgAMjA0AFNDR0kAMQBSRVFVRVNUX01FVEhPRABQT1NUACw8P3htbCB2ZXJzaW9uPSIxLjAiID8+PG1ldGhvZENhbGw+PG1ldGhvZE5hbWU+ZXhlY3V0ZV9jYXB0dXJlPC9tZXRob2ROYW1lPjxwYXJhbXM+PHBhcmFtPjx2YWx1ZT48c3RyaW5nPmxzPC9zdHJpbmc+PC92YWx1ZT48L3BhcmFtPjxwYXJhbT48dmFsdWU+PHN0cmluZz4tYWw8L3N0cmluZz48L3ZhbHVlPjwvcGFyYW0+PC9wYXJhbXM+PC9tZXRob2RDYWxsPgo=" | base64 -d | socat - UNIX-CONNECT:/tmp/rtorrent.sock
 *
 * See also: https://en.wikipedia.org/wiki/Simple_Common_Gateway_Interface
 * See also: https://github.com/rakshasa/rtorrent-vagrant/blob/4084e3f9f44eb59895cb2a689b3b6ed120aed468/scripts/xmlrpc2scgi.py
 * See also: https://pastebin.com/SCM6n3mZ
 */
public class SCGIRequest {
	private static String encodeNetstring(String s) {
		return String.format("%d:%s", s.length(), s);
	}

	private static String makeHeaders(Map<String, String> headers) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> e : headers.entrySet()) {
			sb.append(e.getKey()).append("\0").append(e.getValue()).append("\0");
		}
		return sb.toString();
	}

	public static String addRequiredSCGIHeaders(String data) {
		return addRequiredSCGIHeaders(data, new HashMap<String, String>());
	}

	public static String addRequiredSCGIHeaders(String data, Map<String, String> headers) {
		Map<String, String> h = new LinkedHashMap<String, String>();
		h.put("CONTENT_LENGTH", "");
		h.put("SCGI", "");
		h.putAll(headers);
		h.put("CONTENT_LENGTH", String.valueOf(data.length()));
		h.put("SCGI", "1");
		return String.format("%s,%s", encodeNetstring(makeHeaders(h)), data);
	}

	public static InputStream getSCGIResponse(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		try {
			while (!StringUtils.isBlank(reader.readLine())) {
				// noop
			}
		} catch (IOException e) {
			// noop
		}
		InputStream ris = new ReaderInputStream(reader, StandardCharsets.UTF_8);
		return ris;
	}
}
