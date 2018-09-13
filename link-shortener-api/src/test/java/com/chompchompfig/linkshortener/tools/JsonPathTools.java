package com.chompchompfig.linkshortener.tools;

import java.text.MessageFormat;

public class JsonPathTools {

    public String jsonPathForShortLinkPropertyInList(int indexOfShortLinkInList, String propertyName) {
        String jsonPathForFilmPropertyInList = MessageFormat.format(
                "$._embedded[''shortLinks''][{0, number, integer}].{1}", indexOfShortLinkInList, propertyName);
        return jsonPathForFilmPropertyInList;
    }

    public String jsonPathForProperty(String propertyName) {
        String jsonPathForProperty = MessageFormat.format("$.{0}", propertyName);
        return jsonPathForProperty;
    }
}
