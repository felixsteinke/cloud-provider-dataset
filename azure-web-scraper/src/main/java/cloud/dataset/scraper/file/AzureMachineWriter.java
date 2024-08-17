package cloud.dataset.scraper.file;

import cloud.dataset.scraper.AzureMachineData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public abstract class AzureMachineWriter {
    /**
     * @param machines list with objects to write as csv
     * @param filePath like "../dataset/dataset.csv"
     * @throws IOException if target filePath is invalid
     */
    public static void writeCsvFile(Collection<AzureMachineData> machines, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // header
            writer.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    "Machine",
                    "Family",
                    "WorkloadTypes",
                    "CPU",
                    "vCPUs",
                    "Nodes",
                    "MemoryGB",
                    "BenchmarkScore",
                    "SourceUrl"));
            // content
            for (AzureMachineData machine : machines) {
                writer.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        cleanValue(machine.getMachine()),
                        cleanValue(machine.getFamily()),
                        cleanValue(machine.getWorkloadTypes()),
                        cleanValue(machine.getCpu()),
                        cleanValue(machine.getVCpus().toString()),
                        cleanValue(machine.getNodes().toString()),
                        cleanValue(machine.getMemoryGB().toString()),
                        cleanValue(machine.getBenchmarkScore().toString()),
                        machine.getSourceUrl()));
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
