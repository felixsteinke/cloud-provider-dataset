package cloud.dataset.scraper.exception;

import cloud.dataset.scraper.utils.FileUtils;

/**
 * Exception with appropriate message.
 */
public class DirectoryNotFoundException extends Exception {
    public DirectoryNotFoundException(String directoryName) {
        super("Directory (" + directoryName + ") not found in execution directory: " + FileUtils.getExecutionDirectory());
    }
}
