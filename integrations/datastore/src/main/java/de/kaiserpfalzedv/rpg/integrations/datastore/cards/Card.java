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

import de.kaiserpfalzedv.rpg.core.cards.BasicCardData;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ResourceStatus;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.Optional;
import java.util.UUID;

/**
 * Card -- The entity for storing the card resource.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-09
 */
@MongoEntity(collection = "cards")
public class Card extends PanacheMongoEntityBase implements de.kaiserpfalzedv.rpg.core.cards.Card {
    /** ID of the document. */
    @BsonId
    public UUID uid;

    /** The resource meta data. */
    public ResourceMetadata metadata;

    /** The data of the card. */
    public Optional<BasicCardData> spec;

    /** The status of the resource. */
    public Optional<ResourceStatus<String>> status;

    public Card() {}

    public Card(final de.kaiserpfalzedv.rpg.core.cards.Card orig) {
        uid = orig.getMetadata().getUid();
        metadata = orig.getMetadata();
        spec = orig.getSpec();
        status = orig.getStatus();
    }


    @BsonIgnore
    @Transient
    @Override
    public ResourceMetadata getMetadata() {
        return metadata;
    }

    @BsonIgnore
    @Transient
    @Override
    public Optional<BasicCardData> getSpec() {
        return spec;
    }

    @BsonIgnore
    @Transient
    @Override
    public Optional<ResourceStatus<String>> getStatus() {
        return status;
    }
}
