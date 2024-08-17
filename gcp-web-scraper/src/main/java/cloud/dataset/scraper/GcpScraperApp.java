package cloud.dataset.scraper;

import cloud.dataset.scraper.factory.LoggerFactory;
import cloud.dataset.scraper.file.GcpCpuWriter;
import cloud.dataset.scraper.file.GcpMachineWriter;
import cloud.dataset.scraper.file.GcpRegionHardwareWriter;
import cloud.dataset.scraper.parser.GcpCpuParser;
import cloud.dataset.scraper.parser.GcpMachineParser;
import cloud.dataset.scraper.parser.GcpRegionHardwareParser;
import cloud.dataset.scraper.utils.FileUtils;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class GcpScraperApp {
    private static final Logger LOGGER = LoggerFactory.getLogger();

    public static void main(String[] args) throws Exception {
        runCpuScraper();
        runMachineScraper();
        runRegionHardwareScraper();
    }

    private static void runCpuScraper() throws Exception {
        LOGGER.info("Starting GCP-CPU Scraper.");
        String outputDir = FileUtils.getOutputDirectoryPath("dataset");
        String outputFile = "gcp-cpus.csv";

        List<GcpCpuData> regions = GcpCpuParser.extractCpuData();
        LOGGER.info("Extracted " + regions.size() + " GCP-CPUs.");

        GcpCpuWriter.writeCsvFile(regions, outputDir + outputFile);
        LOGGER.info("Finished GCP-CPU Scraper. Output at: " + outputDir + outputFile);
    }

    private static void runMachineScraper() throws Exception {
        LOGGER.info("Starting GCP-Machine Scraper.");
        LOGGER.warning("Keep in mind the data source is a local copy of the original website.");
        String outputDir = FileUtils.getOutputDirectoryPath("dataset");
        String outputFile = "gcp-machines.csv";

        Collection<GcpMachineData> machines = GcpMachineParser.extractMachineData();
        LOGGER.info("Extracted " + machines.size() + " GCP-Machines.");

        GcpMachineWriter.writeCsvFile(machines, outputDir + outputFile);
        LOGGER.info("Finished GCP-Machine Scraper. Output at: " + outputDir + outputFile);
    }

    private static void runRegionHardwareScraper() throws Exception {
        LOGGER.info("Starting GCP-Hardware Scraper.");
        String outputDir = FileUtils.getOutputDirectoryPath("dataset");
        String outputFile = "gcp-region-hardware.csv";

        List<GcpRegionHardwareData> machines = GcpRegionHardwareParser.extractHardwareData();
        LOGGER.info("Extracted " + machines.size() + " GCP-Region-Hardware-Entries.");

        GcpRegionHardwareWriter.writeCsvFile(machines, outputDir + outputFile);
        LOGGER.info("Finished GCP-Hardware Scraper. Output at: " + outputDir + outputFile);
    }
}
