package com.chompchompfig.linkshortener.domain;

import java.net.URL;
import java.net.MalformedURLException;

/**
 * A Simple ShortLinkFormattingService that appends the ShortLink identifier to a base URL
 */
public class SimpleShortLinkFormattingService implements ShortLinkFormattingService {

    public static final String INVALID_BASE_URL_ERROR_MSG = "Invalid base url ";
    public static final String INVALID_SHORTLINK_NULL_ERROR_MSG = "ShortLink can't be null";

    private SimpleValidator validator = new SimpleValidator();
    private String baseUrl;

    /**
     * Creates a new instance of the ShortLinkFormattingService with the given base URL
     * @param baseUrl <p>the base URL to use for formatting</p>
     * @throws IllegalArgumentException <p>in case the base URL is not a valid URL</p>
     */
    public SimpleShortLinkFormattingService(String baseUrl) {
        validateBaseUrl(baseUrl);
        this.baseUrl = baseUrl;
    }

    /**
     * Performs the validation of the given base URL
     * @param baseUrl <p>the base URL to validate</p>
     * @throws IllegalArgumentException <p>in case the base URL given is not a valid URL</p>
     */
    private void validateBaseUrl(String baseUrl) {
        convert(baseUrl);
    }

    /**
     * @see ShortLinkFormattingService#format(ShortLink)
     */
    @Override
    public URL format(ShortLink shortLink) {
        validator.notNull(shortLink, INVALID_SHORTLINK_NULL_ERROR_MSG);
        return convert(baseUrl + "/" + shortLink.getId());
    }

    /**
     * Converts the given source URL string into a corresponding URL
     * @param sourceUrl <p>the source URL string to use for conversion</p>
     * @return <p>a URL instance representing the given URL string</p>
     * @throws IllegalArgumentException <p>in case the conversion can't be performed successfully</p>
     */
    URL convert(String sourceUrl) {
        try{
            validator.notNull(sourceUrl, SimpleValidator.INVALID_NULL_URL_ERROR_MSG);
            return new URL(sourceUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(INVALID_BASE_URL_ERROR_MSG + e.getMessage());
        }
    }
}
