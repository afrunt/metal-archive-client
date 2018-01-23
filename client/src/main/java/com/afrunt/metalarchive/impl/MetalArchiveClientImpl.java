package com.afrunt.metalarchive.impl;

import com.afrunt.metalarchive.api.MetalArchiveClient;
import com.afrunt.metalarchive.impl.mapper.AlbumKeyToAlbumInfo;
import com.afrunt.metalarchive.impl.mapper.BandKeyToBandInfo;
import com.afrunt.metalarchive.impl.page.DiscographyPage;
import com.afrunt.metalarchive.impl.page.LettersPage;
import com.afrunt.metalarchive.model.AlbumInfo;
import com.afrunt.metalarchive.model.BandInfo;
import com.afrunt.metalarchive.model.BandKey;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Andrii Frunt
 */
public class MetalArchiveClientImpl implements MetalArchiveClient {

    @Override
    public Stream<BandKey> bandKeysForLetterStream(String letter) {
        return new LettersPage(letter).findBandKeysStream();
    }

    @Override
    public BandInfo findBandInfo(long id, String tag) {
        return findBandInfo(new BandKey().setId(id).setTag(tag));
    }

    @Override
    public BandInfo findBandInfo(BandKey key) {
        return new BandKeyToBandInfo().apply(key);
    }

    @Override
    public Stream<BandInfo> bandInfoStream() {
        return bandInfoByKeysStream(bandKeysStream());
    }

    @Override
    public Stream<BandInfo> bandInfoForLetterStream(String letter) {
        return bandInfoByKeysStream(bandKeysForLetterStream(letter));
    }

    private Stream<BandInfo> bandInfoByKeysStream(Stream<BandKey> keysStream) {
        return keysStream
                .map(this::findBandInfo)
                .map(this::toBandInfoWithDiscography);
    }

    private BandInfo toBandInfoWithDiscography(BandInfo bandInfo) {
        List<AlbumInfo> discography = new DiscographyPage()
                .albumKeysStream(bandInfo.getKey().getId())
                .parallel()
                .map(new AlbumKeyToAlbumInfo())
                .collect(Collectors.toList());

        return bandInfo
                .setDiscography(discography);
    }

}
