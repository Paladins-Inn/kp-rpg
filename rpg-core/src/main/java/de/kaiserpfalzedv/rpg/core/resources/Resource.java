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
    /**
     * @return the metadata of the resource containing the structural parameters.
     */
    @Schema(name = "metadata", description = "Technical data to the resource.", required = true)
    ResourceMetadata getMetadata();

    /**
     * @return the data of the resource.
     */
    @Schema(name = "spec", description = "The resource data itself.")
    Optional<D> getSpec();

    /**
     * @return the status and history of the resource.
     */
    @Schema(name = "status", description = "The status of the resource (containing the history).")
    Optional<ResourceStatus> getStatus();

    /**
     * @return the unique identifier of the resource.
     */
    @Transient
    @JsonIgnore
    default UUID getUid() {
        return getMetadata().getUid();
    }

    /**
     * @return the type of the resource.
     */
    @Transient
    @JsonIgnore
    default String getKind() {
        return getMetadata().getKind();
    }

    /**
     * @return the version of the resource definition.
     */
    @Transient
    @JsonIgnore
    default String getApiVersion() {
        return getMetadata().getApiVersion();
    }

    /**
     * @return the namespace of the resource.
     */
    @Transient
    @JsonIgnore
    default String getNameSpace() {
        return getMetadata().getNamespace();
    }

    /**
     * @return the name of the resource.
     */
    @Transient
    @JsonIgnore
    default String getName() {
        return getMetadata().getName();
    }

    /**
     * @return the display name of the resource
     */
    @Transient
    @JsonIgnore
    default String getDisplayName() {
        return String.format("%s/%s/%s/%s", getKind(), getApiVersion(), getNameSpace(), getName());
    }

    /**
     * @return The generation of this resource.
     */
    @Transient
    @JsonIgnore
    default Long getGeneration() {
        return getMetadata().getGeneration();
    }
}
