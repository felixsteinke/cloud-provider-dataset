package cloud.dataset.scraper.file;

import cloud.dataset.scraper.GcpCpuData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public abstract class GcpCpuWriter {
    /**
     * Writes a collection of GcpCpuData objects to a CSV file.
     *
     * @param data     list of GcpCpuData objects to write as CSV
     * @param filePath the file path where the CSV should be written, e.g., "../dataset/gcp_cpu_data.csv"
     * @throws IOException if the target filePath is invalid or there is an I/O error
     */
    public static void writeCsvFile(Collection<GcpCpuData> data, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write the CSV header
            writer.append(String.format("%s,%s,%s,%s\n",
                    "CpuType",
                    "SkuCpu",
                    "Machines",
                    "SourceUrl"));

            // Write the CSV content
            for (GcpCpuData entry : data) {
                writer.append(String.format("%s,%s,%s,%s\n",
                        cleanValue(entry.getCpuType()),
                        cleanValue(entry.getSkuCpu()),
                        cleanValue(entry.getMachines()),
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
