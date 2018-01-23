package com.afrunt.metalarchive.api;

import com.afrunt.metalarchive.model.BandInfo;
import com.afrunt.metalarchive.model.BandKey;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Andrii Frunt
 */
public interface MetalArchiveClient {
    default List<String> letters() {
        String lettersString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#~";

        return IntStream
                .range(0, lettersString.length())
                .boxed()
                .map(i -> lettersString.charAt(i) == '#' ? "NBR" : String.valueOf(lettersString.charAt(i)))
                .collect(Collectors.toList());
    }

    default Stream<String> lettersStream() {
        return letters().stream();
    }

    Stream<BandKey> bandKeysForLetterStream(String letter);

    default List<BandKey> bandKeysForLetter(String letter) {
        return bandKeysForLetterStream(letter)
                .collect(Collectors.toList());
    }

    default Stream<BandKey> bandKeysStream() {
        return lettersStream()
                .map(this::bandKeysForLetterStream)
                .flatMap(Function.identity());
    }

    default List<BandKey> bandKeys() {
        return bandKeysStream().collect(Collectors.toList());
    }

    BandInfo findBandInfo(long id, String tag);

    default BandInfo findBandInfo(BandKey key) {
        return findBandInfo(key.getId(), key.getTag());
    }

    Stream<BandInfo> bandInfoStream();

    Stream<BandInfo> bandInfoForLetterStream(String letter);

}
