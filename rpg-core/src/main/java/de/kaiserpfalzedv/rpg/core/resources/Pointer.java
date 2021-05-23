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
import org.bson.codecs.pojo.annotations.BsonId;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.UUID;

/**
 * ResourcePointer -- A single resource definition pointing to a unique resource on the server.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-07
 */
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = Pointer.PointerBuilder.class)
@JsonPropertyOrder({"kind,apiVersion,namespace,name,selfLink"})
@Schema(name = "ResourcePointer", description = "A full address of a resource within the system.")
public class Pointer implements ResourcePointer {
    @BsonId
    @Schema(name = "Uid", description = "The unique id.")
    @Builder.Default
    @EqualsAndHashCode.Include
    private final UUID uid = UUID.randomUUID();

    @Schema(name = "Kind", description = "The kind (type) of the resource.", required = true)
    private String kind;

    @Schema(name = "ApiVersion", description = "The version of the resource entry.", required = true)
    @Builder.Default
    private final String apiVersion = "v1";

    @Schema(name = "Namespace", description = "The namespace of the resource.", required = true)
    private String namespace;

    @Schema(name = "Name", description = "The unique name (within a namespace) of a resource.", required = true)
    private String name;
}
