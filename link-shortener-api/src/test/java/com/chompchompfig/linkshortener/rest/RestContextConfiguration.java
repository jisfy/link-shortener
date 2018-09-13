package com.chompchompfig.linkshortener.rest;

import com.chompchompfig.linkshortener.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("com.chompchompfig.linkshortener.infrastructure.rest")
@Configuration
public class RestContextConfiguration {

    public static final String SHORT_LY_BASE_URL = "http://short.ly";

    private FixtureFactory fixtureFactory = new FixtureFactory();

    @Bean
    public ShortLinkIdGenerationService shortLinkIdGenerationStrategy() {
        ShortLinkIdGenerationService shortLinkIdGenerationService = fixtureFactory.newShortLinkIdGenerationStrategy();
        return shortLinkIdGenerationService;
    }

    @Bean
    public Shortener appendHashBasedShortLinkShortener(ShortLinkRepository shortLinkRepository) {
        return new Shortener(shortLinkRepository, new HashingShortLinkIdGenerationService());
    }

    @Bean
    public ShortLinkFormattingService simpleShortLinkFormatter() {
        return new SimpleShortLinkFormattingService(SHORT_LY_BASE_URL);
    }

    @Bean
    public ShortLinkRepository shortLinkRepository() {
        ShortLinkRepository shortLinkRepository = fixtureFactory.newShortLinkRepository();
        return shortLinkRepository;
    }
}

