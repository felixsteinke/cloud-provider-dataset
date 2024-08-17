package cloud.dataset.scraper.file;

import cloud.dataset.scraper.GcpRegionHardwareData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class GcpRegionHardwareWriter {
    /**
     * Writes a collection of GcpRegionHardwareData objects to a CSV file.
     *
     * @param data     list of GcpRegionHardwareData objects to write as CSV
     * @param filePath the file path where the CSV should be written, e.g., "../dataset/gcp_region_hardware_data.csv"
     * @throws IOException if the target filePath is invalid or there is an I/O error
     */
    public static void writeCsvFile(Collection<GcpRegionHardwareData> data, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write the CSV header
            writer.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                    "CloudZone",
                    "CloudRegion",
                    "Location",
                    "MachineTypes",
                    "Cpus",
                    "Resource",
                    "SourceUrl"));

            // Write the CSV content
            for (GcpRegionHardwareData entry : data) {
                writer.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                        cleanValue(entry.getCloudZone()),
                        cleanValue(entry.getCloudRegion()),
                        cleanValue(entry.getLocation()),
                        cleanValue(entry.getMachineTypes()),
                        cleanValue(entry.getCpus()),
                        cleanValue(entry.getResource()),
                        entry.getSourceUrl()));
            }
        }
    }

    /**
     * Cleans a CSV value by replacing commas with periods.
     * If the value is null, returns an empty string.
     *
     * @param value the string value to clean
     * @return the cleaned value or an empty string if the input is null
     */
    private static String cleanValue(String value) {
        if (value == null) {
            return "";
        } else {
            return value.replaceAll(",", ".");
        }
    }
}
