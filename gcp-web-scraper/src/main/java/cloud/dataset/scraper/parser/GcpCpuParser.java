package cloud.dataset.scraper.parser;

import cloud.dataset.scraper.GcpCpuData;
import cloud.dataset.scraper.exception.EmptyClientResponseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class GcpCpuParser {
    private static final String SOURCE_URL = "https://cloud.google.com/compute/docs/cpu-platforms?hl=en";

    public static List<GcpCpuData> extractCpuData() throws EmptyClientResponseException, IOException {
        Document page = Jsoup.connect(SOURCE_URL).get();

        Elements tableList = page.select("table");
        if (tableList.isEmpty()) {
            throw new EmptyClientResponseException(SOURCE_URL, "no tables found");
        }

        List<GcpCpuData> dataList = new ArrayList<>();
        for (Element table : tableList) {
            // retrieve table body with validation
            Element tbody = table.selectFirst("tbody");
            if (tbody == null) {
                throw new EmptyClientResponseException(SOURCE_URL, "tbody is null");
            }
            // retrieve table rows with validation
            Elements trList = tbody.select("tr");
            if (trList.isEmpty()) {
                throw new EmptyClientResponseException(SOURCE_URL, "tbodyTrList is empty");
            }
            // create objects for each row
            String cpuType = null;
            int cpuTypeRowspan = 0;
            String skuCpu = null;
            int skuCpuRowspan = 0;

            for (Element row : trList) {
                // handle row content header
                GcpCpuData data = new GcpCpuData();
                Element thCpuType = row.selectFirst("th");
                if (thCpuType != null) {
                    cpuType = thCpuType.text();
                    cpuTypeRowspan = getRowspan(thCpuType);
                }
                data.setCpuType(cpuType);
                cpuTypeRowspan--;
                if (cpuTypeRowspan <= 0) {
                    cpuType = null;
                }
                // handle row divs
                int tdPointer = 0;
                Elements rowTdList = row.select("td");
                if (rowTdList.isEmpty()) {
                    continue;
                }
                if (skuCpu == null) {
                    Element tdSkuCpu = rowTdList.get(tdPointer++);
                    skuCpu = tdSkuCpu.text();
                    skuCpuRowspan = getRowspan(tdSkuCpu);
                }
                data.setSkuCpu(skuCpu);
                skuCpuRowspan--;
                if (skuCpuRowspan <= 0) {
                    skuCpu = null;
                }
                data.setMachines(getMachines(rowTdList.get(tdPointer)));
                data.setSourceUrl(SOURCE_URL);
                dataList.add(data);
            }
        }
        return dataList;
    }

    private static String getMachines(Element machineTd) {
        Elements aElements = machineTd.select("a");
        StringBuilder sb = new StringBuilder();
        for (Element machineLink : aElements) {
            sb.append(machineLink.text());
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private static int getRowspan(Element element) {
        String rowspan = element.attr("rowspan");
        try {
            return rowspan.isEmpty() ? 1 : Integer.parseInt(rowspan);
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
