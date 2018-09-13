package com.chompchompfig.linkshortener.rest;

import com.chompchompfig.linkshortener.domain.FixtureFactory;
import com.chompchompfig.linkshortener.domain.ShortLink;
import com.chompchompfig.linkshortener.domain.ShortLinkRepository;
import com.chompchompfig.linkshortener.infrastructure.rest.ShortLinkController;
import com.chompchompfig.linkshortener.tools.JsonPathTools;
import com.chompchompfig.linkshortener.tools.ShortLinkResourceMatcher;
import com.chompchompfig.linkshortener.tools.ShortLinkResourcesMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.UriTemplate;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.chompchompfig.linkshortener.domain.FixtureFactory.*;
import static com.chompchompfig.linkshortener.infrastructure.rest.ShortLinkController.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ShortLinkController.class)
@ContextConfiguration(classes=RestContextConfiguration.class)
public class ShortLinkControllerTests {

    public static final Integer VISITS_INCREMENT = Integer.valueOf(1);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShortLinkRepository shortLinkRepository;
    private FixtureFactory fixtureFactory = new FixtureFactory();
    private JsonPathTools jsonPathTools = new JsonPathTools();

    @Test
    public void apiShortLinksShouldReturn200OkAndEmptyListBodyWhenNoShortLinks() throws Exception {
        setUpShortLinkRepositoryReturnAll(Collections.emptyList());
        this.mockMvc.perform(get(API_SHORTLINKS_URI)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(content().json("{}"));
    }

    @Test
    public void apiShortLinksShouldReturn200OkAndNonEmptyListBodyWhenShortLinks() throws Exception {
        setUpShortLinkRepositoryReturnAll(fixtureFactory.newValidShortLinks());
        this.mockMvc.perform(get(API_SHORTLINKS_URI)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$._embedded['shortLinks'].length()").value(2))
                .andExpect(new ShortLinkResourcesMatcher(fixtureFactory.newValidShortLinkResources()));
    }

    @Test
    public void apiShortLinksShouldReturn200OkAndShortLinkBodyWhenPostedWithURL() throws Exception {
        this.mockMvc.perform(post(API_SHORTLINKS_URI).content(SOME_VALID_LONG_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(new ShortLinkResourceMatcher(SOME_VALID_SHORTLINK_ID, SOME_VALID_LONG_URL));

        ArgumentCaptor<ShortLink> shortLinkArgumentCaptor = ArgumentCaptor.forClass(ShortLink.class);
        Mockito.verify(shortLinkRepository, Mockito.times(1)).save(shortLinkArgumentCaptor.capture());
        ShortLink savedShortLink = shortLinkArgumentCaptor.getValue();
        assertEquals(SOME_VALID_LONG_URL, savedShortLink.getLongURL());
    }

    @Test
    public void apiShortLinksShouldReturn400BadDataWhenPostedWithInvalidURL() throws Exception {
        this.mockMvc.perform(post(API_SHORTLINKS_URI).content(SOME_INVALID_LONG_URL))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void apiShortLinksIdsShouldReturn200OKAndEmptyWhenNonExistentIds() throws Exception {
        setUpShortLinkRepositoryReturnEmptyForNonExistentIds(NON_EXISTENT_SHORTLINKS_IDS);
        this.mockMvc.perform(get(getEffectiveUri(API_SHORTLINKS_IDS_URI, NON_EXISTENT_SHORTLINKS_IDS_STRING)))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(content().json("{}", true));
        Mockito.verify(shortLinkRepository, Mockito.times(1)).findByIds(NON_EXISTENT_SHORTLINKS_IDS);
    }

    @Test
    public void apiShortLinksIdsShouldReturn200OKAndShortLinksWhenExistentIds() throws Exception {
        setUpShortLinkRepositoryReturnShortLinksForIds(EXISTENT_AND_NON_EXISTENT_SHORTLINKS_IDS,
                Arrays.asList(fixtureFactory.newValidShortLink()));
        this.mockMvc.perform(get(getEffectiveUri(API_SHORTLINKS_IDS_URI,
                EXISTENT_AND_NON_EXISTENT_SHORTLINKS_IDS_STRING)))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(new ShortLinkResourcesMatcher(fixtureFactory.newShortLinkResources()));
        Mockito.verify(shortLinkRepository, Mockito.times(1)).findByIds(EXISTENT_AND_NON_EXISTENT_SHORTLINKS_IDS);
    }

    @Test
    public void apiShortLinkIdShouldReturn404NotFoundWhenNonExistentShortLink() throws Exception {
        setUpShortLinkRepositoryReturnEmptyForNonExistentId(VALID_SHORTLINK_ID);
        this.mockMvc.perform(
                get(getEffectiveUri(API_SHORTLINK_ID_URI, VALID_SHORTLINK_ID)))
                    .andExpect(status().isNotFound());
    }

    @Test
    public void apiShortLinkIdShouldReturn200AndValidShortLinkWhenExistentShortLink() throws Exception {
        setUpShortLinkRepositoryReturnShortLinkForId(
                fixtureFactory.newValidShortLink(), VALID_SHORTLINK_ID);
        this.mockMvc.perform(
                get(getEffectiveUri(API_SHORTLINK_ID_URI, VALID_SHORTLINK_ID)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
                    .andExpect(new ShortLinkResourceMatcher(VALID_SHORTLINK_ID, VALID_LONG_URL));
    }

    @Test
    public void slashIdShouldReturn301MovedPermanentlyWhenExistentShortLinkId() throws Exception {
        setUpShortLinkRepositoryReturnShortLinkForId(fixtureFactory.newValidShortLink(), VALID_SHORTLINK_ID);
        String slashIdUri = getEffectiveUri(SLASH_ID_URI, VALID_SHORTLINK_ID);
        this.mockMvc.perform(get(slashIdUri)).andExpect(status().isMovedPermanently())
                .andExpect(header().string(HttpHeaders.LOCATION, VALID_LONG_URL))
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, CacheControl.noStore().getHeaderValue()));
        assertVisitsAreIncremented();
    }

    private void assertVisitsAreIncremented() {
        ArgumentCaptor<Integer> shortLinkAddVisitsNumberArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> shortLinkAddVisitsIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(shortLinkRepository, Mockito.times(1)).addVisits(
                shortLinkAddVisitsIdArgumentCaptor.capture(), shortLinkAddVisitsNumberArgumentCaptor.capture());
        assertEquals(VALID_SHORTLINK_ID, shortLinkAddVisitsIdArgumentCaptor.getValue());
        assertEquals(VISITS_INCREMENT, shortLinkAddVisitsNumberArgumentCaptor.getValue());
    }

    @Test
    public void slashIdShouldReturn404NotFoundWhenNonExistentShortLinkId() throws Exception {
        setUpShortLinkRepositoryReturnEmptyForNonExistentId(VALID_SHORTLINK_ID);
        String slashIdUri = getEffectiveUri(SLASH_ID_URI, VALID_SHORTLINK_ID);
        this.mockMvc.perform(get(slashIdUri)).andExpect(status().isNotFound());
    }

    private String getEffectiveUri(String uriTemplatePattern, Object ...variables) {
        UriTemplate uriTemplate = new UriTemplate(uriTemplatePattern);
        String effectiveUri = uriTemplate.expand(variables).toString();
        return effectiveUri;
    }

    private void setUpShortLinkRepositoryReturnAll(Collection<ShortLink> shortLinks) {
        Mockito.when(shortLinkRepository.findAll()).thenReturn(shortLinks);
    }

    private void setUpShortLinkRepositoryReturnShortLinkForId(ShortLink returnedShortLink, String shortlinkId) {
        Optional<ShortLink> fetchedShortLink = Optional.ofNullable(returnedShortLink);
        Mockito.when(shortLinkRepository.findById(shortlinkId)).thenReturn(fetchedShortLink);
    }

    private void setUpShortLinkRepositoryReturnEmptyForNonExistentId(String shortlinkId) {
        Mockito.when(shortLinkRepository.findById(shortlinkId)).thenReturn(Optional.empty());
    }

    private void setUpShortLinkRepositoryReturnEmptyForNonExistentIds(Collection<String> shortLinksIds) {
        Mockito.when(shortLinkRepository.findByIds(shortLinksIds)).thenReturn(Collections.emptyList());
    }

    private void setUpShortLinkRepositoryReturnShortLinksForIds(Collection<String> shortLinksIds, Collection<ShortLink> shortLinks) {
        Mockito.when(shortLinkRepository.findByIds(shortLinksIds)).thenReturn(shortLinks);
    }
}
