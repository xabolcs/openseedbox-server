package com.openseedbox.backend;

import com.openseedbox.code.MessageException;
import com.openseedbox.code.Util;
import com.turn.ttorrent.bcodec.BDecoder;
import com.turn.ttorrent.bcodec.BEValue;
import com.turn.ttorrent.bcodec.BEncoder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractTorrentBackend implements ITorrentBackend {

	protected final List<String> StringAsStringList (String s) {
		return Arrays.asList(new String[] { s });
	}

	protected final String getHashFromBase64(String b64) {
		byte[] data = Base64.decodeBase64(b64);
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		return getHashFromInputStream(bais);
	}

	protected final String getHashFromMagnet(String magnet) {
		Map<String, String> params = Util.getUrlParameters(magnet.replace("magnet:", ""));
		if (params.containsKey("xt")) {
			return params.get("xt").replace("urn:btih:", "");
		}
		throw new MessageException("Unable to get torrent hash from magnet link, are you sure its valid?");
	}

	protected final String getHashFromInputStream(InputStream is) {
		try {
			Map<String, BEValue> map = BDecoder.bdecode(is).getMap();
			if (map.containsKey("info")) {
				BEValue info = map.get("info");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				BEncoder.bencode(info, baos);
				return DigestUtils.shaHex(baos.toByteArray());
			}
		} catch (IOException ex) {
			//fuck off java
		}
		throw new MessageException("Unable to obtain torrent info_hash! Is this a valid torrent file or magnet link?");
	}

	@Override
	public void restart() {
		stop();
		start();
	}

	@Override
	public List<ITorrent> listRecentlyActiveTorrents() {
		return listTorrents();
	}
}
