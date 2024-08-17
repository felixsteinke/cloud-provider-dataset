package cloud.dataset.scraper.parser;

import cloud.dataset.scraper.AzureRegionData;
import cloud.dataset.scraper.exception.EmptyClientResponseException;
import cloud.dataset.scraper.utils.ResourceFileReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AzureRegionParser {
    private static final String RESOURCE_FILE_PATH = "data/azure-data-regions.html";
    private static final String SOURCE_URL = "https://www.azurespeed.com/Information/AzureRegions";

    public static List<AzureRegionData> extractRegionData() throws EmptyClientResponseException, IOException {
        // Document page = Jsoup.connect(SOURCE_URL).get();
        // Page is not a static website and needs to be parsed from a local copy.
        Document page = Jsoup.parse(ResourceFileReader.getFile(RESOURCE_FILE_PATH));
        Element tableBody = page.selectFirst("tbody");
        if (tableBody == null) {
            throw new EmptyClientResponseException(SOURCE_URL, "tableBody is null");
        }

        Elements tableRows = tableBody.select("tr");
        if (tableRows.isEmpty()) {
            throw new EmptyClientResponseException(SOURCE_URL, "tableRows are empty");
        }

        List<AzureRegionData> dataList = new ArrayList<>();
        for (Element row : tableRows) {
            AzureRegionData data = new AzureRegionData();
            data.setGeography(row.select("td").get(0).text());
            data.setRegionName(row.select("td").get(1).text());
            data.setPhysicalLocation(row.select("td").get(2).text());
            data.setSourceUrl(SOURCE_URL);
            dataList.add(data);
        }
        return dataList;
    }
}
