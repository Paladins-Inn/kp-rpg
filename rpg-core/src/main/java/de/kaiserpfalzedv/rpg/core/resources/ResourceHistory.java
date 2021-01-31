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

package de.kaiserpfalzedv.rpg.core.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Optional;


/**
 * A single history entry. Basic data is the timestamp, the status and the message.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0
 */
@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableResourceHistory.class)
@JsonDeserialize(builder = ImmutableResourceHistory.Builder.class)
@Schema(name = "ResourceHistory", description = "A single history entry of a change.")
public interface ResourceHistory extends Serializable {
    /**
     * @return Timestamp of this history entry.
     */
    @Schema(name = "TimeStamp", description = "The timestamp of the change.", required = true)
    OffsetDateTime getTimeStamp();

    /**
     * @return Status of the resource after this change.
     */
    @Schema(name = "Status", description = "The resource status after the change.", required = true)
    String getStatus();

    /**
     * @return Human readable message for this change (if any).
     */
    @Schema(name = "Message", description = "The human readable description of the change.")
    Optional<String> getMessage();
}
