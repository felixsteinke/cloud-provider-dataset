package cloud.dataset.scraper;

import cloud.dataset.scraper.factory.LoggerFactory;
import cloud.dataset.scraper.file.AzureMachineWriter;
import cloud.dataset.scraper.file.AzureRegionWriter;
import cloud.dataset.scraper.file.AzureSustainabilityWriter;
import cloud.dataset.scraper.parser.AzureMachineParser;
import cloud.dataset.scraper.parser.AzureRegionParser;
import cloud.dataset.scraper.parser.AzureSustainabilityParser;
import cloud.dataset.scraper.utils.FileUtils;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public class AzureScraperApp {
    private static final Logger LOGGER = LoggerFactory.getLogger();

    public static void main(String[] args) throws Exception {
        runMachineScraper();
        runRegionScraper();
        runSustainabilityScraper();
    }

    private static void runMachineScraper() throws Exception {
        LOGGER.info("Starting Azure-Machine Scraper.");
        String outputDir = FileUtils.getOutputDirectoryPath("dataset");
        String outputFile = "azure-machines.csv";

        List<AzureMachineData> machines = AzureMachineParser.readMachineData();
        LOGGER.info("Extracted " + machines.size() + " Azure-Machines.");

        AzureMachineWriter.writeCsvFile(machines, outputDir + outputFile);
        LOGGER.info("Finished Azure-Machine Scraper. Output at: " + outputDir + outputFile);
    }

    private static void runRegionScraper() throws Exception {
        LOGGER.info("Starting Azure-Region Scraper.");
        LOGGER.warning("Keep in mind the data source is a local copy of the original website.");
        String outputDir = FileUtils.getOutputDirectoryPath("dataset");
        String outputFile = "azure-regions.csv";

        List<AzureRegionData> regions = AzureRegionParser.extractRegionData();
        LOGGER.info("Extracted " + regions.size() + " Azure-Regions.");

        AzureRegionWriter.writeCsvFile(regions, outputDir + outputFile);
        LOGGER.info("Finished Azure-Region Scraper. Output at: " + outputDir + outputFile);
    }

    private static void runSustainabilityScraper() throws Exception {
        LOGGER.info("Starting Azure-Sustainability Scraper.");
        LOGGER.warning("Keep in mind the data source is a local copy of the original website.");
        String outputDir = FileUtils.getOutputDirectoryPath("dataset");
        String outputFile = "azure-sustainability.csv";

        Collection<AzureSustainabilityData> machines = AzureSustainabilityParser.extractSustainabilityData().values();
        LOGGER.info("Extracted " + machines.size() + " Azure-Sustainability-Entries.");

        AzureSustainabilityWriter.writeCsvFile(machines, outputDir + outputFile);
        LOGGER.info("Finished Azure-Sustainability Scraper. Output at: " + outputDir + outputFile);
    }
}
