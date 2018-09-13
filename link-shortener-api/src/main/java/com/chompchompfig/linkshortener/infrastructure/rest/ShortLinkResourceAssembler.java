package com.chompchompfig.linkshortener.infrastructure.rest;

import com.chompchompfig.linkshortener.domain.ShortLink;
import com.chompchompfig.linkshortener.domain.ShortLinkFormattingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * A ResourceAssembler for Short Links. This is, a helper class that converts ShortLinks to ShortLinkResources
 */
@Component
public class ShortLinkResourceAssembler extends ResourceAssemblerSupport<ShortLink, ShortLinkResource> {

    private ShortLinkFormattingService shortLinkFormattingService;

    /**
     * Creates a new instance of a ShortLinkResourceAssembler
     * @param shortLinkFormattingService <p>the ShortLinkFormattingService to use to create the final shortened URLs</p>
     */
    @Autowired
    public ShortLinkResourceAssembler(ShortLinkFormattingService shortLinkFormattingService) {
        super(ShortLinkController.class, ShortLinkResource.class);
        this.shortLinkFormattingService = shortLinkFormattingService;
    }

    /**
     * Converts the given ShortLink into a ShortLinkResource
     * @param shortLink <p>the ShortLink to convert</p>
     * @return <p>a ShortLinkResource resulting from the conversion</p>
     */
    @Override
    public ShortLinkResource toResource(ShortLink shortLink) {
        return new ShortLinkResource(shortLink, shortLinkFormattingService.format(shortLink).toString());
    }
}
