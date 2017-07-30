package com.openseedbox.backend;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IExecuteCommandTorrent {
	/**
	 * The binary to start.
	 * In most cases returning getBinaryName() is more than enough.
	 * In other cases this may be 'screen', 'tmux' and their friends.
	 *
	 * @return
	 */
	String getStartBinaryName();

	String getBinaryName();

	File getConfigFile();

	String getConfigFileContentToWrite();

	List<String> getStartCommandParameters();

	String getDaemonPID(File pidFile) throws IOException;

	List<String> getVersionCommandParameters();
}
