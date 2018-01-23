package com.afrunt.metalarchive.impl.page;

import com.afrunt.metalarchive.impl.util.HttpClient;
import com.afrunt.metalarchive.impl.util.URLs;
import com.afrunt.metalarchive.model.AlbumInfo;
import com.afrunt.metalarchive.model.AlbumKey;
import org.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Andrii Frunt
 */
public class DiscographyPage {


    public Stream<AlbumKey> albumKeysStream(long bandId) {
        TagNode page = HttpClient.getInstance().downloadAsTagNode(new URLs().bandAlbumListUrl(bandId));

        if (page.findElementByAttValue("colspan", "5", true, true) != null) {
            return new ArrayList<AlbumKey>().stream();
        }

        return page
                .findElementByName("tbody", true)
                .getChildTagList()
                .stream()
                .map(this::rowToAlbumKey);
    }

    public AlbumInfo albumKeyToAlbumInfo(AlbumKey key) {
        TagNode page = HttpClient.getInstance().downloadAsTagNode(key.buildAlbumUrl());
        Map<String, String> albumInfoMap = extractAlbumInfoMap(page);
        return new AlbumInfo()
                .setKey(key);
    }

    private Map<String, String> extractAlbumInfoMap(TagNode page) {
        List<String> keyValues = page.findElementByAttValue("id", "album_info", true, true)
                .getChildTagList()
                .stream()
                .filter(el -> "dl".equals(el.getName()))
                .map(TagNode::getChildTagList)
                .flatMap(List::stream)
                .map(el -> el.getText().toString())
                .collect(Collectors.toList());


        return IntStream.range(0, keyValues.size() / 2 - 1)
                .boxed()
                .collect(Collectors.toMap(
                        i -> keyValues.get(i * 2).replace(":", "").replace(" ", "_").toUpperCase(),
                        i -> keyValues.get(i * 2 + 1))
                );

    }

    private AlbumKey rowToAlbumKey(TagNode row) {
        List<TagNode> columns = row.getChildTagList();
        TagNode link = columns.get(0).getChildTagList().iterator().next();

        String[] hrefSplit = link.getAttributes().get("href").replace("https://www.metal-archives.com/albums", "").split("/");


        String type = columns.get(1).getText().toString();
        String year = columns.get(2).getText().toString();
        return new AlbumKey()
                .setId(Long.valueOf(hrefSplit[3]))
                .setName(link.getText().toString())
                .setBandTag(hrefSplit[1])
                .setTag(hrefSplit[2])
                .setType(type)
                .setYear("N/A".equals(year) ? null : Integer.valueOf(year));
    }
}
