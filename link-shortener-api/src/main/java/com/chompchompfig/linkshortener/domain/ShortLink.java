package com.chompchompfig.linkshortener.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * A Short Link. A shortened version of a larger link
 */
@RedisHash(ShortLink.SHORTLINK_KEY_PREFIX)
public class ShortLink {

    public static final String SHORTLINK_KEY_PREFIX = "link";
    public static final String VISITS_FIELD_NAME = "visits";

    @Id
    private String id;
    private String longURL;
    private int visits;

    ShortLink() {
    }

    /**
     * Creates a new instance a Short Link with the given identifier and long URL
     * @param id <p>the Short Link identifier</p>
     * @param longURL <p>the long URL this Short Link helps shorten</p>
     * @param visits <p>the number of visits experienced by this Short Link</p>
     */
    public ShortLink(String id, String longURL, int visits) {
        this.id = id;
        this.longURL = longURL;
        this.visits = visits;
    }

    /**
     * Gets the Short Link identifier
     * @return <p>the identifier for this Short Link</p>
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the long URL this Short Link helps shorten
     * @return <p>the original, long URL, shortened by this Short Link</p>
     */
    public String getLongURL() {
        return longURL;
    }

    /**
     * Gets the number of visits for Short Link so far
     * @return <p>the current number of visits</p>
     */
    public int getVisits() {
        return visits;
    }
}
