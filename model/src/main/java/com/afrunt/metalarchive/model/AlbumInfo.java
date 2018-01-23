package com.afrunt.metalarchive.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrii Frunt
 */
public class AlbumInfo implements Serializable {
    private AlbumKey key;
    private String releaseDate;
    private String catalogId;
    private String label;
    private List<TrackInfo> trackList = new ArrayList<>();

    public AlbumKey getKey() {
        return key;
    }

    public AlbumInfo setKey(AlbumKey key) {
        this.key = key;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public AlbumInfo setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public AlbumInfo setCatalogId(String catalogId) {
        this.catalogId = catalogId;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public AlbumInfo setLabel(String label) {
        this.label = label;
        return this;
    }

    public List<TrackInfo> getTrackList() {
        return trackList;
    }

    public AlbumInfo setTrackList(List<TrackInfo> trackList) {
        this.trackList = trackList;
        return this;
    }
}
