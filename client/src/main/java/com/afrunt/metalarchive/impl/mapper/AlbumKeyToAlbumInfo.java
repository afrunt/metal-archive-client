package com.afrunt.metalarchive.impl.mapper;

import com.afrunt.metalarchive.impl.page.AlbumPage;
import com.afrunt.metalarchive.model.AlbumInfo;
import com.afrunt.metalarchive.model.AlbumKey;

import java.util.function.Function;

/**
 * @author Andrii Frunt
 */
public class AlbumKeyToAlbumInfo implements Function<AlbumKey, AlbumInfo> {
    @Override
    public AlbumInfo apply(AlbumKey albumKey) {
        return new AlbumPage().albumKeyToAlbumInfo(albumKey);
    }
}
