package com.openseedbox.backend;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractExecuteCommandSingleCallTorrentBackend extends AbstractExecuteCommandTorrentBackend {

	@Override
	public void removeTorrent(List<String> hashes) {
		for (String hash : hashes) {
			removeTorrent(hash);
		}
	}

	@Override
	public void startTorrent(List<String> hashes) {
		for (String hash : hashes) {
			startTorrent(hash);
		}
	}

	@Override
	public void stopTorrent(List<String> hashes) {
		for (String hash : hashes) {
			stopTorrent(hash);
		}

	}

	@Override
	public List<ITorrent> getTorrentStatus(List<String> hashes) {
		List<ITorrent> result = new ArrayList<ITorrent>();
		for (String hash : hashes) {
			result.add(getTorrentStatus(hash));
		}
		return result;
	}

	@Override
	public Map<String, List<IPeer>> getTorrentPeers(List<String> hashes) {
		Map<String, List<IPeer>> result = new LinkedHashMap<String, List<IPeer>>();
		for (String hash : hashes) {
			result.put(hash, getTorrentPeers(hash));
		}
		return result;
	}

	@Override
	public Map<String, List<ITracker>> getTorrentTrackers(List<String> hashes) {
		Map<String, List<ITracker>> result = new LinkedHashMap<String, List<ITracker>>();
		for (String hash : hashes) {
			result.put(hash, getTorrentTrackers(hash));
		}
		return result;
	}

	@Override
	public Map<String, List<IFile>> getTorrentFiles(List<String> hashes) {
		Map<String, List<IFile>> result = new LinkedHashMap<String, List<IFile>>();
		for (String hash : hashes) {
			result.put(hash, getTorrentFiles(hash));
		}
		return result;
	}
}
