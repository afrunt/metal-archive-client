package com.afrunt.metalarchive.impl.mapper;

import com.afrunt.metalarchive.impl.page.BandPage;
import com.afrunt.metalarchive.model.BandInfo;
import com.afrunt.metalarchive.model.BandKey;

import java.util.function.Function;

/**
 * @author Andrii Frunt
 */
public class BandKeyToBandInfo implements Function<BandKey, BandInfo> {
    @Override
    public BandInfo apply(BandKey bandKey) {
        return new BandPage().findBandInfo(bandKey);
    }
}
