package com.afrunt.metalarchive.impl.page;

import com.afrunt.metalarchive.impl.util.HttpClient;
import com.afrunt.metalarchive.impl.util.WithUtilities;
import com.afrunt.metalarchive.model.AlbumInfo;
import com.afrunt.metalarchive.model.AlbumKey;
import com.afrunt.metalarchive.model.TrackInfo;
import org.htmlcleaner.TagNode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
public class AlbumPage implements WithUtilities {

    public String downloadLyrics(String trackId) {
        return HttpClient.getInstance().downloadAsTagNode(String.format("https://www.metal-archives.com/release/ajax-view-lyrics/id/%s", trackId)).getText().toString().trim();
    }

    public TagNode downloadAlbumPage(AlbumKey key) {
        return HttpClient.getInstance().downloadAsTagNode(key.buildAlbumUrl());
    }

    public AlbumInfo albumKeyToAlbumInfo(AlbumKey key) {
        TagNode page = downloadAlbumPage(key);

        TagNode albumInfoDiv = page.findElementByAttValue("id", "album_info", true, true);
        Map<String, String> albumInfoMap = extractStatsMap(albumInfoDiv);

        return new AlbumInfo()
                .setKey(key)
                .setTrackList(extractTrackList(page))
                .setLabel(nullIfNA(albumInfoMap.get("LABEL")))
                .setReleaseDate(nullIfNA(albumInfoMap.get("RELEASE_DATE")));
    }

    public List<TrackInfo> extractTrackList(TagNode albumPage) {
        return extractTrackRows(albumPage)
                .parallelStream()
                .map(this::toTrackInfoFromRow)
                .collect(Collectors.toList());
    }

    private TrackInfo toTrackInfoFromRow(TagNode row) {

        List<TagNode> columns = row.getChildTagList();

        TrackInfo trackInfo = new TrackInfo();

        if (!columns.get(0).getChildTagList().isEmpty()) {
            trackInfo.setId(columns.get(0).getChildTagList().get(0).getAttributes().get("name").trim());
        }

        if (columns.size() > 2) {
            String lengthString = columns.get(2).getText().toString();
            String[] split = lengthString.split(":");
            Integer length = split.length < 2 ? null : Integer.valueOf(split[0]) * 60 + Integer.valueOf(split[1]);
            trackInfo.setLength(length);
        }

        if (columns.size() > 3 && columns.get(3).getChildTagList().size() == 1 && columns.get(3).getChildTagList().iterator().next().getName().equals("a")) {

            trackInfo.setLyrics(extractLyrics(row));
        }

        return trackInfo
                .setName(columns.get(1).getText().toString().trim());
    }

    private String extractLyrics(TagNode row) {
        TagNode column = row.getChildTagList().get(3);


        TagNode link = column.findElementByName("a", true);
        if (link == null) {
            return null;
        }

        String id = link.getAttributes().get("href").replace("#", "");

        return downloadLyrics(id).trim();
    }

    private List<TagNode> extractTrackRows(TagNode albumPage) {
        return findElementById(albumPage, "album_tabs_tracklist")
                .findElementByName("table", true)
                .findElementByName("tbody", true)
                .getChildTagList().stream().filter(row ->
                        row.getChildTagList().size() >= 2
                                && !"displayNone".equals(row.getAttributeByName("class"))
                                && row.getChildTagList().get(0).getChildTagList().size() > 0
                )
                .collect(Collectors.toList());
    }
}
