package com.afrunt.metalarchive.test.generator;

import com.afrunt.metalarchive.impl.util.HttpClient;
import com.afrunt.metalarchive.test.TestHttpClient;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andrii Frunt
 */
public class DataGeneratorHttpClient extends HttpClient {

    private static final Map<String, String> URLS = new ConcurrentHashMap<>();

    public DataGeneratorHttpClient() {
        synchronized (TestHttpClient.class) {
            try {
                Path urlsPath = TestDataGenerator.RESOURCES_PATH.resolve("urls.json");
                if (Files.exists(urlsPath)) {

                    InputStream urlsResource = new FileInputStream(urlsPath.toFile());

                    if (URLS.isEmpty()) {
                        JsonObject urls = Json.parse(new InputStreamReader(urlsResource)).asObject();
                        urls.forEach(member -> URLS.put(member.getName(), member.getValue().asString()));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public byte[] download(String url) {

        if (URLS.containsKey(url)) {
            try {
                byte[] bytes = readAllBytes(new FileInputStream(TestDataGenerator.RESOURCES_PATH.getParent().resolve(URLS.get(url)).toFile()));
                System.out.println("Already in cache url = [" + url + "]");
                return bytes;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        byte[] content = super.download(url);


        String fileName = null;
        String resourcePath = null;

        if (url.startsWith("https://www.metal-archives.com/bands/")) {
            String id = url.replace("https://www.metal-archives.com/bands/", "").split("/")[1];
            fileName = id + ".html";
            resourcePath = "bands";
        } else if (url.startsWith("https://www.metal-archives.com/release/ajax-view-lyrics/id/")) {
            String id = url.replace("https://www.metal-archives.com/release/ajax-view-lyrics/id/", "");
            fileName = id + ".html";
            resourcePath = "lyrics";
        } else if (url.startsWith("https://www.metal-archives.com/band/discography/id/")) {
            String id = url.replace("https://www.metal-archives.com/band/discography/id/", "").split("/")[0];
            fileName = id + ".html";
            resourcePath = "discography";
        } else if (url.startsWith("https://www.metal-archives.com/albums/")) {
            String id = url.substring(url.lastIndexOf("/"));
            fileName = id + ".html";
            resourcePath = "albums";
        } else if (url.startsWith("https://www.metal-archives.com/band/read-more/id/")) {
            String id = url.substring(url.lastIndexOf("/"));
            fileName = id + ".html";
            resourcePath = "read-more";
        }

        if (fileName != null) {
            String filePath = resourcePath + "/" + fileName;
            System.out.println(String.format("URL %s cached to %s", url, "data/" + filePath));
            TestDataGenerator.getInstance().addUrl(url, "data/" + filePath);
            TestDataGenerator.getInstance().saveFile(TestDataGenerator.RESOURCES_PATH.resolve(filePath), content);
        } else {
            System.out.println(String.format("URL not cached %s", url));
        }

        return content;
    }
}
