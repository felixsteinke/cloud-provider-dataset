package cloud.dataset.scraper.utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public abstract class ResourceFileReader {
    /**
     * Note: Don't forget to close the reader.
     *
     * @param resourcePath path within the resource directory (example: directory/file.txt)
     * @return reader of input stream at resource path
     * @throws IOException if input stream is null
     */
    protected static BufferedReader getResourceReader(String resourcePath) throws IOException {
        InputStream inputStream = ResourceFileReader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null)
            throw new IOException("No content at " + resourcePath);

        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    /**
     * @param resourcePath path within the resource directory (example: directory/file.txt)
     * @return file at resource path
     * @throws IOException if resource does not exist
     */
    public static File getFile(String resourcePath) throws IOException {
        URL resourceUrl = ResourceFileReader.class.getClassLoader().getResource(resourcePath);
        if (resourceUrl == null)
            throw new IOException("No content at " + resourcePath);

        return new File(resourceUrl.getFile());
    }
}
