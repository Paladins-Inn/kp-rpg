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

package de.kaiserpfalzedv.rpg.integrations.discord.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.Optional;

@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableBaseEvent.class)
@JsonDeserialize(builder = ImmutableBaseEvent.Builder.class)
@Schema(name = "BaseEvent", description = "The base event for discord events.")
public interface BaseEvent {
    /**
     * @return The message id of the event.
     */
    @Schema(name = "MessageId", description = "The id of the event.")
    String getId();

    /**
     * @return The response sequence number within discord.
     */
    @Schema(name = "ResponseSequence", description = "The response sequence number of this event.")
    Long getResponseNumber();

    /**
     * @return The guild this event was generated in (if any).
     */
    Optional<Guild> getGuild();

    /**
     * @return The user for who the event has been created.
     */
    Optional<User> getUser();

    /**
     * @return The timestamp of this event.
     */
    OffsetDateTime getTimestamp();
}
