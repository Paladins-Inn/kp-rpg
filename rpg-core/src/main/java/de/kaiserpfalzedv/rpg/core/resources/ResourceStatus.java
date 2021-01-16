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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * ResourceStatus -- The state of the managed resource.
 *
 * @param <H> Type of the additional history data.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-07
 */
@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableResourceStatus.class)
@JsonDeserialize(builder = ImmutableResourceStatus.Builder.class)
@JsonPropertyOrder({"observedGeneration,history"})
@Schema(name = "ResourceStatus", description = "The status of a resource.")
public interface ResourceStatus<H extends Serializable> extends Serializable {
    /**
     * @return the generation of the resource this history is generated for.
     */
    @Schema(name = "ObservedGeneration", description = "The generation of this resource which is observed.", required = true)
    Long getObservedGeneration();

    /**
     * @return a list of changes.
     */
    @Schema(name = "History", description = "A list of changes of the resource status.")
    List<ResourceHistory<H>> getHistory();

    /**
     * Adds a new history entry.
     *
     * @param status The status of this entry.
     * @param message The generic message for this history entry.
     * @param add Additional data.
     * @return TRUE if the history could be added.
     */
    default ResourceStatus<H> addHistory(final String status, final String message, final H add) {
        getHistory().add(
                ImmutableResourceHistory.<H>builder()
                        .status(status)
                        .timeStamp(OffsetDateTime.now(ZoneOffset.UTC))
                        .message(message)
                        .data(add)
                        .build()
        );

        return this;
    }

    /**
     * Adds a new history entry.
     *
     * @param status The status of this entry.
     * @param message The generic message for this history entry.
     * @return TRUE if the history could be added.
     */
    default ResourceStatus<H>  addHistory(final String status, final String message) {
        getHistory().add(
                ImmutableResourceHistory.<H>builder()
                        .status(status)
                        .timeStamp(OffsetDateTime.now(ZoneOffset.UTC))
                        .message(message)
                        .build()
        );

        return this;
    }
}
