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
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.util.Optional;

/**
 *
 * @param <D> The data provided by this resource.
 * @param <H> The type of additional history data.
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonPropertyOrder({"kind,apiVersion,metadata,spec,status"})
public interface Resource<D extends Serializable, H extends Serializable> extends Serializable {
    @Schema(name = "metadata", description = "Technical data to the resource.", required = true)
    ResourceMetadata getMetadata();

    @Schema(name = "spec", description = "The resource data itself.")
    Optional<D> getSpec();

    @Schema(name = "status", description = "The status of the resource (containting the history).")
    Optional<ResourceStatus<H>> getStatus();
}
