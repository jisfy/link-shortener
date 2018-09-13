package com.chompchompfig.linkshortener.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

@RunWith(SpringRunner.class)
public class SimpleShortLinkFormattingServiceTests {

    public static final String INVALID_BASE_URL = "htjp://something.bad";

    private SimpleShortLinkFormattingService simpleShortLinkFormatter;
    private FixtureFactory fixtureFactory = new FixtureFactory();

    @Before
    public void setUp() {
        simpleShortLinkFormatter = new SimpleShortLinkFormattingService(FixtureFactory.VALID_BASE_URL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionWhenInvalidBaseUrl() {
        new SimpleShortLinkFormattingService(INVALID_BASE_URL);
    }


    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionWhenNullBaseUrl() {
        new SimpleShortLinkFormattingService(null);
    }

    @Test
    public void constructorShouldBuildValidInstanceWhenValidBaseUrl() {
        SimpleShortLinkFormattingService simpleShortLinkFormatter = new SimpleShortLinkFormattingService(FixtureFactory.VALID_BASE_URL);
        Assert.assertNotNull(simpleShortLinkFormatter);
    }

    @Test
    public void formatShouldReturnCorrectURLWhenValidBaseUrlAndShortLink() {
        // URL expectedShortLinkURL = fixtureFactory.newValidUrl();
        URL expectedShortLinkURL = fixtureFactory.newValidShortUrl();
        ShortLink shortLink = fixtureFactory.newShortLinkWithId();
        URL formattedURL = simpleShortLinkFormatter.format(shortLink);
        Assert.assertEquals(expectedShortLinkURL, formattedURL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatShouldThrowIllegalArgumentExceptionWhenValidBaseUrlAndNullShortLink() {
        simpleShortLinkFormatter.format(null);
    }

}
