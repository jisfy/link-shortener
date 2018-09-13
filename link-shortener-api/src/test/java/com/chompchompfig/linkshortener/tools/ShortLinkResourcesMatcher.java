package com.chompchompfig.linkshortener.tools;

import com.chompchompfig.linkshortener.infrastructure.rest.ShortLinkResource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Collection;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ShortLinkResourcesMatcher implements ResultMatcher {

    private JsonPathTools jsonPathTools = new JsonPathTools();
    private Collection<ShortLinkResource> resourcesDescriptions;


    public ShortLinkResourcesMatcher(Collection<ShortLinkResource> resourcesDescriptions) {
        this.resourcesDescriptions = resourcesDescriptions;
    }

    @Override
    public void match(MvcResult mvcResult) throws Exception {
        int resourceIndex = 0;
        for (ShortLinkResource resourceDescription : resourcesDescriptions) {
            jsonPath(jsonPathTools.jsonPathForShortLinkPropertyInList(
                    resourceIndex, ShortLinkResource.LINKID_PROPERTY_NAME)).value(resourceDescription.getLinkId())
                            .match(mvcResult);
            jsonPath(jsonPathTools.jsonPathForShortLinkPropertyInList(
                    resourceIndex, ShortLinkResource.LONG_URL_PROPERTY_NAME)).value(resourceDescription.getLongURL())
                            .match(mvcResult);
            resourceIndex++;
        }
    }
}
