package com.afrunt.metalarchive.test;

import com.afrunt.metalarchive.impl.MetalArchiveClientImpl;
import com.afrunt.metalarchive.model.BandKey;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
public class LettersTest extends BaseTest {
    @Test
    public void testFindLetters() {
        MetalArchiveClientImpl client = new MetalArchiveClientImpl();
        String allLettersString = client.letters().stream().collect(Collectors.joining());
        Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZNBR~", allLettersString);
    }

    @Test
    public void testFindBankKeysForLetter() {
        MetalArchiveClientImpl client = new MetalArchiveClientImpl();
        List<BandKey> keys = client.bandKeysStream().parallel().collect(Collectors.toList());
        Assert.assertTrue(keys.size() == client.letters().size());
    }
}
