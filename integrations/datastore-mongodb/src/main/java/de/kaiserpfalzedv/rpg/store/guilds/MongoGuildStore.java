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
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ResourceStatus;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildStoreService;
import de.kaiserpfalzedv.rpg.store.resources.MongoResourceStore;
import io.quarkus.arc.AlternativePriority;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.StartupEvent;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MongoGuildRepository -- The persistent datastore for guild configurations.
 */
@ApplicationScoped
@AlternativePriority(500)
@Slf4j
public class MongoGuildStore extends MongoResourceStore<Guild, MongoGuild> implements GuildStoreService, PanacheMongoRepository<MongoGuild> {


    public void startUp(@Observes StartupEvent event) {
        log.info("started: {}", this);
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

        log.trace("query: query='{}', count={}", query, result.count());
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
            log.trace("Do not rewrite: guild={}", input.orElse(null));
            return input;
        }

        Guild output = rewriteGuild(input.orElseThrow());
        MongoGuild data = new MongoGuild(output);
        update(data);

        log.debug("Returning rewritten: guild={}", output);
        return Optional.of(output);
    }

    private Guild rewriteGuild(@NotNull final Guild input) {
        log.trace("Rewriting: guild={}", input);
        long generation = input.getGeneration() + 1;

        ResourceMetadata metadata = rewriteMetadata(input, generation);
        ResourceStatus status = addHistory(input, generation);

        return Guild.builder()
                .metadata(metadata)
                .spec(input.getSpec())
                .status(Optional.of(status))
                .build();
    }

    private ResourceMetadata rewriteMetadata(Guild input, long generation) {
        ResourceMetadata inputMetadata = input.getMetadata();
        return ResourceMetadata.builder()
                .kind(Guild.KIND)
                .namespace(inputMetadata.getNamespace())
                .name(inputMetadata.getName())
                .uid(inputMetadata.getUid())
                .generation(generation)

                .annotations(inputMetadata.getAnnotations())
                .labels(inputMetadata.getLabels())
                .owner(inputMetadata.getOwner())

                .created(inputMetadata.getCreated())
                .deleted(inputMetadata.getDeleted())

                .build();
    }

    private ResourceStatus addHistory(Guild input, long generation) {
        ResourceStatus inputStatus = input
                .getStatus()
                .orElse(ResourceStatus.builder().observedGeneration(input.getGeneration()).build());
        List<ResourceHistory> history = inputStatus.getHistory();
        history.add(ResourceHistory.builder()
                .status("repaired")
                .message(Optional.of("Rewrote kind of this resource from 'User' to 'Guild'."))
                .timeStamp(OffsetDateTime.now(Clock.systemUTC()))
                .build()
        );

        return ResourceStatus.builder()
                .observedGeneration(generation)
                .history(history)
                .build();
    }
}
