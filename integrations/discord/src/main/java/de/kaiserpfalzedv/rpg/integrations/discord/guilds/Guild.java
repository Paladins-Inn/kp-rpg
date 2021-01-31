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
import de.kaiserpfalzedv.rpg.core.resources.Resource;
import de.kaiserpfalzedv.rpg.core.user.ImmutableUser;
import de.kaiserpfalzedv.rpg.core.user.UserData;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

/**
 * Guild -- A "server" within Discord.
 *
 * This data is used for customizing the bot and checking for permissions to use certain functions.
 */
@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableGuild.class)
@JsonDeserialize(builder = ImmutableGuild.Builder.class)
@Schema(name = "guild", description = "A single guild (server) within discord.")
public interface Guild extends Resource<GuildData> {
    String API_VERSION = "v1";
    String KIND = "User";

    String DISCORD_NAMESPACE = "discord";
}