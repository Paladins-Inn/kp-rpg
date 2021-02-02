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

package de.kaiserpfalzedv.rpg.integrations.discord.guilds;

import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceMetadata;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * GuildProvider -- handles the storage of Guild settings.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-30
 */
@ApplicationScoped
public class GuildProvider {
    private static final Logger LOG = LoggerFactory.getLogger(GuildProvider.class);

    @Inject
    GuildStoreService store;

    @PostConstruct
    public void logCreation() {
        LOG.info("GuildProvider using store: {}", store);
    }


    @CacheInvalidate(cacheName = "discord-guilds")
    public void store(final Guild data) {
        store.save(data);
    }

    @NotNull
    private ImmutableGuild increaseGeneration(final Guild data) {
        return ImmutableGuild.builder()
                .from(data)
                .metadata(
                        ImmutableResourceMetadata.builder()
                                .from(data.getMetadata())
                                .generation(data.getMetadata().getGeneration() + 1)
                                .build()
                )
                .build();
    }

    /**
     * Loads a guild setup information from the store. If there is no setup, create one and store it.
     *
     * @param name Name of the guild.
     * @return the setup information.
     */
    @CacheResult(cacheName = "discord-guilds")
    public Guild retrieve(final String name) {
        Optional<Guild> data = store.findByNameSpaceAndName(Guild.DISCORD_NAMESPACE, name);


        data.ifPresent(guild -> LOG.debug("Loaded guild: guild={}", guild));
        return data.orElseGet(() -> generateNewGuildEntry(name));
    }

    @NotNull
    private Guild generateNewGuildEntry(final String name) {
        Guild result = ImmutableGuild.builder()
                .metadata(
                        ImmutableResourceMetadata.builder()
                                .kind(Guild.KIND)
                                .apiVersion(Guild.API_VERSION)

                                .namespace(Guild.DISCORD_NAMESPACE)
                                .name(name)
                                .uid(UUID.randomUUID())
                                .created(OffsetDateTime.now(Clock.systemUTC()))

                                .build()
                )
                .build();
        store(result);

        LOG.info("Created: guild={}", result);
        return result;
    }
}
