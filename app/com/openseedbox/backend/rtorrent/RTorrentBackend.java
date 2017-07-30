package com.openseedbox.backend.rtorrent;

import com.openseedbox.backend.*;
import com.openseedbox.backend.rtorrent.xmlrpc.IXMLRPCClientTransport;
import com.openseedbox.backend.rtorrent.xmlrpc.SXMLRPCClient;
import com.openseedbox.backend.rtorrent.xmlrpc.SXMLRPCMultiCall;
import com.openseedbox.backend.transmission.TransmissionFile;
import com.openseedbox.code.Util;
import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import sun.rmi.runtime.Log;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class RTorrentBackend extends AbstractExecuteCommandSingleCallTorrentBackend {
	private static final File CONFIG_FILE = new File(Config.getBackendBasePath(), "rtorrent.rc");
	private static final File DAEMON_PID_FILE = new File(Config.getBackendBasePath(), "rtorrent.pid");
	protected static final File SOCKET_FILE = new File("/tmp", "rtorrent.socket");
	protected static final String RPC_HOST = "127.0.0.1";

	private SXMLRPCClient rpcClient;

	public RTorrentBackend() {
		super();
		rpcClient = new SXMLRPCClient(getRTorrentTransport(), XMLRPCClient.FLAGS_8BYTE_INT /*| XMLRPCClient.FLAGS_DEBUG*/);
	}

	protected IXMLRPCClientTransport getRTorrentTransport() {
		return new RTorrentSocketTransport(SOCKET_FILE);
	};

	@Override
	public String getBinaryName() {
		return "rtorrent";
	}

	@Override
	public File getConfigFile() {
		return CONFIG_FILE;
	}

	@Override
	public String getConfigFileContentToWrite() {
		return StringUtils.join(new String[] {
				"# Instance layout (base paths)",
				String.format("method.insert = cfg.basedir, private|const|string, (cat,\"%s/\")", Config.getBackendBasePath()),
				"method.insert = cfg.complete, private|const|string, (cat,(cfg.basedir),\"complete/\")",
				"method.insert = cfg.logs,    private|const|string, (cat,(cfg.basedir),\"log/\")",
				"method.insert = cfg.logfile, private|const|string, (cat,(cfg.logs),\"rtorrent-\",(system.time),\".log\")",
				"# Create instance directories",
				"execute.throw = bash, -c, (cat,\\",
				"    \"builtin cd \\\"\", (cfg.basedir), \"\\\" \",\\",
				"    \"&& mkdir -p session download log complete\")",
				"",
				"# Tracker-less torrent and UDP tracker support",
				"dht.mode.set = auto",
				"protocol.pex.set = yes",
				"trackers.use_udp.set = yes",
				"",
				"# Peer settings",
				"throttle.min_peers.normal.set = 20",
				"throttle.max_peers.normal.set = 60",
				"throttle.min_peers.seed.set = 30",
				"throttle.max_peers.seed.set = 80",
				"",
				"# Limits for file handle resources, this is optimized for",
				"# an `ulimit` of 1024 (a common default). You MUST leave",
				"# a ceiling of handles reserved for rTorrent's internal needs!",
				"network.http.max_open.set = 50",
				"network.max_open_files.set = 600",
				"network.max_open_sockets.set = 300",
				"",
				"# Memory resource usage (increase if you have a large number of items loaded,",
				"# and/or the available resources to spend)",
				"# pieces.memory.max.set = 512M",
				"network.xmlrpc.size_limit.set = 2M",
				"",
				"# Basic operational settings (no need to change these)",
				"session.path.set = (cat, (cfg.basedir), \"session/\")",
				"directory.default.set = (cat, (cfg.basedir), \"download/\")",
				"log.execute = (cat, (cfg.logs), \"execute.log\")",
				"log.xmlrpc = (cat, (cfg.logs), \"xmlrpc.log\")",
				"",
				"# Logging:",
				"#   Levels = critical error warn notice info debug",
				"#   Groups = connection_* dht_* peer_* rpc_* storage_* thread_* tracker_* torrent_*",
				"print = (cat, \"Logging to \", (cfg.logfile))",
				"log.open_file = \"log\", (cfg.logfile)",
				"log.add_output = \"info\", \"log\"",
				"",
				"# Return path to item data (never empty, unlike `d.base_path`);",
				"# multi-file items return a path ending with a '/'.",
				"method.insert = d.data_path, simple,\\",
				"    \"if=(d.is_multi_file),\\",
				"        (cat, (d.directory), /),\\",
				"        (cat, (d.directory), /, (d.name))\"",
				"# Move completed torrents",
				"method.set_key = event.download.finished,move_complete,\"execute=mv,-u,$d.data_path=,$cfg.complete=; d.directory.set=(cat, (cfg.complete))\"",
				"",
				"# Creates a pid-file",
				String.format("execute = {bash,-c,echo `ps -p \"$\\{1:-$$\\}\" -o ppid=` > %s}", getDaemonPidFilePath()),
				"# Force UTF-8 encoding as XMLRPC requires it",
				"encoding_list = UTF-8",
				"# Allow incoming encrypted connections, starts unencrypted",
				"# outgoing connections but retries with encryption if they fail, preferring",
				"# plaintext to RC4 encryption after the encrypted handshake",
				"encryption = allow_incoming,enable_retry,prefer_plaintext",
				""
		}, "\n");
	}

	@Override
	public List<String> getStartCommandParameters() {
		// scgi_port or scgi_local parameters should be passed as start parameters
		// to avoid errors in config reloading (by import command)
		return Arrays.asList(
				// bash:
				//"-c", "TERM=xterm", "&&", getBinaryName(),
				// screen:
				//"-S", getBinaryName(), "-fa", "-d", "-m",
				//getBinaryName(),
				"-n",
				"-O", String.format("import=%s", getConfigFile().getAbsolutePath()),
				"-O", String.format("scgi_local=%s", SOCKET_FILE)
		);
	}

	@Override
	public List<String> getVersionCommandParameters() {
		return Arrays.asList(
				"-h 2>/dev/null",
				"|",
				"head -n 1"
		);
	}

	@Override
	public boolean isInstalled() {
		return !StringUtils.isEmpty(Util.executeCommand(String.format("which %s", getStartBinaryName()))) && super.isInstalled();
	}

	@Override
	public String getName() {
		return "rTorrent";
	}

	@Override
	public ITorrent addTorrent(File file) {
		return null;
	}

	@Override
	protected void reloadConfig() {
		/*
		./xmlrpc2scgi.py - scgi://127.0.0.1:5001 import "" ../openseedbox-localdata/com.openseedbox.backend.rtorrent.RTorrentBackend/rtorrent.rc
		host:127.0.0.1 port:5001
		inet_host:127.0.0.1
		<?xml version="1.0" encoding="UTF-8"?>
		<methodResponse>
			<fault>
				<value><struct>
					<member><name>faultCode</name>
					<value><i4>-503</i4></value></member>
					<member><name>faultString</name>
					<value><string>Error in option file: ../openseedbox-localdata/com.openseedbox.backend.rtorrent.RTorrentBackend/rtorrent.rc:9: SCGI already enabled.</string></value></member>
				</struct></value>
			</fault>
		</methodResponse>
		 */
	}

	@Override
	protected String getDaemonPidFilePath() {
		return DAEMON_PID_FILE.getAbsolutePath();
	}

	@Override
	public ITorrent addTorrent(String urlOrMagnet) {
		try {
			Object rpcFiles = rpcClient.call("load.start",
					"",
					"https://cdimage.debian.org/debian-cd/current/amd64/bt-cd/debian-8.8.0-amd64-CD-1.iso.torrent"
			);
			Logger.debug(rpcFiles.toString());
			rpcFiles = rpcClient.call("load.start",
					"",
					"https://cdimage.debian.org/debian-cd/current/amd64/bt-cd/debian-mac-8.8.0-amd64-netinst.iso.torrent"
			);
			Logger.debug(rpcFiles.toString());
		} catch (XMLRPCException e) {
			Logger.error(e, "");
		}
		return null;
	}

	@Override
	public void removeTorrent(String hash) {

	}

	@Override
	public void startTorrent(String hash) {

	}

	@Override
	public void stopTorrent(String hash) {

	}

	@Override
	public ITorrent getTorrentStatus(String hash) {
		return null;
	}

	@Override
	public List<IPeer> getTorrentPeers(String hash) {
		return null;
	}

	@Override
	public List<ITracker> getTorrentTrackers(String hash) {
		return null;
	}

	@Override
	public List<IFile> getTorrentFiles(String hash) {
		try {
			Object rpcFiles;
			rpcFiles = rpcClient.call("f.multicall",
					"07AF529B0C23522A837603F79F6A9BBE4F2AB303",
					"",
					"f.path=",
					"f.size_bytes=",
					"f.priority=",
					"f.completed_chunks=",
					"f.size_chunks=",
					"f.frozen_path="
			);
			//Logger.debug(rpcFiles.toString());
			/*rpcFiles = rpcClient.call("f.multicall",
					"462F06B20A1160191A08F4FBC2E46449193DED35", //"FA23C3F58017D1322D27FA761118B2FC2177F730",
					"07AF529B0C23522A837603F79F6A9BBE4F2AB303",
					"",
					"f.path=",
					"f.size_bytes=",
					"f.priority=",
					"f.completed_chunks=",
					"f.size_chunks=",
					"f.frozen_path="
			);*/
			List<IFile> result;
			result = new ArrayList<RTorrentFile>(new SXMLRPCMultiCall<RTorrentFile>(rpcClient, () -> {
				RTorrentFile file = new RTorrentFile();
				file.setTorrentHash("462F06B20A1160191A08F4FBC2E46449193DED35");
				return file;
			}).getMultiCall()/*.stream().map(f-> {
				Logger.info("hash: %s", f.getTorrentHash());
				Logger.info("size: %s", f.getFileSizeBytes());
				Logger.info("name: %s", f.getName());
				Logger.info("want: %s", f.isWanted());
				Logger.info("id:   %s", f.getId());
				return f;
			}).collect(Collectors.toList())*/
			).stream().map(e -> {
				Logger.info(e.getClass().getName());
				return e;
			}).collect(Collectors.toList());
			return result;
		} catch (XMLRPCException e) {
			Logger.error(e, "");
		}
		return null;
	}

	@Override
	public void modifyTorrentFiles(String hash, List<IFile> files) {

	}

	@Override
	public void modifyTorrent(String hash, double seedRatio, long uploadLimitBytes, long downloadLimitBytes) {

	}

	@Override
	public List<ITorrent> listTorrents() {
		try {
			Object rpcFiles = rpcClient.call("d.multicall2",
					"", "",
					"d.hash=",
					"d.name=",
					"d.state=",
					"d.down.rate=",
					"d.up.rate=",
					"d.peers_connected=",
					"d.peers_not_connected=",
					"d.peers_accounted=",
					"d.bytes_done=",
					"d.up.total=",
					"d.size_bytes=",
					"d.creation_date=",
					"d.left_bytes=",
					"d.complete=",
					"d.is_active=",
					"d.is_hash_checking=",
					"d.base_path=",
					"d.base_filename=",
					"d.message=",
					"d.peers_complete=",
					"d.peers_accounted=",
					"d.is_open="
			);
		} catch (XMLRPCException e) {
			Logger.error(e, e.getMessage());
		}
		return new ArrayList<>();
	}

	@Override
	public ISessionStatistics getSessionStatistics() {
		return null;
	}
}
