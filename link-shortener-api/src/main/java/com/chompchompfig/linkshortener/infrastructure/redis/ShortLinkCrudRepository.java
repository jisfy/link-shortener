package com.chompchompfig.linkshortener.infrastructure.redis;

import com.chompchompfig.linkshortener.domain.ShortLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * A Spring Data, ShortLink Crud Repository
 */
@Repository
public interface ShortLinkCrudRepository extends CrudRepository<ShortLink, String> {
}
