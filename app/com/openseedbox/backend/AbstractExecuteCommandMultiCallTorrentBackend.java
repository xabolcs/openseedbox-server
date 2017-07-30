package com.openseedbox.backend;

import java.util.List;

public abstract class AbstractExecuteCommandMultiCallTorrentBackend extends AbstractExecuteCommandTorrentBackend {

	@Override
	public void removeTorrent(String hash) {
		removeTorrent(StringAsStringList(hash));
	}

	@Override
	public void startTorrent(String hash) {
		startTorrent(StringAsStringList(hash));
	}

	@Override
	public void stopTorrent(String hash) {
		stopTorrent(StringAsStringList(hash));
	}

	@Override
	public ITorrent getTorrentStatus(String hash) {
		return getTorrentStatus(StringAsStringList(hash)).get(0);
	}

	@Override
	public List<IPeer> getTorrentPeers(String hash) {
		return getTorrentPeers(StringAsStringList(hash)).get(hash);
	}

	@Override
	public List<ITracker> getTorrentTrackers(String hash) {
		return getTorrentTrackers(StringAsStringList(hash)).get(hash);
	}

	@Override
	public List<IFile> getTorrentFiles(String hash) {
		return getTorrentFiles(StringAsStringList(hash)).get(hash);
	}

}
