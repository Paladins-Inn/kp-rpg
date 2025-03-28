/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti.
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

import de.kaiserpfalzedv.commons.api.resources.Pointer;
import de.kaiserpfalzedv.commons.core.resources.DefaultResourceSpecImpl;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Optional;

/**
 * The campaign data. It stores everything in properties so if is basically build from convenience methods for the
 * {@link DefaultResourceSpec}.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-02-06
 */
@Jacksonized
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(name = "CampaignData", description = "The data for a multiple game spanning campaign.")
public class CampaignData extends DefaultResourceSpecImpl {
    public static String CAMPAIGN_GM = "campaign.gm";
    public static String CAMPAIGN_PLAYERS = "campaign.players";
    public static String DISCORD_CHANNEL = "discord.channel";
    public static String DISCORD_GUILD = "discord.guild";
    public static String GAMES = "games";

    public static String[] STRUCTURED_PROPERTIES = {
            CAMPAIGN_GM,
            CAMPAIGN_PLAYERS,
            DISCORD_GUILD,
            DISCORD_CHANNEL,
            GAMES
    };


    @Override
    public String[] getDefaultProperties() {
        return STRUCTURED_PROPERTIES;
    }


    @JsonIgnore
    public Optional<Pointer> getGameMaster() {
        return getResourcePointer(CAMPAIGN_GM);
    }

    @JsonIgnore
    public List<Pointer> getPlayers() {
        return getResourcePointers(CAMPAIGN_PLAYERS);
    }

    @JsonIgnore
    public Optional<Pointer> getDiscordChannel() {
        return getResourcePointer(DISCORD_CHANNEL);
    }

    @JsonIgnore
    public Optional<Pointer> getDiscordGuild() {
        return getResourcePointer(DISCORD_GUILD);
    }

    @JsonIgnore
    public List<Pointer> getGames() {
        return getResourcePointers(GAMES);
    }
}
