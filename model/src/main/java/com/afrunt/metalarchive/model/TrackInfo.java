package com.afrunt.metalarchive.model;

import java.io.Serializable;

/**
 * @author Andrii Frunt
 */
public class TrackInfo implements Serializable {
    private String id;
    private String name;
    private Integer length;
    private String lyrics;

    public String getId() {
        return id;
    }

    public TrackInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TrackInfo setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getLength() {
        return length;
    }

    public TrackInfo setLength(Integer length) {
        this.length = length;
        return this;
    }

    public String getLyrics() {
        return lyrics;
    }

    public TrackInfo setLyrics(String lyrics) {
        this.lyrics = lyrics;
        return this;
    }
}
