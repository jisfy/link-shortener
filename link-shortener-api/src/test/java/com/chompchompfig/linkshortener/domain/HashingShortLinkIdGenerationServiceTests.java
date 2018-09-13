package com.chompchompfig.linkshortener.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class HashingShortLinkIdGenerationServiceTests {

    private HashingShortLinkIdGenerationService hashingShortLinkIdGenerationStrategy =
            new HashingShortLinkIdGenerationService();
    private FixtureFactory fixtureFactory = new FixtureFactory();

    @Test(expected = IllegalArgumentException.class)
    public void getIdShouldThrowExceptionWhenNullUrl() {
        hashingShortLinkIdGenerationStrategy.getId(null);
    }

    @Test
    public void getIdShouldReturnValidValueWhenValidUrl() {
        String shortLinkId = hashingShortLinkIdGenerationStrategy.getId(fixtureFactory.newValidUrl());
        Assert.assertNotNull(shortLinkId);
    }
}
