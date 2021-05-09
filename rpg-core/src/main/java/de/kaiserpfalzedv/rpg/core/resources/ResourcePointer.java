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

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;

/**
 * ResourcePointer --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-09
 */
public interface ResourcePointer extends Serializable {
    @Schema(name = "kind", description = "The type of the resource")
    String getKind();

    @Schema(name = "apiVersion", description = "The version of this resource")
    String getApiVersion();

    @Schema(name = "namespace", description = "The namespace (group) of this resource")
    String getNamespace();

    @Schema(name = "name", description = "The unique name of this resource within the namespace")
    String getName();

    @Schema(name = "uid", description = "The unique identifier of this resource")
    java.util.UUID getUid();

    @Schema(name = "SelfLink", description = "The local part of the URL to retrieve the resource.", required = true)
    default String getSelfLink() {
        return "/apis/" + getApiVersion() + "/" + getKind() + "/" + getUid();
    }

}
