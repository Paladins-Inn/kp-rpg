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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The basic data for every guild.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableGuildData.class)
@JsonDeserialize(builder = ImmutableGuildData.Builder.class)
@Schema(name="guildData", description = "The data for a guild (server) within Discord.")
public interface GuildData extends Serializable {
    String DEFAULT_PREFIX = "tb!";

    /**
     * getPrefix -- The default prefix for this guild.
     * <p>
     * Will be {@value #DEFAULT_PREFIX} ({@link #DEFAULT_PREFIX}) by default but can
     * be redefined. A plugin can decide to take this prefix or roll with another (like the dice rolling plugin which
     * uses "/r " instead to mimic <a href="https://roll20.net">Roll20</a>.
     *
     * @return the prefix for this guild.
     */
    @Schema(name = "prefix", description = "The global prefix to use in this discord guild.")
    @Value.Default
    default String getPrefix() {
        return DEFAULT_PREFIX;
    }


    /**
     * @return list of role names which are considered administrators for this guild.
     */
    @Value.Default
    default List<String> getAdminRoles() {
        return new ArrayList<>();
    }

    /**
     * @return Hashmap of configuration properties.
     */
    @Value.Default
    default Map<String, String> getProperties() {
        return new HashMap<>();
    }
}
