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
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * ResourcePointer -- A single resource definition pointing to a unique resource on the server.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-07
 */
@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonPropertyOrder({"kind,apiVersion,namespace,name,selfLink"})
@Schema(name = "ResourcePointer", description = "A full address of a resource within the system.")
public interface ResourcePointer extends Serializable {
    /**
     * @return The kind of the resource.
     */
    @Schema(name = "Kind", description = "The kind (type) of the resource.", required = true)
    String getKind();

    /**
     * @return The version of the resource.
     */
    @Schema(name = "ApiVersion", description = "The version of the resource entry.", required = true)
    String getApiVersion();


    /**
     * @return The namespace of the resource.
     */
    @Schema(name = "Namespace", description = "The namespace of the resource.", required = true)
    String getNamespace();

    /**
     * @return The name of the resource. Must be unique within a namespace.
     */
    @Schema(name = "Name", description = "The unique name (within a namespace) of a resource.", required = true)
    String getName();

    /**
     * @return The unique id of the resource.
     * @since 1.2.0
     */
    @Schema(name = "Uid", description = "The unique id.")
    UUID getUid();

    /**
     * @return The local part of the URL to retrieve this resource.
     */
    @Schema(name = "SelfLink", description = "The local part of the URL to retrieve the resource.", required = true)
    default String getSelfLink() {
        return "/apis/" + getApiVersion() + "/" + getKind() + "/" + getUid();
    }
}
