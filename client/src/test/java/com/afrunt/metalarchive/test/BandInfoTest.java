package com.afrunt.metalarchive.test;

import com.afrunt.metalarchive.impl.MetalArchiveClientImpl;
import com.afrunt.metalarchive.model.BandInfo;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
public class BandInfoTest extends BaseTest {
    @Test
    public void testBandInfo() {
        MetalArchiveClientImpl client = new MetalArchiveClientImpl();
        List<BandInfo> bands = client.bandInfoStream().parallel().collect(Collectors.toList());
        System.out.println("");
    }


}
