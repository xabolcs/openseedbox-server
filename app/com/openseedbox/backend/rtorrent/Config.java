package com.openseedbox.backend.rtorrent;

import play.Play;

class Config extends com.openseedbox.Config {
	public static String getRpcPort() {
		return Play.configuration.getProperty("backend.rtorrent.port", "5001");
	}}
