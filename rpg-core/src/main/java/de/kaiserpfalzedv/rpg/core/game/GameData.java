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
import de.kaiserpfalzedv.rpg.core.resources.DefaultResourceSpec;
import de.kaiserpfalzedv.rpg.core.resources.ResourcePointer;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.beans.Transient;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The game data. It stores everything in properties so if is basically build from convenience methods for the
 * {@link DefaultResourceSpec}.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-02-06
 */
@SuppressWarnings("unused")
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(name = "GameData", description = "A game session data.")
public class GameData extends DefaultResourceSpec {
    public static String CAMPAIGN = "campaign";
    public static String GAME_GM = "game.gm";
    public static String GAME_PLAYERS = "game.players";
    public static String DISCORD_GUILD = "discord.guild";
    public static String DISCORD_CHANNEL = "discord.channel";

    public static String[] STRUCTURED_PROPERTIES = {
            CAMPAIGN,
            GAME_GM,
            GAME_PLAYERS,
            DISCORD_GUILD,
            DISCORD_CHANNEL
    };

    @Builder
    public GameData(
            final Map<String, String> properties,
            final ResourcePointer campaign,
            final ResourcePointer gameMaster,
            final List<ResourcePointer> players,
            final ResourcePointer discordGuild,
            final ResourcePointer discordChannel
    ) {
        super(properties);

        saveResourcePointer(CAMPAIGN, campaign);
        saveResourcePointer(GAME_GM, gameMaster);
        saveResourcePointers(GAME_PLAYERS, players);
        saveResourcePointer(DISCORD_GUILD, discordGuild);
        saveResourcePointer(DISCORD_CHANNEL, discordChannel);
    }

    @Override
    public String[] getDefaultProperties() {
        return STRUCTURED_PROPERTIES;
    }


    @Transient
    @JsonIgnore
    public Optional<ResourcePointer> getCampaign() {
        return getResourcePointer(CAMPAIGN);
    }


    @Transient
    @JsonIgnore
    public Optional<ResourcePointer> getGameMaster() {
        return getResourcePointer(GAME_GM);
    }


    @Transient
    @JsonIgnore
    public List<ResourcePointer> getPlayers() {
        return getResourcePointers(GAME_PLAYERS);
    }


    @Transient
    @JsonIgnore
    public Optional<ResourcePointer> getDiscordChannel() {
        return getResourcePointer(DISCORD_CHANNEL);
    }


    @Transient
    @JsonIgnore
    public Optional<ResourcePointer> getDiscordGuild() {
        return getResourcePointer(DISCORD_GUILD);
    }
}
