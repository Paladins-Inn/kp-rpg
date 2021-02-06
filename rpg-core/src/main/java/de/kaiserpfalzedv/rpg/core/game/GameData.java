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

package de.kaiserpfalzedv.rpg.core.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.core.resources.DefaultResourceSpec;
import de.kaiserpfalzedv.rpg.core.resources.ResourcePointer;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

/**
 * The game data. It stores everything in properties so if is basically build from convenience methods for the
 * {@link DefaultResourceSpec}.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-02-06
 */
@SuppressWarnings("unused")
@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableGameData.class)
@JsonDeserialize(builder = ImmutableGameData.Builder.class)
@Schema(name = "GameData", description = "A game session data.")
public interface GameData extends DefaultResourceSpec {
    String CAMPAIGN = "campaign";
    String GAME_GM = "game.gm";
    String GAME_PLAYERS = "game.players";
    String DISCORD_GUILD = "discord.guild";
    String DISCORD_CHANNEL = "discord.channel";

    String[] STRUCTURED_PROPERTIES = {
            CAMPAIGN,
            GAME_GM,
            GAME_PLAYERS,
            DISCORD_GUILD,
            DISCORD_CHANNEL
    };


    @Override
    default String[] getDefaultProperties() {
        return STRUCTURED_PROPERTIES;
    }


    @Value.Default
    @Transient
    @JsonIgnore
    default Optional<ResourcePointer> getCampaign() {
        return getResourcePointer(CAMPAIGN);
    }

    @Value.Default
    @Transient
    @JsonIgnore
    default Optional<ResourcePointer> getGameMaster() {
        return getResourcePointer(GAME_GM);
    }

    @Value.Default
    @Transient
    @JsonIgnore
    default List<ResourcePointer> getPlayers() {
        return getResourcePointers(GAME_PLAYERS);
    }

    @Value.Default
    @Transient
    @JsonIgnore
    default Optional<ResourcePointer> getDiscordChannel() {
        return getResourcePointer(DISCORD_CHANNEL);
    }

    @Value.Default
    @Transient
    @JsonIgnore
    default Optional<ResourcePointer> getDiscordGuild() {
        return getResourcePointer(DISCORD_GUILD);
    }
}
