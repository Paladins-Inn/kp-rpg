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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ResourceStatus -- The state of the managed resource.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-07
 */
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = ResourceStatus.ResourceStatusBuilder.class)
@JsonPropertyOrder({"observedGeneration,history"})
@Schema(name = "ResourceStatus", description = "The status of a resource.")
public class ResourceStatus implements Serializable {
    @Schema(name = "ObservedGeneration", description = "The generation of this resource which is observed.", required = true)
    @Builder.Default
    Long observedGeneration = 0L;

    @Schema(name = "History", description = "A list of changes of the resource status.")
    @Builder.Default
    List<ResourceHistory> history = new ArrayList<>();

    /**
     * Adds a new history entry.
     *
     * @param status  The status of this entry.
     * @param message The generic message for this history entry.
     * @return TRUE if the history could be added.
     */
    @SuppressWarnings("unused")
    public ResourceStatus addHistory(final String status, final String message) {
        getHistory().add(
                ResourceHistory.builder()
                        .withStatus(status)
                        .withTimeStamp(OffsetDateTime.now(ZoneOffset.UTC))
                        .withMessage(Optional.ofNullable(message))
                        .build()
        );

        return this;
    }
}
