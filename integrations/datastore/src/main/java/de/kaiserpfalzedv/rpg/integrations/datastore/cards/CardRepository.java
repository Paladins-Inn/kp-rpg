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

package de.kaiserpfalzedv.rpg.integrations.datastore.cards;

import de.kaiserpfalzedv.rpg.core.cards.Card;
import de.kaiserpfalzedv.rpg.core.cards.CardStoreService;
import de.kaiserpfalzedv.rpg.core.cards.ImmutableCard;
import de.kaiserpfalzedv.rpg.core.user.ImmutableUser;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
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
public class CardRepository implements CardStoreService, PanacheMongoRepository<MongoCard> {
    private static final Logger LOG = LoggerFactory.getLogger(CardRepository.class);

    @Override
    public Optional<Card> findByNameSpaceAndName(final String nameSpace, final String name) {
        LOG.trace("loading: type=card nameSpace={}, name={}", nameSpace, name);

        Map<String, String> queryParams = new HashMap<>(2);
        queryParams.put("nameSpace", nameSpace);
        queryParams.put("name", name);
        PanacheQuery<MongoCard> query = MongoCard.find("nameSpace = :nameSpace and name = :name", Sort.descending("generation"), queryParams);

        MongoCard result = query.firstResult();
        LOG.debug("Loaded: {}", result);
        return Optional.ofNullable(ImmutableCard.builder().from(result).build());
    }

    @Override
    public void persist(Card object) {
        LOG.trace("persisting: card={}", object);

        persistOrUpdate(new MongoCard(object));
    }
}
