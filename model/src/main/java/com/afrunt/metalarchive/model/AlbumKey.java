package com.afrunt.metalarchive.model;

import java.io.Serializable;

/**
 * @author Andrii Frunt
 */
public class AlbumKey implements Serializable {
    private Long id;
    private String tag;
    private String bandTag;
    private String name;
    private String type;
    private Integer year;

    public Long getId() {
        return id;
    }

    public AlbumKey setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public AlbumKey setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getBandTag() {
        return bandTag;
    }

    public AlbumKey setBandTag(String bandTag) {
        this.bandTag = bandTag;
        return this;
    }

    public String getName() {
        return name;
    }

    public AlbumKey setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public AlbumKey setType(String type) {
        this.type = type;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public AlbumKey setYear(Integer year) {
        this.year = year;
        return this;
    }

    public String buildAlbumUrl(){
        return String.format("https://www.metal-archives.com/albums/%s/%s/%d", bandTag, tag, id);
    }
}
