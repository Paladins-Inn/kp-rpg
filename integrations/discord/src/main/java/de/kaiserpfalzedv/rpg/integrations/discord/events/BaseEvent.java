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
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(name = "BaseEvent", description = "The base event for discord events.")
public abstract class BaseEvent {
    /**
     * The message id of the event.
     */
    @EqualsAndHashCode.Include
    @Schema(name = "MessageId", description = "The unique id of the event.")
    private String id;

    /**
     * The response sequence number within discord.
     */
    @EqualsAndHashCode.Include
    @Schema(name = "ResponseSequence", description = "The response sequence number of this event.")
    private Long responseNumber;

    /**
     * The guild this event was generated in (if any).
     */
    @Schema(name = "guild", description = "The guild the event is generated in (if any)", nullable = true)
    private Optional<Guild> guild;

    /**
     * The user for who the event has been created.
     */
    @Schema(name = "user", description = "The user for whom the event has been created (if any)", nullable = true)
    private Optional<User> user;

    /**
     * The timestamp of this event.
     */
    @Schema(name = "timestamp", description = "The timestamp of the event")
    private OffsetDateTime timestamp;
}
