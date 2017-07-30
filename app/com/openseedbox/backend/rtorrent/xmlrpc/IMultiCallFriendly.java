package com.openseedbox.backend.rtorrent.xmlrpc;

import com.openseedbox.backend.rtorrent.xmlrpc.mapper.IXMLRPCMapper;

public interface IMultiCallFriendly {
	String getMultiCallMethod();
	IXMLRPCMapper[] getMultiCallParams();
}
