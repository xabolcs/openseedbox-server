package com.openseedbox.backend.rtorrent;

import com.openseedbox.backend.AbstractFile;
import com.openseedbox.backend.rtorrent.xmlrpc.IMultiCallFriendly;
import com.openseedbox.backend.rtorrent.xmlrpc.mapper.AbstractXMLRPCMapper;
import com.openseedbox.backend.rtorrent.xmlrpc.mapper.FileMapper.*;
import com.openseedbox.backend.rtorrent.xmlrpc.mapper.IXMLRPCMapper;
import com.openseedbox.code.MessageException;
import org.apache.commons.lang.StringUtils;
import org.h2.store.fs.FileUtils;

public class RTorrentFile extends AbstractFile implements IMultiCallFriendly {

	private int index;
	private long chunkSize;
	private String torrentHash = null;

	private FileCompletedChunks completedChunks;
	private FilePath path;
	private FileSizeInBytes sizeInBytes;
	private FileSizeInChunks sizeInChunks;
	private FilePriority priority;

	public RTorrentFile() {
		completedChunks = new FileCompletedChunks("");
		path = new FilePath("");
		sizeInBytes = new FileSizeInBytes("");
		sizeInChunks = new FileSizeInChunks("");
		priority = new FilePriority("");
	}

	public FileCompletedChunks getCompletedChunks() {
		return completedChunks;
	}

	public FilePath getPath() {
		return path;
	}

	public FileSizeInBytes getSizeInBytes() {
		return sizeInBytes;
	}

	public FileSizeInChunks getSizeInChunks() {
		return sizeInChunks;
	}

	public FilePriority getPriorityMapper() {
		return priority;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTorrentHash() {
		if (StringUtils.isEmpty(torrentHash)) {
			throw new MessageException("You forgot to call setTorrentHash!");
		}
		return torrentHash;
	}

	public void setTorrentHash(String torrentHash) {
		this.torrentHash = torrentHash;
	}

	public void setChunkSize(long chunkSize) {
		this.chunkSize = chunkSize;
	}

	@Override
	public String getMultiCallMethod() {
		return "f.multicall";
	}

	@Override
	public IXMLRPCMapper[] getMultiCallParams() {
		return new IXMLRPCMapper[] {
				new AbstractXMLRPCMapper.DummyStringMapper(getTorrentHash()),
				new AbstractXMLRPCMapper.DummyStringMapper(""),
				completedChunks,
				path,
				sizeInBytes,
				sizeInChunks,
				priority
		};
	}

	@Override
	public String getId() {
		return "" + index;
	}

	@Override
	public String getName() {
		return FileUtils.getName(path.getValueObject());
	}

	@Override
	public String getFullPath() {
		return path.getValueObject();
	}

	@Override
	public boolean isWanted() {
		return priority.getValueObject() != 0;
	}

	@Override
	public long getBytesCompleted() {
		if (completedChunks.getValueObject().equals(sizeInChunks.getValueObject())) {
			return sizeInBytes.getValueObject();
		}
		if (chunkSize == 0) {
			throw new MessageException("You forgot to call setChunkSize!");
		}
		return completedChunks.getValueObject() * chunkSize;
	}

	@Override
	public long getFileSizeBytes() {
		return sizeInBytes.getValueObject();
	}

	@Override
	public int getPriority() {
		return priority.getValueObject().intValue();
	}

	@Override
	public String getDownloadLink() {
		return "nope!";
	}
}
