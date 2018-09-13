package com.chompchompfig.linkshortener.domain;

import com.google.common.hash.Hashing;

import java.net.URL;

/**
 * A hash based ShortLink identifier generation Service. This implementation uses a Hashing algorithm to create
 * the identifiers. Notice that the length of the identifiers generated could have an impact on the length of the
 * final ShortLinks. Thus the election of a Hashing algorithm that yields 32 bit results. Obviously this will have
 * an impact on the total number of ShortLinks that can be uniquely identified
 */
public class HashingShortLinkIdGenerationService implements ShortLinkIdGenerationService {

    private SimpleValidator urlValidator = new SimpleValidator();

    /**
     * @see ShortLinkIdGenerationService#getId(URL)
     */
    @Override
    public String getId(URL longUrl) {
        urlValidator.notNull(longUrl, SimpleValidator.INVALID_NULL_URL_ERROR_MSG);
        return Hashing.murmur3_32().hashBytes(longUrl.toString().getBytes()).toString();
    }

}
