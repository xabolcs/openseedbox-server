package com.openseedbox.backend.rtorrent;

import com.openseedbox.backend.*;

import java.util.List;

public class RTorrentTorrent extends AbstractTorrent {
	@Override
	public String getName() {
		return null;
	}

	@Override
	public double getMetadataPercentComplete() {
		return 100;
	}

	@Override
	public double getPercentComplete() {
		return 0;
	}

	@Override
	public long getDownloadSpeedBytes() {
		return 0;
	}

	@Override
	public long getUploadSpeedBytes() {
		return 0;
	}

	@Override
	public String getTorrentHash() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public long getTotalSizeBytes() {
		return 0;
	}

	@Override
	public long getDownloadedBytes() {
		return 0;
	}

	@Override
	public long getUploadedBytes() {
		return 0;
	}

	@Override
	public TorrentState getStatus() {
		return null;
	}

	@Override
	public List<IFile> getFiles() {
		return null;
	}

	@Override
	public List<IPeer> getPeers() {
		return null;
	}

	@Override
	public List<ITracker> getTrackers() {
		return null;
	}

	@Override
	public String getZipDownloadLink() {
		return null;
	}
}
