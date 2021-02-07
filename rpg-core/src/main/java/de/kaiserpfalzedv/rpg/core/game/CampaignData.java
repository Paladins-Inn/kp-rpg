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
 * The campaign data. It stores everything in properties so if is basically build from convenience methods for the
 * {@link DefaultResourceSpec}.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-02-06
 */
@SuppressWarnings("unused")
@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableCampaignData.class)
@JsonDeserialize(builder = ImmutableCampaignData.Builder.class)
@Schema(name = "CampaignData", description = "The data for a multiple game spanning campaign.")
public interface CampaignData extends DefaultResourceSpec {
    String CAMPAIGN_GM = "campaign.gm";
    String CAMPAIGN_PLAYERS = "campaign.players";
    String DISCORD_CHANNEL = "discord.channel";
    String DISCORD_GUILD = "discord.guild";
    String GAMES = "games";

    String[] STRUCTURED_PROPERTIES = {
            CAMPAIGN_GM,
            CAMPAIGN_PLAYERS,
            DISCORD_GUILD,
            DISCORD_CHANNEL,
            GAMES
    };


    @Override
    default String[] getDefaultProperties() {
        return STRUCTURED_PROPERTIES;
    }


    @Value.Default
    @Transient
    @JsonIgnore
    default Optional<ResourcePointer> getGameMaster() {
        return getResourcePointer(CAMPAIGN_GM);
    }

    @Value.Default
    @Transient
    @JsonIgnore
    default List<ResourcePointer> getPlayers() {
        return getResourcePointers(CAMPAIGN_PLAYERS);
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

    @Value.Default
    @Transient
    @JsonIgnore
    default List<ResourcePointer> getGames() {
        return getResourcePointers(GAMES);
    }
}
