package com.chompchompfig.linkshortener.tools;

import com.chompchompfig.linkshortener.infrastructure.rest.ShortLinkResource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ShortLinkResourceMatcher implements ResultMatcher {

    private JsonPathTools jsonPathTools = new JsonPathTools();
    private String shortLinkId;
    private String shortLinkLongUrl;

    public ShortLinkResourceMatcher(String shortLinkId, String shortLinkLongUrl) {
        this.shortLinkId = shortLinkId;
        this.shortLinkLongUrl = shortLinkLongUrl;
    }

    @Override
    public void match(MvcResult mvcResult) throws Exception {
        jsonPath(jsonPathTools.jsonPathForProperty(
                ShortLinkResource.LINKID_PROPERTY_NAME)).value(shortLinkId).match(mvcResult);
        jsonPath(jsonPathTools.jsonPathForProperty(
                ShortLinkResource.LONG_URL_PROPERTY_NAME)).value(shortLinkLongUrl).match(mvcResult);
    }
}
