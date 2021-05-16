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
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;


/**
 * A single history entry. Basic data is the timestamp, the status and the message.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = ResourceHistory.ResourceHistoryBuilder.class)
@Schema(name = "ResourceHistory", description = "A single history entry of a change.")
public class ResourceHistory implements Serializable {
    @Schema(name = "TimeStamp", description = "The timestamp of the change.", required = true)
    @Builder.Default
    private final OffsetDateTime timeStamp = OffsetDateTime.now(ZoneOffset.UTC);

    @Schema(name = "Status", description = "The resource status after the change.", required = true)
    private String status;

    @Schema(name = "Message", description = "The human readable description of the change.")
    @Builder.Default
    private final Optional<String> message = Optional.empty();
}
