package com.chompchompfig.linkshortener.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.net.URL;

public class ShortenerTests {

    private FixtureFactory fixtureFactory = new FixtureFactory();
    private ShortLinkRepository shortLinkRepository;
    private ShortLinkIdGenerationService shortLinkIdGenerationService;
    private Shortener shortener;
    private URL validURL;

    @Before
    public void setUp() {
        shortLinkRepository = fixtureFactory.newShortLinkRepository();
        shortLinkIdGenerationService = fixtureFactory.newShortLinkIdGenerationStrategy();
        shortener = new Shortener(shortLinkRepository, shortLinkIdGenerationService);
        validURL = fixtureFactory.newValidUrl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionWhenShortLinkRepositoryIsNull() {
        new Shortener(null, fixtureFactory.newShortLinkIdGenerationStrategy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionWhenIdGenerationStrategyIsNull() {
        new Shortener(fixtureFactory.newShortLinkRepository(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shortenShouldThrowExceptionWhenURLIsNull() {
        shortener.shorten(null);
    }

    @Test
    public void shortenShouldReturnValidShortLinkWhenValidURL() {
        Mockito.when(shortLinkIdGenerationService.getId(validURL)).thenReturn(FixtureFactory.VALID_SHORTLINK_ID);
        ArgumentCaptor<ShortLink> shortLinkArgumentCaptor = ArgumentCaptor.forClass(ShortLink.class);
        ShortLink shortenedLink = shortener.shorten(validURL);

        Mockito.verify(shortLinkRepository, Mockito.times(1)).save(shortLinkArgumentCaptor.capture());
        ShortLink savedShortLink = shortLinkArgumentCaptor.getValue();

        Assert.assertNotNull(shortenedLink);
        Assert.assertEquals(FixtureFactory.VALID_SHORTLINK_ID, savedShortLink.getId());
        Assert.assertEquals(shortenedLink, savedShortLink);
        Assert.assertEquals(validURL.toString(), savedShortLink.getLongURL());
    }
}
