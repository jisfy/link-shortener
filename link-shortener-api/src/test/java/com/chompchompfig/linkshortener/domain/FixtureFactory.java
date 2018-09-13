package com.chompchompfig.linkshortener.domain;

import com.chompchompfig.linkshortener.infrastructure.utils.URLUtils;
import com.chompchompfig.linkshortener.infrastructure.rest.ShortLinkController;
import com.chompchompfig.linkshortener.infrastructure.rest.ShortLinkResource;
import org.mockito.Mockito;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

public class FixtureFactory {

    public static final String VALID_BASE_URL = "http://short.ly/v";


    public static final String NON_EXISTENT_SHORTLINKS_ID_1 = "12313";
    public static final String NON_EXISTENT_SHORTLINKS_ID_2 = "12314";

    public static final Collection<String> NON_EXISTENT_SHORTLINKS_IDS =
            Arrays.asList(NON_EXISTENT_SHORTLINKS_ID_1, NON_EXISTENT_SHORTLINKS_ID_2);
    public static final String NON_EXISTENT_SHORTLINKS_IDS_STRING = NON_EXISTENT_SHORTLINKS_ID_1 +
            ShortLinkController.SHORTLINK_IDS_URI_SEPARATOR + NON_EXISTENT_SHORTLINKS_ID_2;


    public static final String VALID_SHORTLINK_ID = "1234567";
    public static final String VALID_LONG_URL = "http://blablablabla.com";;
    public static final String VALID_SHORT_URL = VALID_BASE_URL + "/" + VALID_SHORTLINK_ID;
    public static final int VALID_NUMBER_OF_VISITS = 0;

    public static final Collection<String> EXISTENT_AND_NON_EXISTENT_SHORTLINKS_IDS =
            Arrays.asList(VALID_SHORTLINK_ID, NON_EXISTENT_SHORTLINKS_ID_1);
    public static final String EXISTENT_AND_NON_EXISTENT_SHORTLINKS_IDS_STRING = VALID_SHORTLINK_ID +
            ShortLinkController.SHORTLINK_IDS_URI_SEPARATOR + NON_EXISTENT_SHORTLINKS_ID_1;


    public static final String VALID_SHORTLINK_ID_2 = "1234568";
    public static final String VALID_LONG_URL_2 = "http://bipbipbipbip.com";
    public static final String VALID_SHORT_URL_2 = VALID_BASE_URL + "/" + VALID_SHORTLINK_ID_2;

    public static final String SOME_VALID_SHORTLINK_ID = "4b252d29";
    public static final String SOME_VALID_SHORTLINK_URL = VALID_BASE_URL + "/" + SOME_VALID_SHORTLINK_ID;
    public static final String SOME_VALID_LONG_URL = "http://www.thetractorbeamhasyou.com";

    public static final String SOME_INVALID_LONG_URL = "htp://www.thetractorbeamhasyou.com";

    private URLUtils urlUtils = new URLUtils();

    public URL newValidUrl() {
        return urlUtils.toUrl(VALID_BASE_URL + "/" + VALID_SHORTLINK_ID);
    }

    public URL newValidShortUrl() {
        return urlUtils.toUrl("http://short.ly/v" + "/" + VALID_SHORTLINK_ID);
    }

    public ShortLink newShortLinkWithId() {
        ShortLink shortLink = Mockito.mock(ShortLink.class);
        Mockito.when(shortLink.getId()).thenReturn(VALID_SHORTLINK_ID);
        return shortLink;
    }

    public ShortLinkIdGenerationService newShortLinkIdGenerationStrategy() {
        ShortLinkIdGenerationService idGenerationStrategy = Mockito.mock(ShortLinkIdGenerationService.class);
        return idGenerationStrategy;
    }

    public ShortLinkRepository newShortLinkRepository() {
        ShortLinkRepository shortLinkRepository = Mockito.mock(ShortLinkRepository.class);
        return shortLinkRepository;
    }

    public ShortLink newValidShortLink() {
        return new ShortLink(VALID_SHORTLINK_ID, VALID_LONG_URL, VALID_NUMBER_OF_VISITS);
    }

    public Collection<ShortLink> newValidShortLinks() {
        return Arrays.asList(new ShortLink(VALID_SHORTLINK_ID, VALID_LONG_URL, VALID_NUMBER_OF_VISITS),
                new ShortLink(VALID_SHORTLINK_ID_2, VALID_LONG_URL_2, VALID_NUMBER_OF_VISITS));
    }

    public Collection<ShortLinkResource> newValidShortLinkResources() {
        ShortLinkResource shortLinkResource1 =
                newShortLinkResource(VALID_SHORTLINK_ID, VALID_LONG_URL, VALID_SHORT_URL);
        ShortLinkResource shortLinkResource2 =
                newShortLinkResource(VALID_SHORTLINK_ID_2, VALID_LONG_URL_2, VALID_SHORT_URL_2);

        return Arrays.asList(shortLinkResource1, shortLinkResource2);
    }

    public Collection<ShortLinkResource> newShortLinkResources() {
        return Arrays.asList(newShortLinkResource(VALID_SHORTLINK_ID, VALID_LONG_URL, VALID_SHORT_URL));
    }

    private ShortLinkResource newShortLinkResource(String validShortlinkId, String validLongUrl, String validShortUrl) {
        ShortLink shortLink1 = new ShortLink(validShortlinkId, validLongUrl, VALID_NUMBER_OF_VISITS);
        return new ShortLinkResource(shortLink1, validShortUrl);
    }


}
