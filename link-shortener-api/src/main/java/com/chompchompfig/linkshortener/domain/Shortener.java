package com.chompchompfig.linkshortener.domain;

import java.net.URL;
import java.util.Collection;

/**
 * A Link shortener. A class that will accept long URLs and turn them into others of more manageable size
 */
public class Shortener {

    public static final String SHORT_LINK_REPOSITORY_VALIDATION_ERROR_MSG = "ShortLink Repository can't be null";
    public static final String ID_GENERATION_SERVICE_VALIDATION_ERROR_MSG =
            "ShortLink Id Generation Service can't be null";
    public static final String SHORTEN_NULL_URL_ERROR_MSG = "can't shorten a null URL";

    private ShortLinkRepository repository;
    private ShortLinkIdGenerationService idGenerationService;
    private SimpleValidator validator = new SimpleValidator();

    /**
     * Creates a new instance of a link shortener
     * @param shortLinkRepository <p>the ShortLink Repository to get/store the ShortLinks from/to</p>
     * @param idGenerationService <p>the ShortLinkIdGenerationService that will be used to generate the
     *                             identifiers of the shortened links</p>
     */
    public Shortener(ShortLinkRepository shortLinkRepository, ShortLinkIdGenerationService idGenerationService) {
        validator.notNull(shortLinkRepository, SHORT_LINK_REPOSITORY_VALIDATION_ERROR_MSG);
        validator.notNull(idGenerationService, ID_GENERATION_SERVICE_VALIDATION_ERROR_MSG);
        this.repository = shortLinkRepository;
        this.idGenerationService = idGenerationService;
    }

    /**
     * Gets the ShortLink corresponding to the given identifier
     * @param id <p>the identifier of the ShortLink to get</p>
     * @return <p>the ShortLink with the given identifier</p>
     * @throws IllegalArgumentException <p>in case a ShortLink with the given identifier does not exist</p>
     */
    public ShortLink getShortLinkById(String id) {
        return repository.findById(id).get();
    }

    /**
     * Gets all ShortLinks in the application
     * @return <p>a Collection of all the ShortLinks registered in the application</p>
     */
    public Collection<ShortLink> getAllShortLinks() {
        return repository.findAll();
    }

    /**
     * Gets the requested ShortLinks by their identifiers
     * @return <p>a Collection of all the requested ShortLinks</p>
     */
    public Collection<ShortLink> getShortLinksByIds(Collection<String> ids) {
        return repository.findByIds(ids);
    }

    /**
     * Performs a visit for a ShortLink, also incrementing its number of visits
     *
     * @param id <p>the identifier of the ShortLink to visit</p>
     * @return <p>the ShortLink being visited</p>
     */
    public ShortLink visitShortLink(String id) {
        ShortLink visitingShortLink = getShortLinkById(id);
        repository.addVisits(id, 1);
        return visitingShortLink;
    }

    /**
     * Shortens the given long URL
     * @param longURL <p>the long URL to shorten</p>
     * @return <p>a ShortLink with the shortened version of the given URL</p>
     */
    public ShortLink shorten(URL longURL) {
        validator.notNull(longURL, SHORTEN_NULL_URL_ERROR_MSG);
        String shortLinkId = idGenerationService.getId(longURL);
        ShortLink shortLink = new ShortLink(shortLinkId, longURL.toString(), 0);
        repository.save(shortLink);
        return shortLink;
    }

    /**
     * Shortens the given long URL
     * @param longURL <p>the long URL to shorten</p>
     * @param shortLinkFormattingService <p>a ShortLinkFormattingService to use to build the final shortened URL</p>
     * @return <p>a shortened version of the given long URL</p>
     */
    public URL shorten(URL longURL, ShortLinkFormattingService shortLinkFormattingService) {
        ShortLink shortLink = shorten(longURL);
        return shortLinkFormattingService.format(shortLink);
    }
}
