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

import de.kaiserpfalzedv.rpg.integrations.datastore.resources.MongoResource;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.ImmutableGuild;
import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.StringJoiner;

/**
 * The stored guild data.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-31
 */
@MongoEntity(collection = "guilds")
public class MongoGuild extends MongoResource<Guild> {
    /**
     * The data of the card.
     */
    public MongoGuildData spec;


    public MongoGuild() {
    }

    public MongoGuild(final Guild orig) {
        data(orig);
    }

    @BsonIgnore
    @Transient
    @Override
    public void data(final Guild orig) {
        super.data(orig);

        if (orig.getSpec().isPresent()) spec = new MongoGuildData(orig.getSpec().get());
    }

    @BsonIgnore
    @Transient
    @Override
    public Guild data() {
        ImmutableGuild.Builder result = ImmutableGuild.builder()
                .metadata(metadata.data());

        if (spec != null) result.spec(spec.data());
        if (status != null) result.status(status.data());

        return result.build();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MongoGuild.class.getSimpleName() + "[", "]")
                .add("hash=" + System.identityHashCode(this))
                .add("uid=" + uid)
                .add("nameSpace='" + nameSpace + "'")
                .add("name='" + name + "'")
                .toString();
    }
}
