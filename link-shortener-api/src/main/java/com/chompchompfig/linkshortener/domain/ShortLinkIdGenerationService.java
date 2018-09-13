package com.chompchompfig.linkshortener.domain;

import java.net.URL;

/**
 * A Strategy (design pattern) for ShortLink identifier generation.
 * This Domain Service is introduced in order to support different
 * strategies for ShortLink identifier generation. If the operation
 * lived in the ShortLink Entity, we would need a different
 * Entity implementation to support different identifier generation.
 * Besides I think ShortLinks should be provided with their
 * identifiers, not so much let them generate their own
 */
public interface ShortLinkIdGenerationService {

    /**
     * Generates a ShortLink identifier for the given URL
     * @param longUrl <p>the long URL to generate an identifier for</p>
     * @return <p>a ShortLink identifier</p>
     */
    String getId(URL longUrl);

}
