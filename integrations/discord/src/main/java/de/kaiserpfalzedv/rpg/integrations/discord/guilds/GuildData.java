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
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

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
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = GuildData.GuildDataBuilder.class)
@Schema(name = "guildData", description = "The data for a guild (server) within Discord.")
public class GuildData implements Serializable {
    public static String DEFAULT_PREFIX = "tb!";

    @Schema(name = "adminRoles", description = "The roles needed for being seen as admin.")
    @Builder.Default
    private final List<String> adminRoles = new ArrayList<>();

    @Schema(name = "properties", description = "Configuration properties")
    @Builder.Default
    private final Map<String, String> properties = new HashMap<>();

    @Schema(name = "prefix", description = "The global prefix to use in this discord guild.")
    @Builder.Default
    private final String prefix = DEFAULT_PREFIX;
}
