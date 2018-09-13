package com.chompchompfig.linkshortener;

import com.chompchompfig.linkshortener.domain.*;
import com.chompchompfig.linkshortener.infrastructure.redis.ShortLinkCrudRepository;
import com.chompchompfig.linkshortener.infrastructure.redis.ShortLinkRedisRepository;
import com.chompchompfig.linkshortener.domain.*;
import com.chompchompfig.linkshortener.infrastructure.rest.ShortLinkController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

/**
 * The Spring Boot entry point for the ShortLink RESTful application. This class also holds the Spring Configuration
 * that will be used for Bean creation and auto wiring
 */
@EnableRedisRepositories
@SpringBootApplication
public class LinkShortenerApplication {

	public static final String JEDIS_HOST_PROPERTY_NAME = "jedis.host";
	public static final String JEDIS_PORT_PROPERTY_NAME = "jedis.port";
    public static final String JEDIS_DEFAULT_HOSTNAME = "local	host";
	public static final int JEDIS_DEFAULT_PORT = 6379;

    public static final String SERVER_ADDRESS_PROPERTY_NAME = "server.address";
    public static final String SERVER_PORT_PROPERTY_NAME = "server.port";
    public static final String HTTP_PROTOCOL_SCHEME = "http";
    public static final String DEFAULT_HOST_ADDRESS = "localhost";

    public static final String UI_TARGET_FORWARD_URI = "forward:/ui/index.html";
    public static final String HAL_BROWSER_TARGET_REDIRECT_URI = "redirect:/hal-browser/browser.html#/api";

    public static final String DOCS_RELATION_URL_TEMPLATE = "/docs/rel_{rel}.html";
    public static final String HAL_BROWSER_URI_PATH = "/hal-browser";
    public static final String UI_URI_PATH = "/ui";

    public static void main(String[] args) {
		SpringApplication.run(LinkShortenerApplication.class, args);
	}

	@Bean
	public RedisConnectionFactory jedisConnectionFactory(Environment environment) {
		String jedisHost = getJedisHostName(environment);
		Integer jedisPort = getJedisPort(environment);
		RedisStandaloneConfiguration jedisConfiguration = new RedisStandaloneConfiguration(jedisHost, jedisPort);
		JedisConnectionFactory jedisConnectionFactory =  new JedisConnectionFactory();
		return jedisConnectionFactory;
	}

    private Integer getJedisPort(Environment environment) {
        return Optional.ofNullable(
                environment.getProperty(JEDIS_PORT_PROPERTY_NAME, Integer.class)).orElse(JEDIS_DEFAULT_PORT);
    }

    private String getJedisHostName(Environment environment) {
        return Optional.ofNullable(
                environment.getProperty(JEDIS_HOST_PROPERTY_NAME)).orElse(JEDIS_DEFAULT_HOSTNAME);
    }

    @Bean
    public Shortener appendHashBasedShortLinkShortener(ShortLinkRepository shortLinkRepository) {
	    return new Shortener(shortLinkRepository, new HashingShortLinkIdGenerationService());
    }

    @Bean
    public ShortLinkFormattingService simpleShortLinkFormatter(Environment environment) {
	    String serverAddress =
                Optional.ofNullable(environment.getProperty(SERVER_ADDRESS_PROPERTY_NAME)).orElse(DEFAULT_HOST_ADDRESS);
	    String serverPort = environment.getProperty(SERVER_PORT_PROPERTY_NAME);
	    String baseUrl = HTTP_PROTOCOL_SCHEME + "://" + serverAddress + ":" + serverPort +
				ShortLinkController.VISIT_SHORTLINK_URI_PREFIX;
	    return new SimpleShortLinkFormattingService(baseUrl);
    }

    @Bean
    public ShortLinkRepository shortLinkRedisRepository(ShortLinkCrudRepository shortLinkCrudRepository,
														RedisTemplate redisTemplate) {
	    return new ShortLinkRedisRepository(shortLinkCrudRepository, redisTemplate);
    }

	@Bean
	public WebMvcConfigurer forwardToIndex() {
		return new WebMvcConfigurer() {
			@Override
			public void addViewControllers(ViewControllerRegistry registry) {
				registry.addViewController(UI_URI_PATH).setViewName(UI_TARGET_FORWARD_URI);
				registry.addViewController(HAL_BROWSER_URI_PATH).setViewName(HAL_BROWSER_TARGET_REDIRECT_URI);
			}
		};
	}

	@Bean
	public RedisTemplate<String, ShortLink> redisTemplate(RedisConnectionFactory jedisConnectionFactory) {
		RedisTemplate<String, ShortLink> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory);
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setKeySerializer(new StringRedisSerializer());
		return template;
	}

	@Bean
	public CurieProvider curieProvider() {
		return new DefaultCurieProvider("ex", new UriTemplate(DOCS_RELATION_URL_TEMPLATE));
	}
}
