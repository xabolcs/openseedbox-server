package com.openseedbox.backend.rtorrent.xmlrpc.mapper;

public class DownloadsMapper {

	public class DownloadHash extends StringXMLRPCMapper {
		public DownloadHash(String methodNameParam) {
			super(methodNameParam);
			methodName = "d.hash";
		}
	}

	public class DownloadName extends StringXMLRPCMapper {
		public DownloadName(String methodNameParam) {
			super(methodNameParam);
			methodName = "d.name";
		}
	}
}
