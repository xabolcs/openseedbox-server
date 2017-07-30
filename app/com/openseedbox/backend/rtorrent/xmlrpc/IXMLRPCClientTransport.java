package com.openseedbox.backend.rtorrent.xmlrpc;

import java.io.InputStream;
import java.io.OutputStream;

public interface IXMLRPCClientTransport {
	OutputStream getOutputStream();
	InputStream getInputStream();
}
