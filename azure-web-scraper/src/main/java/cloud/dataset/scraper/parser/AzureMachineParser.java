package cloud.dataset.scraper.parser;

import cloud.dataset.scraper.AzureMachineData;
import cloud.dataset.scraper.exception.EmptyClientResponseException;
import cloud.dataset.scraper.utils.ResourceFileReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public abstract class AzureMachineParser extends ResourceFileReader {
    private static final String SOURCE_URL = "https://learn.microsoft.com/en-us/azure/virtual-machines/linux/compute-benchmark-scores";

    public static List<AzureMachineData> readMachineData() throws IOException, EmptyClientResponseException {
        List<AzureMachineData> data = new ArrayList<>();
        Document page = Jsoup.connect(SOURCE_URL).get();
        // retrieve html table with validation
        Element contentDiv = page.selectFirst("#main > div.content");
        if (contentDiv == null) {
            throw new EmptyClientResponseException(SOURCE_URL, "#main > div.content");
        }
        Elements tables = contentDiv.select("table");
        Elements headings = page.select("h3");
        if (tables.isEmpty()) {
            throw new EmptyClientResponseException(SOURCE_URL, "table");
        }
        Map<String, List<String>> workloadMap = parseWorkloadTypes(tables.get(0));
        for (int i = 1; i < tables.size(); i++) {
            Element tbody = tables.get(i).selectFirst("tbody");
            if (tbody == null) {
                continue;
            }
            for (Element tr : tbody.select("tr")) {
                AzureMachineData dataObj = parseRow(tr.select("td"));
                dataObj.setFamily(headings.get(i - 1).text());
                dataObj.setWorkloadTypes(findWorkloadType(workloadMap, dataObj.getFamily()));
                data.add(dataObj);
            }
        }
        return data;
    }

    private static Map<String, List<String>> parseWorkloadTypes(Element table) {
        Map<String, List<String>> workloadMap = new HashMap<>();
        Element tbody = table.selectFirst("tbody");
        if (tbody == null) {
            return workloadMap;
        }
        for (Element tr : tbody.select("tr")) {
            Elements tds = tr.select("td");
            String workloadType = tds.get(0).text();
            List<String> machineFamilies = tds.get(1).select("a").stream()
                    .map(e -> {
                        if (e == null) {
                            return null;
                        }
                        return e.text();
                    }).filter(Objects::nonNull)
                    .toList();
            workloadMap.put(workloadType, machineFamilies);
        }
        return workloadMap;
    }


    private static AzureMachineData parseRow(Elements tds) {
        AzureMachineData data = new AzureMachineData();
        data.setMachine(tds.get(0).text());
        data.setCpu(tds.get(1).text());
        data.setVCpus(Integer.parseInt(tds.get(2).text()));
        data.setNodes(Integer.parseInt(tds.get(3).text()));
        data.setMemoryGB(Float.parseFloat(tds.get(4).text().replaceAll(",", "")));
        data.setBenchmarkScore(Float.parseFloat(tds.get(5).text().replaceAll(",", "")));
        data.setSourceUrl(SOURCE_URL);
        return data;
    }

    private static String findWorkloadType(Map<String, List<String>> workloadMap, String family) {
        for (var key : workloadMap.keySet()) {
            if (workloadMap.get(key).contains(family.split(" ")[0])) {
                return key;
            }
        }
        return "Undefined";
    }
}
