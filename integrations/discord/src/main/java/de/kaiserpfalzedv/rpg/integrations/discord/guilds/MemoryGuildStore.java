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

import de.kaiserpfalzedv.rpg.core.store.GenericStoreService;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildStoreService;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.ImmutableGuild;
import io.quarkus.arc.AlternativePriority;

import javax.enterprise.context.ApplicationScoped;

/**
 * MemoryGuildStore -- an ephemeral store for Guild settings.
 *
 * This is a memory alternative for a persistent data store for Guild settings.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
@ApplicationScoped
@AlternativePriority(9000)
public class MemoryGuildStore extends GenericStoreService<Guild> implements GuildStoreService {
    @Override
    public Guild increaseGeneration(final Guild data) {
        return ImmutableGuild.builder()
                .from(data)
                .metadata(
                        increaseGeneration(data.getMetadata())
                )
                .build();
    }
}
