package com.chompchompfig.linkshortener.infrastructure.rest;

import com.chompchompfig.linkshortener.domain.ShortLink;
import com.chompchompfig.linkshortener.domain.Shortener;
import com.chompchompfig.linkshortener.infrastructure.utils.URLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A RESTful Controller to serve ShortLink Resources
 */
@RestController
public class ShortLinkController {

    public static final String API_URI = "/api";
    public static final String API_SHORTLINKS_URI = API_URI + "/shortlinks";
    public static final String API_SHORTLINKS_IDS_URI = API_SHORTLINKS_URI + "/ids/{ids}";
    public static final String API_SHORTLINK_ID_URI = API_URI + "/shortlink/{id}";
    public static final String VISIT_SHORTLINK_URI_PREFIX = "/v";
    public static final String SLASH_ID_URI = VISIT_SHORTLINK_URI_PREFIX + "/{id}";
    public static final String SHORTLINK_IDS_URI_SEPARATOR = ":";
    public static final String SHORTLINKS_REL_NAME = "shortlinks";
    public static final String SHORTLINKS_BY_IDS_REL_NAME = "shortlinksByIds";

    @Autowired
    private Shortener shortener;
    @Autowired
    private ShortLinkResourceAssembler shortLinkResourceAssembler;
    private URLUtils urlUtils = new URLUtils();


    /**
     * The API entry point
     *
     * @return <p>a ResponseEntity representing an HTTP moved permanently, that will finally result in a redirection</p>
     */
    @GetMapping(value = API_URI, produces = MediaTypes.HAL_JSON_VALUE)
    public Resource api() {
        Resource apiResource = new Resource("");
        apiResource.add(linkTo(methodOn(ShortLinkController.class).getShortLinks()).withRel(SHORTLINKS_REL_NAME));
        apiResource.add(linkTo(methodOn(ShortLinkController.class).getShortLinksByIds(null))
                .withRel(SHORTLINKS_BY_IDS_REL_NAME));
        return apiResource;
    }

    /**
     * Performs the URL redirection corresponding to the given Short Link identifier
     *
     * @param id <p>the Short Link identifier</p>
     * @return <p>a ResponseEntity representing an HTTP moved permanently, that will finally result in a redirection</p>
     */
    @GetMapping(value = SLASH_ID_URI)
    public ResponseEntity visitShortLink(@PathVariable String id) {
        ShortLink visitingLink = shortener.visitShortLink(id);
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, visitingLink.getLongURL())
                .cacheControl(CacheControl.noStore()).build();

        return responseEntity;
    }

    /**
     * Gets the Short Link corresponding to the given identifier
     *
     * @param id <p>the Short Link identifier</p>
     * @return <p>a ShortLink Resource representation corresponding to the given identifier</p>
     */
    @GetMapping(value = API_SHORTLINK_ID_URI, produces = MediaTypes.HAL_JSON_VALUE)
    public ShortLinkResource getShortLink(@PathVariable String id) {
        ShortLink shortLink = shortener.getShortLinkById(id);
        ShortLinkResource shortLinkResource = shortLinkResourceAssembler.toResource(shortLink);
        return shortLinkResource;
    }

    /**
     * Gets a list of Resource representations for all Short Links available in the application
     *
     * @return <p>a list of all Resources representing all Short Links currently available</p>
     */
    @GetMapping(value = API_SHORTLINKS_URI, produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<ShortLinkResource> getShortLinks() {
        Collection<ShortLink> allShortLinks = shortener.getAllShortLinks();
        Resources<ShortLinkResource> shortLinkResources =
                new Resources(shortLinkResourceAssembler.toResources(allShortLinks));
        return shortLinkResources;
    }

    /**
     * Gets a list of Resource representations for the ShortLinks requested by their identifiers
     *
     * @return <p>a list of all Resources representing the requested Short Links</p>
     */
    @GetMapping(value = API_SHORTLINKS_IDS_URI, produces = MediaTypes.HAL_JSON_VALUE)
    public Resources<ShortLinkResource> getShortLinksByIds(@PathVariable String ids) {
        Collection<String> requestedIds = Arrays.asList(ids.split(SHORTLINK_IDS_URI_SEPARATOR));
        Collection<ShortLink> shortLinksByIds = shortener.getShortLinksByIds(requestedIds);
        Resources<ShortLinkResource> shortLinkResources =
                new Resources(shortLinkResourceAssembler.toResources(shortLinksByIds));
        return shortLinkResources;
    }

    /**
     * Creates a new Short Link for a given URL, storing it for later retrieval
     *
     * @param url <p>the URL to create the new Short Link for</p>
     * @return <p>a Resource representation for the newly created Short Link</p>
     */
    @PostMapping(API_SHORTLINKS_URI)
    public ShortLinkResource addShortLink(@RequestBody String url) {
        ShortLink shortLink = shortener.shorten(urlUtils.toUrl(url));
        return shortLinkResourceAssembler.toResource(shortLink);
    }
}
