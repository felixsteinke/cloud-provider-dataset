package cloud.dataset.scraper.file;

import cloud.dataset.scraper.AzureRegionData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public abstract class AzureRegionWriter {
    /**
     * @param regions  list with objects to write as csv
     * @param filePath like "../dataset/dataset.csv"
     * @throws IOException if target filePath is invalid
     */
    public static void writeCsvFile(Collection<AzureRegionData> regions, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // header
            writer.append(String.format("%s,%s,%s,%s\n",
                    "Geography",
                    "RegionName",
                    "PhysicalLocation",
                    "SourceUrl"));
            // content
            for (AzureRegionData region : regions) {
                writer.append(String.format("%s,%s,%s,%s\n",
                        cleanValue(region.getGeography()),
                        cleanValue(region.getRegionName()),
                        cleanValue(region.getPhysicalLocation()),
                        region.getSourceUrl()));
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
