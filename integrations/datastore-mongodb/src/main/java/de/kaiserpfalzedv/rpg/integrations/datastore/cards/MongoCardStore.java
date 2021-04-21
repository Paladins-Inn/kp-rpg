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
import de.kaiserpfalzedv.rpg.integrations.datastore.resources.MongoResourceStore;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MongoCardStore extends MongoResourceStore<Card, MongoCard> implements CardStoreService {
    @Override
    public MongoCard empty() {
        return new MongoCard();
    }

    @Override
    public PanacheQuery<MongoCard> query(final String query, final Parameters parameters) {
        PanacheQuery<MongoCard> result = find(query, parameters);

        LOG.trace("query: query='{}', count={}", query, result.count());
        return result;
    }
}
