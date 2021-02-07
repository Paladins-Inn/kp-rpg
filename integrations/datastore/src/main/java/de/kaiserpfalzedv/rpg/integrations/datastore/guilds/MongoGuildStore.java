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

import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceHistory;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceStatus;
import de.kaiserpfalzedv.rpg.integrations.datastore.resources.MongoResourceStore;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildStoreService;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.ImmutableGuild;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.StartupEvent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * MongoGuildRepository -- The persistent datastore for guild configurations.
 */
@ApplicationScoped
public class MongoGuildStore extends MongoResourceStore<Guild, MongoGuild> implements GuildStoreService, PanacheMongoRepository<MongoGuild> {


    public void startUp(@Observes StartupEvent event) {
        LOG.info("started: {}", this);
    }

    @PostConstruct
    @Override
    public MongoGuildStore setUp() {
        super.setUp();

        return this;
    }

    @PreDestroy
    @Override
    public void tearDown() {
        super.tearDown();
    }

    public MongoGuild empty() {
        return new MongoGuild();
    }

    @Override
    public PanacheQuery<MongoGuild> query(final String query, final Parameters parameters) {
        PanacheQuery<MongoGuild> result = find(query, parameters);

        LOG.trace("query: query='{}', count={}", query, result.count());
        return result;
    }


    @Override
    public Optional<Guild> findByNameSpaceAndName(final String nameSpace, final String name) {
        Optional<Guild> result = super.findByNameSpaceAndName(nameSpace, name);

        return repairKind(result);
    }

    @Override
    public Optional<Guild> findByUid(final UUID uid) {
        Optional<Guild> result = super.findByUid(uid);

        return repairKind(result);
    }

    private Optional<Guild> repairKind(Optional<Guild> input) {
        if (input.isEmpty() || Guild.KIND.equals(input.get().getKind())) {
            LOG.trace("Do not rewrite: guild={}", input.orElse(null));
            return input;
        }

        LOG.trace("Rewriting: guild={}", input.get());

        long generation = input.get().getGeneration() + 1;
        Guild output = ImmutableGuild.builder()
                .from(input.get())
                .metadata(
                        ImmutableResourceMetadata.builder()
                                .from(input.get().getMetadata())
                                .kind(Guild.KIND)
                                .generation(generation)
                                .build()
                )
                .status(
                        ImmutableResourceStatus.builder()
                                .from(input.get().getStatus().orElse(
                                        ImmutableResourceStatus.builder()
                                                .observedGeneration(input.get().getGeneration())
                                                .build()
                                ))
                                .observedGeneration(generation)
                                .addHistory(
                                        ImmutableResourceHistory.builder()
                                                .status("repaired")
                                                .message("Rewrote kind of this resource from 'User' to 'Guild'.")
                                                .timeStamp(OffsetDateTime.now(Clock.systemUTC()))
                                                .build()
                                )
                                .build()
                )
                .build();

        MongoGuild data = new MongoGuild(output);
        update(data);

        LOG.debug("Returning rewritten: guild={}", output);
        return Optional.of(output);
    }
}
