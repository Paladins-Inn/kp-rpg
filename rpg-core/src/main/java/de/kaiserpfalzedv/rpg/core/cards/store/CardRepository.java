/*
 * Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.core.cards.store;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class CardRepository implements PanacheMongoRepository<Card> {
    private static final Logger LOG = LoggerFactory.getLogger(CardRepository.class);

    public Optional<Card> findByNameSpaceAndName(final String nameSpace, final String name) {
        LOG.trace("loading card: nameSpace={}, name={}", nameSpace, name);

        Map<String, String> queryParams = new HashMap<>(2);
        queryParams.put("nameSpace", nameSpace);
        queryParams.put("name", name);
        PanacheQuery<Card> query = Card.find("nameSpace = :nameSpace and name = :name", Sort.descending("generation"), queryParams);

        Card result = query.firstResult();
        LOG.debug("Loaded: {}", result);
        return Optional.ofNullable(result);
    }
}
