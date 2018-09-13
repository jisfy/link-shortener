package com.chompchompfig.linkshortener.domain;

import java.util.Collection;
import java.util.Optional;

/**
 * A Short Link Repository, as described in Domain Driven Design. This is, a class to retrieve ShortLinks from
 * a secondary store
 */
public interface ShortLinkRepository {

    /**
     * Gets the ShortLink corresponding to the given identifier
     * @param id <p>the identifier of the ShortLink to retrieve</p>
     * @return <p>either a ShortLink corresponding to the given identifier, or Empty if it does not exist</p>
     */
    Optional<ShortLink> findById(String id);

    /**
     * Gets a Collection of all ShortLinks available from the secondary store
     * @return <p>a Collection of all ShortLinks available in the application</p>
     */
    Collection<ShortLink> findAll();

    /**
     * Gets a Collection of the ShortLinks requested by their identifiers from the secondary store
     * @return <p>a Collection of all requested ShortLinks</p>
     */
    Collection<ShortLink> findByIds(Collection<String> ids);

    /**
     * Saves the given ShortLink to a secondary store
     * @param shortLink <p>the ShortLink to save</p>
     */
    void save(ShortLink shortLink);

    /**
     * Increments the number of visits for the given Short Link
     * @param id <p>the identifier of the ShortLink whose visits we would like to increment</p>
     * @param visits <p>the number of visits to add</p>
     */
    void addVisits(String id, int visits);
}
