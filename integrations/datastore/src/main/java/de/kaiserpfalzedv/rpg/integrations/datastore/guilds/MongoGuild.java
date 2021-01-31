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

package de.kaiserpfalzedv.rpg.integrations.datastore.guilds;

import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ResourceStatus;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildData;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.Optional;
import java.util.UUID;

/**
 * The stored user data.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-30
 */
@MongoEntity(collection = "users")
public class MongoGuild extends PanacheMongoEntityBase implements de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild {
    /** ID of the document. */
    @BsonId
    public UUID uid;

    /** The resource meta data. */
    public ResourceMetadata metadata;

    /** The data of the card. */
    public Optional<de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildData> spec;

    /** The status of the resource. */
    public Optional<ResourceStatus> status;


    public MongoGuild() {}

    public MongoGuild(final de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild orig) {
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
    public Optional<GuildData> getSpec() {
        return spec;
    }

    @BsonIgnore
    @Transient
    @Override
    public Optional<ResourceStatus> getStatus() {
        return status;
    }
}
