package com.afrunt.metalarchive.test.generator;

import com.afrunt.metalarchive.impl.MetalArchiveClientImpl;
import com.afrunt.metalarchive.impl.page.LettersPage;
import com.afrunt.metalarchive.impl.util.HttpClient;
import com.afrunt.metalarchive.impl.util.URLs;
import com.afrunt.metalarchive.model.BandInfo;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
public class TestDataGenerator {
    private static TestDataGenerator INSTANCE;
    public static final Path RESOURCES_PATH = Paths.get("client/src/test/resources/data");
    private JsonObject urls = new JsonObject();

    public static void main(String[] args) throws IOException {
        if (Files.exists(RESOURCES_PATH)) {
            delete(RESOURCES_PATH.toFile());
        }
        getInstance().generateData();
    }

    private JsonArray getBandIds() {
        InputStream bandIdsResource = getClass().getClassLoader().getResourceAsStream("bandIds.json");
        JsonArray bandIds = new JsonArray();
        if (bandIdsResource != null) {
            try {
                bandIds = Json.parse(new InputStreamReader(bandIdsResource)).asArray();
                return bandIds;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return bandIds;
    }

    private void generateData() {
        final JsonArray bandIds = getBandIds();

        MetalArchiveClientImpl client = new MetalArchiveClientImpl();
        List<String> letters = client.letters();
        Map<String, JsonObject> letterObjectsMap = letters.parallelStream()
                .collect(Collectors.toMap(Function.identity(), v -> cleanLetterObject(v, bandIds)));

        saveLetterFiles(letterObjectsMap);

        letterObjectsMap.keySet().forEach(l -> urls.set(new URLs().letterPageUrl(l, 1), "data/letters/" + letterFileName(l)));

        saveFile(RESOURCES_PATH.resolve("urls.json"), urls.toString());

        System.setProperty(HttpClient.HTTP_CLIENT_TYPE, DataGeneratorHttpClient.class.getName());

        List<BandInfo> infos = client.bandInfoStream().parallel().collect(Collectors.toList());

        if (bandIds.isEmpty()) {
            infos.forEach(i -> bandIds.add(i.getKey().getId()));

            saveFile(RESOURCES_PATH.getParent().resolve("bandIds.json"), bandIds.toString());
        }

        saveFile(RESOURCES_PATH.resolve("urls.json"), urls.toString());
    }

    private void saveLetterFiles(Map<String, JsonObject> letterObjectsMap) {
        Path lettersPath = createDirectories("letters");

        for (Map.Entry<String, JsonObject> entry : letterObjectsMap.entrySet()) {
            Path filePath = lettersPath.resolve(letterFileName(entry.getKey()));
            saveFile(filePath, entry.getValue().toString());
        }

    }

    private String letterFileName(String letter) {
        if ("#".equals(letter)) {
            letter = "NBR";
        } else if ("~".equals(letter)) {
            letter = "TILDA";
        }

        return letter + ".json";
    }

    public void saveFile(Path path, String content) {
        saveFile(path, content.getBytes());
    }

    public void saveFile(Path path, byte[] content) {
        try {
            createDirectories(path.getParent().toAbsolutePath().toString());
            FileOutputStream fos = new FileOutputStream(path.toFile());
            fos.write(content);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonObject cleanLetterObject(String letter, JsonArray bandIds) {
        JsonObject object = new LettersPage(letter).downloadLetterPageJson(1);

        object.set("iTotalRecords", 1);
        object.set("iTotalDisplayRecords", 1);
        JsonValue band = null;
        if (!bandIds.isEmpty()) {
            for (JsonValue b : object.get("aaData").asArray()) {
                String idString = b.asArray().get(0).asString().replace("<a href='https://www.metal-archives.com/bands/", "").split("/")[1];
                long id = Long.valueOf(idString.substring(0, idString.indexOf("'")));
                if (bandIds.values().stream().mapToLong(v -> v.asLong()).boxed().anyMatch(v -> v.equals(id))) {
                    band = b;
                }
            }
        } else {
            band = object.get("aaData").asArray().get(0);
        }

        if (band == null) {
            throw new RuntimeException("Band with predefined ID not found");
        }

        JsonArray aaData = new JsonArray();
        aaData.add(band);
        object.set("aaData", aaData);
        return object;
    }

    private Path createDirectories(String path) {
        try {
            Path resolved = RESOURCES_PATH.resolve(path);
            if (!Files.exists(resolved)) {
                Files.createDirectories(resolved);
            }

            return resolved;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TestDataGenerator() {
    }

    public static synchronized TestDataGenerator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestDataGenerator();
        }

        return INSTANCE;
    }

    public synchronized void addUrl(String url, String resourcePath) {
        urls.set(url, resourcePath);
    }

    private static void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }
}
