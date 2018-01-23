package com.afrunt.metalarchive.model;

import java.io.Serializable;

/**
 * @author Andrii Frunt
 */
public class BandKey implements Serializable {
    private Long id;
    private String name;
    private String tag;
    private String genre;
    private String status;
    private String country;

    public Long getId() {
        return id;
    }

    public BandKey setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public BandKey setName(String name) {
        this.name = name;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public BandKey setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public BandKey setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public BandKey setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public BandKey setCountry(String country) {
        this.country = country;
        return this;
    }

    public String buildUrl() {
        return "https://www.metal-archives.com/bands/" + tag + "/" + id;
    }
}
