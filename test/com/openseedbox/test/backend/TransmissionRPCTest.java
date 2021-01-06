package com.openseedbox.test.backend;

import com.openseedbox.backend.ITracker;
import com.openseedbox.backend.transmission.TransmissionBackend;
import org.junit.*;
import play.mvc.Router;
import play.test.UnitTest;

import java.util.List;

public class TransmissionRPCTest extends UnitTest {

	private TransmissionBackend backend = new TransmissionBackend();

	@BeforeClass
	public static void setUp() {
		Router.addRoute("*", "/transmission/rpc", "test.TransmissionRPC.index");
	}

	@AfterClass
	public static void tearDown() {
	}

	@Test
	public void testTransmission2TrackerStats() {
		List<ITracker> trackers = backend.getTorrentTrackers("transmission-daemon 2.94 (d8e60ee44f)");
		assertTrue("Response from Transmission 2 doesn't throw", trackers.get(0).getAnnounceUrl().contains("d8e60ee44f"));
	}
	@Test
	public void testTransmission3TrackerStats() {
		List<ITracker> trackers = backend.getTorrentTrackers("transmission-daemon 3.00 (bb6b5a062e)");
		assertTrue("Response from Transmission 3 doesn't throw", trackers.get(1).getAnnounceUrl().contains("bb6b5a062e"));
	}
}
