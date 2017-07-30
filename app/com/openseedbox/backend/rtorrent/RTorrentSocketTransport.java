package com.openseedbox.backend.rtorrent;

import com.openseedbox.backend.rtorrent.xmlrpc.IXMLRPCClientTransport;
import com.openseedbox.backend.rtorrent.xmlrpc.SCGIRequest;
import com.openseedbox.code.Util;
import play.Logger;

import java.io.*;


public class RTorrentSocketTransport implements IXMLRPCClientTransport {
	private File path;
	private Process process;
	private OutputStream outputStream;
	private InputStream inputStream;

	public RTorrentSocketTransport(File path) {
		this.path = path;
	}

	@Override
	public OutputStream getOutputStream() {
		process = Util.processBuilderStart(new String[] {"/usr/bin/socat",
				"-", String.format("%s:%s", "UNIX-CONNECT", path.getAbsolutePath())});
		if (process == null) {
			throw new RuntimeException("Unable to start socket transport");
		}
		outputStream = new FilterOutputStream(process.getOutputStream()) {
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				String payload = new String(b, off, len);
				Logger.trace("Got %d bytes", len);
				String scgiPayload = SCGIRequest.addRequiredSCGIHeaders(payload);
				Logger.trace("SCGI payload: %s", scgiPayload);
				super.write(scgiPayload.getBytes(), off, scgiPayload.getBytes().length);
			}
		};
		inputStream = process.getInputStream();

		return outputStream;
	}

	@Override
	public InputStream getInputStream() {
		return SCGIRequest.getSCGIResponse(inputStream);
	}
}
