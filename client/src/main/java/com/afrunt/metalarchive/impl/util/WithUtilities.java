package com.afrunt.metalarchive.impl.util;

import org.htmlcleaner.TagNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Andrii Frunt
 */
public interface WithUtilities {
    default String nullIfNA(String value) {
        return "N/A".equals(value) ? null : value;
    }

    default <T> T mapIfAvailable(String value, Function<String, T> mapper) {
        return Optional.ofNullable(nullIfNA(value)).map(mapper).orElse(null);
    }

    default Map<String, String> extractStatsMap(TagNode parentNode) {
        List<String> keyValues = parentNode
                .getChildTagList()
                .stream()
                .filter(el -> "dl".equals(el.getName()))
                .map(TagNode::getChildTagList)
                .flatMap(List::stream)
                .map(el -> el.getText().toString())
                .collect(Collectors.toList());


        return IntStream.range(0, keyValues.size() / 2 - 1)
                .boxed()
                .collect(Collectors.toMap(
                        i -> keyValues.get(i * 2).replace(":", "").replace(" ", "_").toUpperCase(),
                        i -> keyValues.get(i * 2 + 1))
                );

    }

    default TagNode findElementById(TagNode node, String id) {
        return node.findElementByAttValue("id", id, true, true);
    }
}
