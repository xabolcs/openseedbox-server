package controllers.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import controllers.BaseController;
import play.Logger;

public class TransmissionRPC extends BaseController {
    public static void index(String json) throws Exception {
        Logger.info("json: %s", json);
        // transmission-daemon 2.94 (d8e60ee44f):
        // {"hashString":"65162b3234bbafbc71beb372562f59148a1526be","trackerStats":[{"announce":"http://bttracker.debian.org:6969/announce","announceState":1,"downloadCount":141,"hasAnnounced":true,"hasScraped":true,"host":"http://bttracker.debian.org:6969","id":0,"isBackup":false,"lastAnnouncePeerCount":32,"lastAnnounceResult":"Success","lastAnnounceStartTime":1609761014,"lastAnnounceSucceeded":true,"lastAnnounceTime":1609761015,"lastAnnounceTimedOut":false,"lastScrapeResult":"","lastScrapeStartTime":1609761020,"lastScrapeSucceeded":true,"lastScrapeTime":1609761021,"lastScrapeTimedOut":0,"leecherCount":1,"nextAnnounceTime":1609761915,"nextScrapeTime":1609762830,"scrape":"http://bttracker.debian.org:6969/scrape","scrapeState":1,"seederCount":44,"tier":0}]}
        //
        // transmission-daemon 3.00 (bb6b5a062e):
        // {"hashString":"65162b3234bbafbc71beb372562f59148a1526be","trackerStats":[{"announce":"http://bttracker.debian.org:6969/announce","announceState":0,"downloadCount":-1,"hasAnnounced":false,"hasScraped":false,"host":"http://bttracker.debian.org:6969","id":0,"isBackup":false,"lastAnnouncePeerCount":0,"lastAnnounceResult":"","lastAnnounceStartTime":0,"lastAnnounceSucceeded":false,"lastAnnounceTime":0,"lastAnnounceTimedOut":false,"lastScrapeResult":"","lastScrapeStartTime":0,"lastScrapeSucceeded":false,"lastScrapeTime":0,"lastScrapeTimedOut":false,"leecherCount":-1,"nextAnnounceTime":0,"nextScrapeTime":1609762250,"scrape":"http://bttracker.debian.org:6969/scrape","scrapeState":1,"seederCount":-1,"tier":0}]}

        result(new Gson().fromJson("{\"torrents\":[" +
                "{\"hashString\":\"65162b3234bbafbc71beb372562f59148a1526be\",\"trackerStats\":[" +
                "{\"announce\":\"http://transmission-daemon.2.94.d8e60ee44f.bttracker.debian.org:6969/announce\",\"announceState\":1,\"downloadCount\":141,\"hasAnnounced\":true,\"hasScraped\":true,\"host\":\"http://bttracker.debian.org:6969\",\"id\":0,\"isBackup\":false,\"lastAnnouncePeerCount\":32,\"lastAnnounceResult\":\"Success\",\"lastAnnounceStartTime\":1609761014,\"lastAnnounceSucceeded\":true,\"lastAnnounceTime\":1609761015,\"lastAnnounceTimedOut\":false,\"lastScrapeResult\":\"\",\"lastScrapeStartTime\":1609761020,\"lastScrapeSucceeded\":true,\"lastScrapeTime\":1609761021,\"lastScrapeTimedOut\":0,\"leecherCount\":1,\"nextAnnounceTime\":1609761915,\"nextScrapeTime\":1609762830,\"scrape\":\"http://bttracker.debian.org:6969/scrape\",\"scrapeState\":1,\"seederCount\":44,\"tier\":0}" +
                ",{\"announce\":\"http://transmission-daemon.3.00.bb6b5a062e.bttracker.debian.org:6969/announce\",\"announceState\":0,\"downloadCount\":-1,\"hasAnnounced\":false,\"hasScraped\":false,\"host\":\"http://bttracker.debian.org:6969\",\"id\":0,\"isBackup\":false,\"lastAnnouncePeerCount\":0,\"lastAnnounceResult\":\"\",\"lastAnnounceStartTime\":0,\"lastAnnounceSucceeded\":false,\"lastAnnounceTime\":0,\"lastAnnounceTimedOut\":false,\"lastScrapeResult\":\"\",\"lastScrapeStartTime\":0,\"lastScrapeSucceeded\":false,\"lastScrapeTime\":0,\"lastScrapeTimedOut\":false,\"leecherCount\":-1,\"nextAnnounceTime\":0,\"nextScrapeTime\":1609762250,\"scrape\":\"http://bttracker.debian.org:6969/scrape\",\"scrapeState\":1,\"seederCount\":-1,\"tier\":0}" +
                "]}" +
                "]}", JsonObject.class)
        );
    }
}
