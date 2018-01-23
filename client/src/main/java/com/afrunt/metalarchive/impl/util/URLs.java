package com.afrunt.metalarchive.impl.util;

/**
 * @author Andrii Frunt
 */
public class URLs {
    private static final int PER_PAGE = 500;
    private static final String LETTER_PAGE_DETAILS_URL_PATTERN = "https://www.metal-archives.com/browse/ajax-letter/l/%s/json/1?sEcho=%d&iColumns=4&sColumns=&iDisplayStart=%d&iDisplayLength=" + PER_PAGE;

    private static final String BAND_PAGE_URL_PATTERN = "https://www.metal-archives.com/bands/%s/%d";
    private static final String BAND_DESCRIPTION_URL_PATTERN = "https://www.metal-archives.com/band/read-more/id/%d";
    private static final String BAND_ALBUM_LIST_URL_PATTERN = "https://www.metal-archives.com/band/discography/id/%d/tab/all";

    public String bandPageUrl(long id, String tag) {
        return String.format(BAND_PAGE_URL_PATTERN, tag, id);
    }

    public String bandDescriptionUrl(long id) {
        return String.format(BAND_DESCRIPTION_URL_PATTERN, id);
    }

    public String bandAlbumListUrl(long id) {
        return String.format(BAND_ALBUM_LIST_URL_PATTERN, id);
    }

    public String letterPageUrl(String letter, int pageNumber) {
        if ("#".equals(letter)) {
            letter = "NBR";
        }

        return String.format(LETTER_PAGE_DETAILS_URL_PATTERN, letter, pageNumber, (pageNumber - 1) * PER_PAGE);
    }
}
