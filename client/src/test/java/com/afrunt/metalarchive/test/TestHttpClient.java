package com.afrunt.metalarchive.test;

import com.afrunt.metalarchive.impl.util.HttpClient;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrii Frunt
 */
public class TestHttpClient extends HttpClient {

    private static final Map<String, String> URLS = new HashMap<>();

    public TestHttpClient() {
        synchronized (TestHttpClient.class) {
            try {
                InputStream urlsResource = getClass().getClassLoader().getResourceAsStream("data/urls.json");
                if (URLS.isEmpty() && urlsResource != null) {
                    JsonObject urls = Json.parse(new InputStreamReader(urlsResource)).asObject();
                    urls.forEach(member -> URLS.put(member.getName(), member.getValue().asString()));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public byte[] download(String urlString) {
        String resourceFilePath = URLS.get(urlString);
        if (resourceFilePath != null) {
            try {
                return readAllBytes(getClass().getClassLoader().getResourceAsStream(resourceFilePath));
            } catch (Exception e) {
                System.out.println("Error downloading from URL: " + urlString);
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Resource not cached. downloading from URL: " + urlString);
            return super.download(urlString);
        }
    }
}
