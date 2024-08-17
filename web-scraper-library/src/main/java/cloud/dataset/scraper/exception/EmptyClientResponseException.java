package cloud.dataset.scraper.exception;

public class EmptyClientResponseException extends Exception {
    public EmptyClientResponseException(String uri) {
        super("Empty client response from: " + uri);
    }

    public EmptyClientResponseException(String uri, String description) {
        super("Empty client response from: " + uri + " (" + description + ")");
    }
}
