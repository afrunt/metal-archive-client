package com.afrunt.metalarchive.impl.page;

import com.afrunt.metalarchive.impl.util.HttpClient;
import com.afrunt.metalarchive.impl.util.URLs;
import com.afrunt.metalarchive.impl.util.WithUtilities;
import com.afrunt.metalarchive.model.BandInfo;
import com.afrunt.metalarchive.model.BandKey;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.util.Map;
import java.util.Optional;

/**
 * @author Andrii Frunt
 */
public class BandPage implements WithUtilities {

    public BandInfo findBandInfo(BandKey key) {
        return findBandInfo(key.getId(), key.getTag());
    }

    public BandInfo findBandInfo(long id, String tag) {
        HttpClient httpClient = HttpClient.getInstance();
        TagNode page = httpClient.downloadAsTagNode(new URLs().bandPageUrl(id, tag));

        boolean withLongDescription = withLongDescription(page);

        String description;
        if (withLongDescription) {
            description = httpClient.downloadAsString(new URLs().bandDescriptionUrl(id));
        } else {
            description = page.findElementByAttValue("class", "band_comment clear", true, true).getText().toString();
        }

        description = new HtmlCleaner().clean(description).getText().toString().trim().replace("\n", " ").replace("\r", "");

        Map<String, String> statsMap = extractStatsMap(page.findElementByAttValue("id", "band_stats", true, true));

        String name = page.findElementByAttValue("id", "band_info", true, true)
                .findElementByName("h1", true).getText().toString();

        BandKey key = new BandKey()
                .setId(id)
                .setTag(tag)
                .setName(name)
                .setGenre(statsMap.get("GENRE"))
                .setStatus(statsMap.get("STATUS"))
                .setCountry(statsMap.get("COUNTRY_OF_ORIGIN"));

        return new BandInfo()
                .setKey(key)
                .setDescription(description.trim())
                .setLocation(nullIfNA(statsMap.get("LOCATION")))
                .setFormedIn(mapIfAvailable(statsMap.get("FORMED_IN"), Integer::valueOf))
                .setLyricalThemes(statsMap.get("LYRICAL_THEMES"))
                .setCurrentLabel(statsMap.get("CURRENT_LABEL"))
                .setLogoUrl(logoUrl(page))
                ;
    }

    private String logoUrl(TagNode page) {
        return Optional.ofNullable(page.findElementByAttValue("id", "logo", true, true))
                .map(link -> link.getChildTagList().get(0).getAttributes().get("src"))
                .orElse(null);
    }

    private boolean withLongDescription(TagNode page) {
        return page.findElementByAttValue("class", "btn_read_more", true, true) != null;
    }

}
