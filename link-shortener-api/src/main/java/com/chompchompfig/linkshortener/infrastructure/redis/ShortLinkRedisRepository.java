package com.chompchompfig.linkshortener.infrastructure.redis;

import java.util.Optional;
import java.util.Collection;

import com.google.common.collect.Lists;
import com.chompchompfig.linkshortener.domain.ShortLink;
import com.chompchompfig.linkshortener.domain.ShortLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * A Redis implementation of a ShortLinkRepository. This implementation helps decouple the ShortLinkRepository in
 * the domain model from its underlying store technology
 */
public class ShortLinkRedisRepository implements ShortLinkRepository {

    private ShortLinkCrudRepository shortLinkCrudRepository;
    private RedisTemplate<String, ShortLink> redisTemplate;

    @Autowired
    public ShortLinkRedisRepository(ShortLinkCrudRepository shortLinkCrudRepository,
                                    RedisTemplate<String, ShortLink> redisTemplate) {
        this.shortLinkCrudRepository = shortLinkCrudRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Gets a ShortLink for the given identifier
     * @param id <p>the ShortLink identifier to fetch</p>
     * @return <p>either the ShortLink for the given identifier or Empty</p>
     */
    @Override
    public Optional<ShortLink> findById(String id) {
        return shortLinkCrudRepository.findById(id);
    }

    /**
     * Gets all the ShortLinks available in the application
     * @return <p>a Collection of all ShortLinks available</p>
     */
    @Override
    public Collection<ShortLink> findAll() {
        return Lists.newArrayList(shortLinkCrudRepository.findAll());
    }

    /**
     * Gets all the ShortLinks requested by their identifiers
     * @return <p>a Collection of all requested ShortLinks </p>
     */
    @Override
    public Collection<ShortLink> findByIds(Collection<String> ids) {
        return Lists.newArrayList(shortLinkCrudRepository.findAllById(ids));
    }

    /**
     * Saves the given ShortLink
     * @param shortLink <p>the ShortLink to persist</p>
     */
    @Override
    public void save(ShortLink shortLink) {
        shortLinkCrudRepository.save(shortLink);
    }

    /**
     * Increments the number of visits of the given ShortLink by the given number
     * @param id <p>the identifier of the ShortLink whose visits we would like to increment</p>
     * @param visits <p>the number of visits to add</p>
     */
    @Override
    public void addVisits(String id, int visits) {
        redisTemplate.opsForHash().increment(
                ShortLink.SHORTLINK_KEY_PREFIX + ":" + id, ShortLink.VISITS_FIELD_NAME, 1);
    }
}
