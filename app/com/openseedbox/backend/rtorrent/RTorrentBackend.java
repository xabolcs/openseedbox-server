package com.openseedbox.backend.rtorrent;

import com.openseedbox.backend.*;

import java.io.File;
import java.util.List;
import java.util.Map;

public class RTorrentBackend implements ITorrentBackend {
    @Override
    public boolean isInstalled() {
        return false;
    }

    @Override
    public String getName() {
        return "rTorrent";
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public ITorrent addTorrent(File file) {
        return null;
    }

    @Override
    public ITorrent addTorrent(String urlOrMagnet) {
        return null;
    }

    @Override
    public void removeTorrent(String hash) {

    }

    @Override
    public void removeTorrent(List<String> hashes) {

    }

    @Override
    public void startTorrent(String hash) {

    }

    @Override
    public void startTorrent(List<String> hashes) {

    }

    @Override
    public void stopTorrent(String hash) {

    }

    @Override
    public void stopTorrent(List<String> hashes) {

    }

    @Override
    public ITorrent getTorrentStatus(String hash) {
        return null;
    }

    @Override
    public List<ITorrent> getTorrentStatus(List<String> hashes) {
        return null;
    }

    @Override
    public List<IPeer> getTorrentPeers(String hash) {
        return null;
    }

    @Override
    public Map<String, List<IPeer>> getTorrentPeers(List<String> hashes) {
        return null;
    }

    @Override
    public List<ITracker> getTorrentTrackers(String hash) {
        return null;
    }

    @Override
    public Map<String, List<ITracker>> getTorrentTrackers(List<String> hashes) {
        return null;
    }

    @Override
    public List<IFile> getTorrentFiles(String hash) {
        return null;
    }

    @Override
    public Map<String, List<IFile>> getTorrentFiles(List<String> hashes) {
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
        return null;
    }

    @Override
    public List<ITorrent> listRecentlyActiveTorrents() {
        return null;
    }

    @Override
    public ISessionStatistics getSessionStatistics() {
        return null;
    }
}
