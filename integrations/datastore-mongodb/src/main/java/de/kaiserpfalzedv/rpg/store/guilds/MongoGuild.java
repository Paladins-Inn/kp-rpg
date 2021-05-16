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

package de.kaiserpfalzedv.rpg.store.guilds;

import de.kaiserpfalzedv.rpg.core.resources.ResourceHistory;
import de.kaiserpfalzedv.rpg.core.resources.ResourceStatus;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildData;
import de.kaiserpfalzedv.rpg.store.resources.MongoResource;
import io.quarkus.mongodb.panache.MongoEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * The stored guild data.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-31
 */
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@MongoEntity(collection = "guilds")
public class MongoGuild extends MongoResource<Guild> {
    /**
     * The data of the card.
     */
    public MongoGuildData spec;


    public MongoGuild(final Guild orig) {
        data(orig);
    }

    @BsonIgnore
    @Transient
    @Override
    public void data(final Guild orig) {
        super.data(orig);

        if (orig.getSpec() != null && orig.getSpec().isPresent())
            spec = new MongoGuildData(orig.getSpec().get());
    }

    @BsonIgnore
    @Transient
    @Override
    public Guild data() {
        GuildData.GuildDataBuilder data = GuildData.builder();
        ResourceStatus.ResourceStatusBuilder status = ResourceStatus.builder();

        if (spec != null) {
            data
                    .adminRoles(spec.adminRoles != null ? spec.adminRoles : new ArrayList<>())
                    .properties(spec.properties != null ? spec.properties : new HashMap<>())
                    .prefix(spec.prefix);
        }

        if (status != null) {
            List<ResourceHistory> history = new ArrayList<>();

            if (!this.status.history.isEmpty()) {
                this.status.history.forEach(h -> {
                    history.add(ResourceHistory.builder()
                            .status(h.status)
                            .timeStamp(h.timeStamp.timeStamp())
                            .message(Optional.ofNullable(h.message))
                            .build()
                    );
                });

                status
                        .observedGeneration(this.status.observedGeneration)
                        .history(history);
            }
        }

        return Guild.builder()
                .metadata(metadata.data(id))
                .spec(Optional.ofNullable(data.build()))
                .status(Optional.ofNullable(status.build()))
                .build();
    }
}
