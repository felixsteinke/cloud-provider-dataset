package cloud.dataset.scraper.parser;

import cloud.dataset.scraper.GcpMachineData;
import cloud.dataset.scraper.exception.EmptyClientResponseException;
import cloud.dataset.scraper.utils.ResourceFileReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class GcpMachineParser {
    private static final String RESOURCE_FILE_PATH = "data/gcp-data-machines.html";
    private static final String SOURCE_URL = "https://cloud.google.com/compute/docs/machine-resource?hl=en";

    public static Collection<GcpMachineData> extractMachineData() throws EmptyClientResponseException, IOException {
        // Document page = Jsoup.connect(SOURCE_URL).get();
        // Page is not a static website and needs to be parsed from a local copy.
        Document page = Jsoup.parse(ResourceFileReader.getFile(RESOURCE_FILE_PATH));

        String rowHeadWorkload = normalizeString("Workload Types");
        String rowHeadCpuTypes = normalizeString("CPU Types");
        String rowHeadArchitecture = normalizeString("Architecture");
        String rowHeadVCpuRange = normalizeString("vCPUs");
        String rowHeadVCpuDefinition = normalizeString("vCPU Definition");
        String rowHeadMemoryRange = normalizeString("Memory");
        String rowHeadSsdMax = normalizeString("Max Local SSD");
        String rowHeadNetworkRange = normalizeString("Network Performance");
        String rowHeadVCpuCoremark = normalizeString("Coremark");

        // retrieve html table with validation
        Element table = page.selectFirst("table");
        if (table == null) {
            throw new EmptyClientResponseException(SOURCE_URL, "table not accessible");
        }
        // retrieve html head and body with validation
        Element thead = table.selectFirst("thead");
        Element tbody = table.selectFirst("tbody");
        if (thead == null || tbody == null) {
            throw new EmptyClientResponseException(SOURCE_URL, "thead or tbody is null");
        }
        // retrieve all expected table rows with validation
        Element theadTr = thead.selectFirst("tr");
        Elements tbodyTrList = tbody.select("tr");
        if (theadTr == null || tbodyTrList.isEmpty()) {
            throw new EmptyClientResponseException(SOURCE_URL, "theadTr or tbodyTrList is null");
        }

        // create objects based on column header
        Map<Integer, GcpMachineData> columnMap = new HashMap<>();
        Elements theadTrThList = theadTr.select("th:not(:first-child)");
        for (int i = 0; i < theadTrThList.size(); i++) {
            String machineName = theadTrThList.get(i).text();
            if (machineName.isEmpty()) {
                continue;
            }
            columnMap.put(i, new GcpMachineData(machineName, SOURCE_URL));
        }
        // iterate through each row and handle based on leading header
        for (Element row : tbodyTrList) {
            // retrieve row content with validation
            Element rowTh = row.selectFirst("th");
            Elements rowTdList = row.select("td");
            if (rowTh == null || rowTdList.isEmpty()) {
                continue;
            }
            // handle row content
            String rowTitle = normalizeString(rowTh.text());
            if (rowTitle.contains(rowHeadWorkload)) {
                setValues(columnMap, rowTdList, GcpMachineData::setWorkloadTypes);
            } else if (rowTitle.contains(rowHeadCpuTypes)) {
                setValues(columnMap, rowTdList, GcpMachineData::setCpuTypeText);
            } else if (rowTitle.contains(rowHeadArchitecture)) {
                setValues(columnMap, rowTdList, GcpMachineData::setArchitecture);
            } else if (rowTitle.contains(rowHeadVCpuRange)) {
                setValues(columnMap, rowTdList, GcpMachineData::setVCpuRange);
            } else if (rowTitle.contains(rowHeadVCpuDefinition)) {
                setValues(columnMap, rowTdList, GcpMachineData::setVCpuDefinition);
            } else if (rowTitle.equals(rowHeadMemoryRange)) {
                setValues(columnMap, rowTdList, GcpMachineData::setMemoryRange);
            } else if (rowTitle.contains(rowHeadSsdMax)) {
                setValues(columnMap, rowTdList, GcpMachineData::setMaxLocalSsd);
            } else if (rowTitle.contains(rowHeadNetworkRange)) {
                setValues(columnMap, rowTdList, GcpMachineData::setNetworkRange);
            } else if (rowTitle.contains(rowHeadVCpuCoremark)) {
                setValues(columnMap, rowTdList, GcpMachineData::setVCpuCoremarkScore);
            }
        }
        return columnMap.values();
    }

    private static String normalizeString(String s) {
        return s.toLowerCase().replaceAll(" ", "");
    }

    private static void setValues(Map<Integer, GcpMachineData> columnMap, Elements rowTdList, BiConsumer<GcpMachineData, String> setter) {
        for (int i = 0; i < rowTdList.size(); i++) {
            setter.accept(columnMap.get(i), rowTdList.get(i).text());
        }
    }
}
