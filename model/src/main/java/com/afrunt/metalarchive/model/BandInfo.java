package com.afrunt.metalarchive.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andrii Frunt
 */
public class BandInfo implements Serializable {
    private BandKey key;
    private Integer formedIn;
    private String location;
    private String currentLabel;
    private String lyricalThemes;
    private String logoUrl;
    private String description;
    private List<AlbumInfo> discography;

    public BandKey getKey() {
        return key;
    }

    public BandInfo setKey(BandKey key) {
        this.key = key;
        return this;
    }

    public Integer getFormedIn() {
        return formedIn;
    }

    public BandInfo setFormedIn(Integer formedIn) {
        this.formedIn = formedIn;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public BandInfo setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getCurrentLabel() {
        return currentLabel;
    }

    public BandInfo setCurrentLabel(String currentLabel) {
        this.currentLabel = currentLabel;
        return this;
    }

    public String getLyricalThemes() {
        return lyricalThemes;
    }

    public BandInfo setLyricalThemes(String lyricalThemes) {
        this.lyricalThemes = lyricalThemes;
        return this;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public BandInfo setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BandInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<AlbumInfo> getDiscography() {
        return discography;
    }

    public BandInfo setDiscography(List<AlbumInfo> discography) {
        this.discography = discography;
        return this;
    }
}
