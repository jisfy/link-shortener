package com.chompchompfig.linkshortener.infrastructure.utils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * An utility class with URL helper functions. This could live better in
 * the URL class itself, but it is closed for extension. This is then
 * a typical case of a foreign method
 */
public class URLUtils {

    public static final String URL_BUILD_ERROR_MSG = "Couldn't build a valid URL from ";

    /**
     * Turns the given URL string into its corresponding URL instance
     * @param url <p>the source URL string to convert</p>
     * @return <p>a URL representing the given URL string</p>
     */
    public URL toUrl(String url) {
        try {
            URL validURL = new URL(url);
            return validURL;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(URL_BUILD_ERROR_MSG + url);
        }
    }
}
