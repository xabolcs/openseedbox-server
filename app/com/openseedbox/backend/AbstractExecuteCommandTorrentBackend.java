package com.openseedbox.backend;

import com.openseedbox.code.MessageException;
import com.openseedbox.code.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import play.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractExecuteCommandTorrentBackend extends AbstractTorrentBackend implements IExecuteCommandTorrent {

	protected static Process backendProcess = null;

	private void sleepWhile(boolean running) {
		int count = 0;
		int limit = 20; //prevent infinite loop
		try {
			if (running) {
				while (isRunning()) {
					if (count > limit) {
						throw new RuntimeException("Process didnt start in 10 seconds, exiting to prevent infinite loop.");
					}
					Thread.sleep(500);
					count++;
				}
			} else {
				while (!isRunning()) {
					if (count > limit) {
						Logger.error("Process output: %s", Util.inputStreamToString(backendProcess.getInputStream()));
						throw new RuntimeException("Process didnt die in 10 seconds, exiting to prevent infinite loop.");
					}
					Thread.sleep(500);
					count++;
				}
			}
		} catch (InterruptedException ex) {
			//no state to clean up
		}
	}

	protected final void writeConfigFile() {
		try {
			FileUtils.writeStringToFile(getConfigFile(), getConfigFileContentToWrite(), "UTF-8");
			if (this.isRunning()) {
				reloadConfig();
			}
		} catch (IOException ex) {
			throw new MessageException(ex, String.format("Unable to write %s config file", getName()));
		}
	}

	protected abstract void reloadConfig();

	protected abstract String getDaemonPidFilePath();

	/***
	 * IExecuteCommandTorrent
	 *
	 */

	@Override
	public String getStartBinaryName() {
		return getBinaryName();
	}

	@Override
	public String getDaemonPID(File pidFile) throws IOException {
		String pid = FileUtils.readFileToString(pidFile);
		if (StringUtils.isEmpty(pid)) {
			pid = StringUtils.trim(Util.executeCommand(String.format("pidof %s | awk '{print $1}'", getBinaryName())));
		}
		return pid;
	}

	/**
	 * IExecuteCommandTorrent
	 *
	 */

	/**
	 * ITorrentBackend
	 *
	 */

	@Override
	public boolean isInstalled() {
		String output = Util.executeCommand(String.format("which %s", getBinaryName()));
		return !StringUtils.isEmpty(output);
	}

	@Override
	public String getVersion() {
		return Util.executeCommand(String.format("%s %s 2>/dev/null", getBinaryName(), StringUtils.join(getVersionCommandParameters(), " ")));
	}

	@Override
	public void start() {
		if (!isRunning()) {
			writeConfigFile();
			List<String> command = new ArrayList<String>();
			command.add(Util.executeCommand("which " + getStartBinaryName()));
			command.addAll(getStartCommandParameters());

			Logger.info("Starting %s via: %s", getBinaryName(), StringUtils.join(command, " "));
			backendProcess = Util.processBuilderStart(command.toArray(new String[]{}));

			sleepWhile(false);
		}
	}

	@Override
	public void stop() {
		if (isRunning()) {
			try {
				if (backendProcess != null) {
					backendProcess.destroy();
				} else {
					String pid = getDaemonPID(new File(getDaemonPidFilePath()));
					Util.executeCommand("kill " + pid);
				}
				sleepWhile(true);
				Logger.info("Stopped " + getName());
			} catch (IOException ex) {
				Logger.info("Unable to read PID file: %s", ex);
			}
		}
		new File(getDaemonPidFilePath()).delete();
	}

	@Override
	public boolean isRunning() {
		File pidFile = new File(getDaemonPidFilePath());
		if (pidFile.exists()) {
			try {
				String pid = getDaemonPID(pidFile);
				return !StringUtils.isEmpty(Util.executeCommand(String.format("ps --no-headers -p %s", pid)).trim());
			} catch (IOException ex) {
				Logger.error(String.format("Unable to read %s file", pidFile.getName()), ex);
				return false;
			}
		}
		return false;
	}
}
