package com.openseedbox.backend.rtorrent.xmlrpc.mapper;

public class FileMapper {

	public static class FilePath extends StringXMLRPCMapper {
		public FilePath(String methodNameParam) {
			super(methodNameParam);
			methodName = "f.path";
		}
	}

	public static class FileFrozenPath extends StringXMLRPCMapper {
		public FileFrozenPath(String methodNameParam) {
			super(methodNameParam);
			methodName = "f.frozen_path";
		}
	}
	public static class FileSizeInBytes extends LongXMLRPCMapper {
		public FileSizeInBytes(String methodNameParam) {
			super(methodNameParam);
			methodName = "f.size_bytes";
		}
	}

	public static class FileSizeInChunks extends LongXMLRPCMapper {
		public FileSizeInChunks(String methodNameParam) {
			super(methodNameParam);
			methodName = "f.size_chunks";
		}
	}

	public static class FilePriority extends LongXMLRPCMapper {
		public FilePriority(String methodNameParam) {
			super(methodNameParam);
			methodName = "f.priority";
		}
	}

	public static class FileCompletedChunks extends LongXMLRPCMapper {
		public FileCompletedChunks(String methodNameParam) {
			super(methodNameParam);
			methodName = "f.completed_chunks";
		}
	}
}
