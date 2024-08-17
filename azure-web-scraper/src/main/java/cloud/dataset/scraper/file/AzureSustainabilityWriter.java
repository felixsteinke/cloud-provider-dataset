package cloud.dataset.scraper.file;

import cloud.dataset.scraper.AzureSustainabilityData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public abstract class AzureSustainabilityWriter {
    /**
     * @param dataCenters list with objects to write as csv
     * @param filePath    like "../dataset/dataset.csv"
     * @throws IOException if target filePath is invalid
     */
    public static void writeCsvFile(Collection<AzureSustainabilityData> dataCenters, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // header
            writer.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
                    "Region",
                    "PUE",
                    "CFE",
                    "Date",
                    "CloudRegion",
                    "Year",
                    "SourceUrl",
                    "SourceDescription"));
            // content
            for (AzureSustainabilityData data : dataCenters) {
                writer.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
                        cleanValue(data.getRegionText()),
                        cleanValue(data.getPueText()),
                        cleanValue(data.getCfeText()),
                        cleanValue(data.getDateText()),
                        cleanValue(data.getCloudRegion()),
                        cleanValue(data.getYear().toString()),
                        cleanValue(data.getSourceUrl()),
                        data.getSourceDescription()));
            }
        }
    }

    private static String cleanValue(String value) {
        if (value == null) {
            return "";
        } else {
            return value.replaceAll(",", ".");
        }
    }
}
