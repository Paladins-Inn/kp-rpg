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
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.beans.Transient;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Metadata -- common data for every resource of the system.
 * <p>
 * Default values for the lombok builder are set in {@link MetadataBuilder}.
 *
 * @author klenkes74 {@literal <rlichit@kaiserpfalz-edv.de>}
 * @since 1.0.0
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "FieldMayBeFinal"})
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = Metadata.MetadataBuilder.class)
@JsonPropertyOrder({"owner,created,deleted,annotations,labels"})
@Schema(name = "ResourceMetadata", description = "The metadata of a resource.")
public class Metadata implements Serializable {
    @Schema(name = "owner", description = "The owning resource. This is a sub-resource or managed resource of the given address.")
    @Builder.Default
    private ResourcePointer owner = null;

    @Schema(name = "created", description = "The timestamp of resource creation.", required = true)
    @Builder.Default
    private OffsetDateTime created = OffsetDateTime.now(ZoneOffset.UTC);

    @Schema(name = "deleted", description = "The timestamp of object deletion. Marks an object to be deleted.")
    @Builder.Default
    private OffsetDateTime deleted = null;

    @Schema(name = "annotations", description = "A set of annotations to this resource.", maxItems = 256)
    @Singular
    private Map<String, String> annotations = new HashMap<>();

    @Schema(name = "labels", description = "A set of labels to this resource.", maxItems = 256)
    @Singular
    private Map<String, String> labels = new HashMap<>();


    @Transient
    @JsonIgnore
    @BsonIgnore
    public Optional<OffsetDateTime> getDeletionTimestamp() {
        return Optional.ofNullable(deleted);
    }

    @Transient
    @JsonIgnore
    @BsonIgnore
    public Optional<ResourcePointer> getOwningResource() {
        return Optional.ofNullable(owner);
    }

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
