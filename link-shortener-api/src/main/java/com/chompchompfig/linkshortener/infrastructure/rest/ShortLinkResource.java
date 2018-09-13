package com.chompchompfig.linkshortener.infrastructure.rest;

import com.chompchompfig.linkshortener.domain.ShortLink;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.chompchompfig.linkshortener.domain.SimpleValidator;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A Resource representation for a Short Link
 */
@Relation(collectionRelation = "shortLinks", value = "shortLink")
@JsonPropertyOrder({"linkId", "shortURL", "longURL"})
public class ShortLinkResource extends ResourceSupport {

    public static final String SHORT_LINK_RESOURCE_CREATION_ERROR_NULL_SHORTLINK_MSG =
            "Can't create a ShortLinkResource with null ShortLink";
    public static final String SHORT_LINK_RESOURCE_CREATION_ERROR_NULL_SHORT_URL_MSG =
            "Can't create a ShortLinkResource with null short URL";

    public static final String LINKID_PROPERTY_NAME = "linkId";
    public static final String SHORT_URL_PROPERTY_NAME = "shortURL";
    public static final String LONG_URL_PROPERTY_NAME = "longURL";

    private SimpleValidator simpleValidator = new SimpleValidator();
    private ShortLink shortLink;
    private String shortURL;
    private int visits;

    /**
     * Creates a new Short Link Resource representation instance
     * @param shortLink <p>the Short Link to create the Resource for</p>
     * @param shortURL <p>the Short URL representation of the Short Link</p>
     */
    public ShortLinkResource(ShortLink shortLink, String shortURL) {
        simpleValidator.notNull(shortLink, SHORT_LINK_RESOURCE_CREATION_ERROR_NULL_SHORTLINK_MSG);
        simpleValidator.notNull(shortURL, SHORT_LINK_RESOURCE_CREATION_ERROR_NULL_SHORT_URL_MSG);
        this.shortLink = shortLink;
        this.shortURL = shortURL;
        this.add(linkTo(methodOn(ShortLinkController.class).getShortLink(this.shortLink.getId())).withSelfRel());
        this.add(linkTo(methodOn(ShortLinkController.class).visitShortLink(this.shortLink.getId())).withRel("visit"));
    }

    /**
     * Gets the Short Link identifier
     *
     * @return <p>the Short Link identifier</p>
     */
    public String getLinkId() {
        return shortLink.getId();
    }

    /**
     * Gets the Short Link URL. This is the shortened URL for a given long link
     * @return <p>the Short Link URL</p>
     */
    public String getShortURL() {
        return this.shortURL;
    }

    /**
     * Gets the Long Link URL. This is, the original URL to be shortened
     * @return <p>the Long Link URL</p>
     */
    public String getLongURL() {
        return shortLink.getLongURL();
    }

    /**
     * Gets the total number of visits experienced by this Short Link Resource
     * @return <p>the number of visits</p>
     */
    public int getVisits() {
        return shortLink.getVisits();
    }
}
