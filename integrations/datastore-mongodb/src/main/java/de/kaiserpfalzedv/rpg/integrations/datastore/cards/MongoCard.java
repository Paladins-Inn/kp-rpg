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
import de.kaiserpfalzedv.rpg.core.cards.ImmutableCard;
import de.kaiserpfalzedv.rpg.integrations.datastore.resources.MongoResource;
import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.StringJoiner;

/**
 * Card -- The entity for storing the card resource.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-09
 */
@MongoEntity(collection = "cards")
public class MongoCard extends MongoResource<Card> {
    /**
     * The data of the card.
     */
    public MongoCardData spec;

    public MongoCard() {
    }

    public MongoCard(final Card orig) {
        data(orig);
    }

    @BsonIgnore
    @Transient
    @Override
    public void data(final Card orig) {
        super.data(orig);

        if (orig.getSpec().isPresent()) {
            spec = new MongoCardData(orig.getSpec().get());
        }
    }

    @BsonIgnore
    @Transient
    @Override
    public Card data() {
        ImmutableCard.Builder result = ImmutableCard.builder()
                .metadata(metadata.data(id));

        if (spec != null) result.spec(spec.data());

        return result.build();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MongoCard.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .add("uid=" + uid)
                .add("nameSpace='" + nameSpace + "'")
                .add("name='" + name + "'")
                .toString();
    }
}
