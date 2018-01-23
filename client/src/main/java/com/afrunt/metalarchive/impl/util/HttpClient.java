package com.afrunt.metalarchive.impl.util;

import com.afrunt.metalarchive.api.MetalArchiveClientException;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Andrii Frunt
 */
public class HttpClient {
    private static final Logger LOGGER = Logger.getLogger(HttpClient.class.getName());
    public static final String HTTP_CLIENT_TYPE = "com.afrunt.metalarchive.HTTP_CLIENT_TYPE";

    private int numberOfRetries = 5;

    public static HttpClient getInstance() {
        String httpClientType = System.getProperty(HTTP_CLIENT_TYPE);
        if (httpClientType != null) {
            try {
                return (HttpClient) Class.forName(httpClientType).newInstance();
            } catch (Exception e) {
                throw new MetalArchiveClientException(e);
            }
        } else {
            return new HttpClient();
        }
    }

    public JsonObject downloadAsJsonObject(String url) {
        String jsonString = downloadAsString(url);
        return (JsonObject) Json.parse(jsonString);
    }

    public String downloadAsString(String url) {
        return new String(download(url), Charset.forName("UTF-8"));
    }

    public String downloadAsString(String url, Charset charset) {
        return new String(download(url), charset);
    }

    public TagNode downloadAsTagNode(String url) {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        String htmlContent = downloadAsString(url);
        return htmlCleaner.clean(htmlContent);
    }


    public byte[] download(String urlString) {
        int failCount = 0;
        while (failCount < numberOfRetries) {
            try {
                URL url = new URL(urlString);
                InputStream is = url.openStream();
                return readAllBytes(is);
            } catch (Exception e) {
                ++failCount;
                LOGGER.log(Level.WARNING, "Error during downloading of data from {0}. Current fail count is {1}", new Object[]{urlString, failCount});
            }
        }

        throw new MetalArchiveClientException("Error during downloading of data from " + urlString);
    }

    protected byte[] readAllBytes(InputStream is) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new MetalArchiveClientException("Error reading data", e);
        }
    }

    protected HttpClient() {

    }
}
