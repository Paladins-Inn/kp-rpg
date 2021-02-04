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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

/**
 * A generic resource.
 *
 * @param <D> The data provided by this resource.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonPropertyOrder({"kind,apiVersion,metadata,spec,status"})
public interface Resource<D extends Serializable> extends Serializable {
    @Schema(name = "metadata", description = "Technical data to the resource.", required = true)
    ResourceMetadata getMetadata();

    @Schema(name = "spec", description = "The resource data itself.")
    Optional<D> getSpec();

    @Schema(name = "status", description = "The status of the resource (containting the history).")
    Optional<ResourceStatus> getStatus();

    @Transient
    @JsonIgnore
    default UUID getUid() {
        return getMetadata().getUid();
    }

    @Transient
    @JsonIgnore
    default String getKind() {
        return getMetadata().getKind();
    }

    @Transient
    @JsonIgnore
    default String getApiVersion() {
        return getMetadata().getApiVersion();
    }

    @Transient
    @JsonIgnore
    default String getNameSpace() {
        return getMetadata().getNamespace();
    }

    @Transient
    @JsonIgnore
    default String getName() {
        return getMetadata().getName();
    }

    @Transient
    @JsonIgnore
    default String getDisplayName() {
        return String.format("%s/%s/%s/%s", getKind(), getApiVersion(), getNameSpace(), getName());
    }

    @Transient
    @JsonIgnore
    default Long getGeneration() {
        return getMetadata().getGeneration();
    }
}
