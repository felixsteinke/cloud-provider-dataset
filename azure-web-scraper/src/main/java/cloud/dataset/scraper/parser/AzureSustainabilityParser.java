package cloud.dataset.scraper.parser;

import cloud.dataset.scraper.AzureSustainabilityData;
import cloud.dataset.scraper.exception.EmptyClientResponseException;
import cloud.dataset.scraper.utils.ResourceFileReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AzureSustainabilityParser {
    private static final String SOURCE_URL = "https://datacenters.microsoft.com/globe/fact-sheets/";
    private static final String HOST_URL = "https://datacenters.microsoft.com";
    private static final String RESOURCE_FILE_PATH = "data/azure-data-sustainability.html";


    /**
     * @return map with cloud region keys and sustainability data values
     * @throws IOException if data source is not accessible
     */
    public static Map<String, AzureSustainabilityData> extractSustainabilityData() throws EmptyClientResponseException, IOException {
        Map<String, AzureSustainabilityData> data = new HashMap<>();
        for (String link : extractFactSheetLinks()) {
            AzureSustainabilityData entry = extractFactSheetData(link);
            data.put(entry.getCloudRegion(), entry);
        }
        return data;
    }

    /**
     * @return extracted links to all fact sheets
     * @throws IOException                  if source url is not accessible
     * @throws EmptyClientResponseException if page could not be processed
     */
    private static List<String> extractFactSheetLinks() throws IOException, EmptyClientResponseException {
        //Document page = Jsoup.connect(SOURCE_URL).get();
        // Page is not a static website and needs to be parsed from a local copy.
        Document page = Jsoup.parse(ResourceFileReader.getFile(RESOURCE_FILE_PATH));

        Element factSheetList = page.selectFirst("div");
        if (factSheetList == null) {
            throw new EmptyClientResponseException(SOURCE_URL, "factSheetList is null");
        }

        Elements listItems = factSheetList.select("div");
        if (listItems.isEmpty()) {
            throw new EmptyClientResponseException(SOURCE_URL, "listItems are empty");
        }

        List<String> factSheetLinks = new ArrayList<>();
        for (Element item : listItems) {
            Elements links = item.select("a");
            if (listItems.isEmpty()) {
                continue;
            }
            for (Element a : links) {
                String href = a.attr("href");
                if (href.isBlank() || !href.contains(".pdf")) {
                    continue;
                }
                factSheetLinks.add(HOST_URL + href);
            }
        }
        return factSheetLinks;
    }

    /**
     * @param pdfUrl e.g.: https://datacenters.microsoft.com/globe/pdfs/sustainability/factsheets/Iowa%20(Central%20US).pdf
     * @return extract data of the fact sheet
     * @throws IOException if url could not be processed
     */
    protected static AzureSustainabilityData extractFactSheetData(String pdfUrl) throws IOException {
        var fileName = extractPdfName(pdfUrl);
        var encodedFileName = fileName.replaceAll(" ", "%20");
        InputStream pdfInputStream = new URL(pdfUrl.replace(fileName, encodedFileName)).openStream();
        String pdfText = extractPdfText(pdfInputStream);

        AzureSustainabilityData data = new AzureSustainabilityData();
        data.setSourceUrl(pdfUrl);
        data.setSourceDescription("Data from Azure Sustainability Fact Sheet.");

        data.setRegionText(fileName.replaceAll(".pdf", ""));
        data.setPueText(extractPUE(pdfText));
        data.setCfeText(extractCFE(pdfText));
        data.setDateText(extractDate(pdfText));
        data.setCloudRegion(extractCloudRegion(data.getRegionText()));
        data.setYear(extractYear(data.getDateText()));
        return data;
    }

    private static String extractPdfName(String pdfUrl) {
        var urlSplit = pdfUrl.split("/");
        return urlSplit[urlSplit.length - 1];
    }

    private static String extractPdfText(InputStream pdfInputStream) throws IOException {
        try (PDDocument document = PDDocument.load(pdfInputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private static String extractPUE(String pdfText) {
        Matcher matcher = Pattern.compile("(\\d+\\.\\d+)[\\s\\S]+[Design ]?[Pp]ower usage").matcher(pdfText);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private static String extractCFE(String pdfText) {
        Matcher matcher = Pattern.compile("(\\d+)%[\\s\\S]+Renewable energy coverage").matcher(pdfText);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private static String extractDate(String pdfText) {
        Matcher matcher = Pattern.compile("Published ([A-Za-z]+ (\\d)+)").matcher(pdfText);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private static String extractCloudRegion(String regionText) {
        Matcher cloudRegionMatcher = Pattern.compile("\\((.*?)\\)").matcher(regionText);
        if (cloudRegionMatcher.find()) {
            return cloudRegionMatcher.group(1);
        } else {
            return regionText;
        }
    }

    private static Integer extractYear(String dateText) {
        Matcher yearMatcher = Pattern.compile("(\\d+)").matcher(dateText);
        if (yearMatcher.find()) {
            return Integer.parseInt(yearMatcher.group(1));
        } else {
            return null;
        }
    }
}
