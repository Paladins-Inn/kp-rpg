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
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Metadata -- common data for every resource of the system.
 *
 * @author klenkes74 {@literal <rlichit@kaiserpfalz-edv.de>}
 * @since 1.0.0
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonPropertyOrder({"uid,generation,owner,created,deleted,annotations,labels"})
@Schema(name = "ResourceMetadata", description = "The metadata of a resource.")
public class ResourceMetadata implements ResourcePointer {
    @EqualsAndHashCode.Include
    @Schema(name = "Uid", description = "The unique id.")
    private final UUID uid = UUID.randomUUID();
    @EqualsAndHashCode.Include
    @Schema(name = "generation", description = "The generation of this object. Every change adds 1.", required = true, defaultValue = "0L")
    private final Long generation = 0L;
    @Schema(name = "owner", description = "The owning resource. This is a sub-resource or managed resource of the given address.")
    private final Optional<ResourcePointer> owner = Optional.empty();
    @Schema(name = "created", description = "The timestamp of resource creation.", required = true)
    private final OffsetDateTime created = OffsetDateTime.now(ZoneOffset.UTC);
    @Schema(name = "deleted", description = "The timestamp of object deletion. Marks an object to be deleted.")
    private final Optional<OffsetDateTime> deleted = Optional.empty();
    @Schema(name = "annotations", description = "A set of annotations to this resource.", maxItems = 256)
    private final Map<String, String> annotations = new HashMap<>();
    @Schema(name = "labels", description = "A set of labels to this resource.", maxItems = 256)
    private final Map<String, String> labels = new HashMap<>();
    @Schema(name = "Kind", description = "The kind (type) of the resource.", required = true)
    private String kind;
    @Schema(name = "ApiVersion", description = "The version of the resource entry.", required = true)
    private String apiVersion;
    @Schema(name = "Namespace", description = "The namespace of the resource.", required = true)
    private String namespace;
    @Schema(name = "Name", description = "The unique name (within a namespace) of a resource.", required = true)
    private String name;

    /**
     * Checks if there is an annotation for this name.
     *
     * @param name the name of the annotation.
     * @return If there is an annotation for this name.
     */
    @JsonIgnore
    public boolean isAnnotated(final String name) {
        return getAnnotations().containsKey(name);
    }

    /**
     * Checks if there is a label with a special name.
     *
     * @param name The name of the label.
     * @return If the label is there.
     */
    @JsonIgnore
    public boolean isLabeled(final String name) {
        return getLabels().containsKey(name);
    }
}
