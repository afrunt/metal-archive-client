package com.afrunt.metalarchive.impl.page;

import com.afrunt.metalarchive.impl.util.HttpClient;
import com.afrunt.metalarchive.impl.util.URLs;
import com.afrunt.metalarchive.model.BandKey;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Andrii Frunt
 */
public class LettersPage {
    private static final int PER_PAGE = 500;

    private String letter;

    public LettersPage(String letter) {
        this.letter = letter;
    }

    public Stream<BandKey> findBandKeysStream() {
        int pageCount = pageCount();

        return IntStream
                .range(0, pageCount).boxed()
                .map(p -> findBandKeysForPage(p + 1))
                .flatMap(List::stream)
                .collect(Collectors.toList())
                .stream()
                ;
    }

    public List<BandKey> findBandKeysForPage(int pageNumber) {
        JsonObject value = downloadLetterPageJson(pageNumber);
        JsonArray aaData = (JsonArray) value.get("aaData");
        List<BandKey> keys = new ArrayList<>();

        for (JsonValue v : aaData) {
            keys.add(letterDataEntryToBandKey((JsonArray) v));
        }

        return keys;
    }

    public JsonObject downloadLetterPageJson(int pageNumber) {
        String url = new URLs().letterPageUrl(letter, pageNumber);
        return HttpClient.getInstance().downloadAsJsonObject(url);
    }

    private int pageCount() {
        JsonObject index = downloadLetterPageJson(1);

        int totalRecords = index.getInt("iTotalRecords", 0);


        return totalRecords / PER_PAGE + (totalRecords % PER_PAGE == 0 ? 0 : 1);
    }

    private BandKey letterDataEntryToBandKey(JsonArray value) {
        String bandMarker = "https://www.metal-archives.com/bands/";
        String a = value.get(0).asString();
        a = a.substring(a.indexOf(bandMarker) + bandMarker.length());

        String tag = a.substring(0, a.indexOf("/"));

        a = a.substring(a.indexOf("/") + 1);

        Long id = Long.valueOf(a.substring(0, a.indexOf("'")));

        String name = a.substring(a.indexOf(">") + 1, a.lastIndexOf("<"));

        String statusTag = value.get(3).asString();
        String status = statusTag.substring(statusTag.indexOf("\">") + 2, statusTag.lastIndexOf("<"));

        return new BandKey()
                .setId(id)
                .setName(name)
                .setTag(tag)
                .setCountry(value.get(1).asString())
                .setGenre(value.get(2).asString())
                .setStatus(status);
    }
}
