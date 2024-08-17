package cloud.dataset.scraper.parser;

import cloud.dataset.scraper.GcpRegionHardwareData;
import cloud.dataset.scraper.exception.EmptyClientResponseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GcpRegionHardwareParser {
    private static final String SOURCE_URL = "https://cloud.google.com/compute/docs/regions-zones?hl=en";

    public static List<GcpRegionHardwareData> extractHardwareData() throws EmptyClientResponseException, IOException {
        Document html = Jsoup.connect(SOURCE_URL).get();

        // retrieve html body with validation
        Element tbody = html.selectFirst("tbody.list");
        if (tbody == null) {
            throw new EmptyClientResponseException(SOURCE_URL, "tbody is null");
        }
        // retrieve table rows with validation
        Elements tbodyTrList = tbody.select("tr");
        if (tbodyTrList.size() == 0) {
            throw new EmptyClientResponseException(SOURCE_URL, "tbodyTrList is null");
        }

        // create objects for each row
        List<GcpRegionHardwareData> dataList = new ArrayList<>();
        for (Element row : tbodyTrList) {
            // retrieve row content with validation
            Elements rowTdList = row.select("td");
            if (rowTdList.size() == 0) {
                continue;
            }
            // handle row content
            GcpRegionHardwareData data = new GcpRegionHardwareData();
            String zone = row.select("td").get(0).text();
            data.setCloudZone(zone);
            data.setCloudRegion(zone.substring(0, zone.length() - 2));
            data.setLocation(row.select("td").get(1).text());
            data.setMachineTypes(row.select("td").get(2).text());
            data.setCpus(row.select("td").get(3).text());
            data.setResource(row.select("td").get(4).text());
            data.setSourceUrl(SOURCE_URL);
            dataList.add(data);
        }
        return dataList;
    }
}
