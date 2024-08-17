package cloud.dataset.scraper.file;

import cloud.dataset.scraper.GcpMachineData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GcpMachineWriter {
    /**
     * Writes a collection of GcpMachineData objects to a CSV file.
     *
     * @param data     list of GcpMachineData objects to write as CSV
     * @param filePath the file path where the CSV should be written, e.g., "../dataset/gcp_machine_data.csv"
     * @throws IOException if the target filePath is invalid or there is an I/O error
     */
    public static void writeCsvFile(Collection<GcpMachineData> data, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write the CSV header
            writer.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    "Machine",
                    "WorkloadTypes",
                    "CpuTypeText",
                    "CpuTypes",
                    "Architecture",
                    "VCpuRange",
                    "VCpuDefinition",
                    "MemoryRange",
                    "MaxLocalSsd",
                    "NetworkRange",
                    "VCpuCoremarkScore",
                    "SourceUrl"));

            // Write the CSV content
            for (GcpMachineData entry : data) {
                writer.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        cleanValue(entry.getMachine()),
                        cleanValue(entry.getWorkloadTypes()),
                        cleanValue(entry.getCpuTypeText()),
                        cleanValue(joinList(entry.getCpuTypes())),
                        cleanValue(entry.getArchitecture()),
                        cleanValue(entry.getVCpuRange()),
                        cleanValue(entry.getVCpuDefinition()),
                        cleanValue(entry.getMemoryRange()),
                        cleanValue(entry.getMaxLocalSsd().toString()),
                        cleanValue(entry.getNetworkRange()),
                        cleanValue(entry.getVCpuCoremarkScore() != null ? entry.getVCpuCoremarkScore().toString() : ""),
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

    /**
     * Joins a list of strings into a single string, separated by semicolons.
     * If the list is null or empty, returns an empty string.
     *
     * @param list the list of strings to join
     * @return the joined string or an empty string if the input is null or empty
     */
    private static String joinList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        } else {
            return list.stream()
                    .map(GcpMachineWriter::cleanValue)
                    .collect(Collectors.joining(";"));
        }
    }
}
