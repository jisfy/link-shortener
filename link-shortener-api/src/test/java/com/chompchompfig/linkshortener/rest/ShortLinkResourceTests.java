package com.chompchompfig.linkshortener.rest;

import com.chompchompfig.linkshortener.domain.FixtureFactory;
import com.chompchompfig.linkshortener.domain.ShortLink;
import com.chompchompfig.linkshortener.infrastructure.rest.ShortLinkResource;
import org.junit.Test;

public class ShortLinkResourceTests {

    private FixtureFactory fixtureFactory = new FixtureFactory();

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionWhenShortLinkIsNull() {
        String validUrlString = fixtureFactory.newValidUrl().toString();
        new ShortLinkResource(null, validUrlString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionWhenShortUrlIsNull() {
        ShortLink shortLink = fixtureFactory.newShortLinkWithId();
        new ShortLinkResource(shortLink, null);
    }
}
