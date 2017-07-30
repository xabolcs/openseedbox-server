package controllers;

import com.openseedbox.Config;
import com.openseedbox.backend.ITorrentBackend;
import com.openseedbox.backend.NodeStatus;
import com.openseedbox.code.Util;
import java.io.File;
import org.apache.commons.lang.StringUtils;

/**
 * Returns server statistics
 * @author Erin Drummond
 */
public class Status extends Base {
	
	public static void index() throws Exception {
		checkDownloadLocations();
		String uptime = Util.executeCommand("uptime").trim();
		String baseDevice = Config.getBackendBaseDevice();
		if (StringUtils.isEmpty(baseDevice)) {
			baseDevice = "/dev/";
		}
		String free_space = Util.executeCommand(String.format("df -B 1 | grep %s | awk '{print $4}' | head -1", baseDevice));
		if (StringUtils.isEmpty(free_space)) {
			free_space = "-1";
		}		
		String total_space = Util.executeCommand(String.format("df -B 1 | grep %s | awk '{print $2}' | head -1", baseDevice));
		if (StringUtils.isEmpty(total_space)) {
			total_space = "-1";
		}

		ITorrentBackend backend = getBackend();
		
		result(new NodeStatus(uptime, Long.parseLong(free_space), Long.parseLong(total_space), Config.getBackendBasePath(), backendBasePathWritable(), backend));
	}
	
}
