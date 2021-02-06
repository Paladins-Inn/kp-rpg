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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
/**
 * Metadata -- common data for every resource of the system.
 *
 * @author klenkes74 {@literal <rlichit@kaiserpfalz-edv.de>}
 * @since 1.0.0
 */
@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableResourceMetadata.class)
@JsonDeserialize(builder = ImmutableResourceMetadata.Builder.class)
@JsonPropertyOrder({"uid,generation,owner,created,deleted,annotations,labels"})
@Schema(name = "ResourceMetadata", description = "The metadata of a resource.")
public interface ResourceMetadata extends ResourcePointer {
    /**
     * During generation of a new resource the generation is 0L. When the resource is persisted,
     * the generation is incremented.
     *
     * @return The generation of this resource. Starting with 1.
     */
    @Schema(name = "generation", description = "The generation of this object. Every change adds 1.", required = true, defaultValue = "0L")
    @Value.Default
    default Long getGeneration() {
        return 0L;
    }

    /**
     * @return The owning resource of this resource.
     */
    @Schema(name = "owner", description = "The owning resource. This is a sub-resource or managed resource of the given address.")
    Optional<ResourcePointer> getOwner();

    /**
     * @return the creation timestamp.
     */
    @Schema(name = "created", description = "The timestamp of resource creation.", required = true)
    OffsetDateTime getCreated();

    /**
     * @return the deletion timestamp.
     */
    @Schema(name = "deleted", description = "The timestamp of object deletion. Marks an object to be deleted.")
    Optional<OffsetDateTime> getDeleted();

    /**
     * Annotations are technical notes to resources.
     *
     * @return all annotations.
     */
    @Schema(name = "annotations", description = "A set of annotations to this resource.", maxItems = 256)
    @Value.Default
    default Map<String, String> getAnnotations() {
        return new HashMap<>();
    }

    /**
     * Checks if there is an annotation for this name.
     *
     * @param name the name of the annotation.
     * @return If there is an annotation for this name.
     */
    @JsonIgnore
    default boolean isAnnotated(final String name) {
        return getAnnotations().containsKey(name);
    }

    /**
     * Labels are tags assigned by users to resources.
     *
     * @return all labels.
     */
    @Schema(name = "labels", description = "A set of labels to this resource.", maxItems = 256)
    @Value.Default
    default Map<String, String> getLabels() {
        return new HashMap<>();
    }

    /**
     * Checks if there is a label with a special name.
     *
     * @param name The name of the label.
     * @return If the label is there.
     */
    @JsonIgnore
    default boolean isLabeled(final String name) {
        return getLabels().containsKey(name);
    }

    @Override
    @Schema(name = "SelfLink", description = "The local part of the URL to retrieve the resource.", required = true)
    @Value.Default
    default String getSelfLink() {
        return "/apis/" + getApiVersion() + "/" + getKind() + "/" + getUid();
    }
}
