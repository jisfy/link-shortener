package com.chompchompfig.linkshortener.domain;

import java.net.URL;

/**
 * A Service which turns ShortLinks into their final URL representation. This
 * Domain Service is created in order to support multiple implementations and
 * ways of formatting final URLs. One could think this is a case of a feature
 * envy smell, because it only uses attributes from a ShortLink. But by having
 * this method placed in a ShortLink we would need to have a different
 * ShortLink implementation just for formatting purposes. This separated
 * interface enables the Shortener to use different formatting strategies, and
 * better decouples a ShortLink from the final server address and port hosting it
 */
public interface ShortLinkFormattingService {

    /**
     * Formats the given ShortLink into its final URL representation
     * @param shortLink <p>the ShortLink to convert</p>
     * @return <p>the URL representing the given ShortLink</p>
     */
    URL format(ShortLink shortLink);

}
