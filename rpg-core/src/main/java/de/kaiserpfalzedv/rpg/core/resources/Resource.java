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
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
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
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonPropertyOrder({"metadata,spec,status"})
public class Resource<D extends Serializable> implements Serializable, ResourcePointer {
    @BsonId
    @EqualsAndHashCode.Include
    @ToString.Include
    @Schema(name = "Uid", description = "The unique id.")
    @Builder.Default
    private final UUID uid = UUID.randomUUID();

    @EqualsAndHashCode.Include
    @ToString.Include
    @Schema(name = "Kind", description = "The kind (type) of the resource.", required = true)
    private String kind;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Schema(name = "ApiVersion", description = "The version of the resource entry.", required = true)
    @Builder.Default
    private final String apiVersion = "v1";

    @EqualsAndHashCode.Include
    @ToString.Include
    @Schema(name = "Namespace", description = "The namespace of the resource.", required = true)
    private String namespace;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Schema(name = "Name", description = "The unique name (within a namespace) of a resource.", required = true)
    private String name;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Schema(name = "generation", description = "The generation of this object. Every change adds 1.", required = true, defaultValue = "0L")
    @Builder.Default
    private Long generation = 0L;

    @Schema(name = "metadata", description = "Technical data to the resource.", required = true)
    protected Metadata metadata;

    @Schema(name = "spec", description = "The resource data itself.")
    @Builder.Default
    protected D spec = null;

    @Schema(name = "status", description = "The status of the resource (containing the history).")
    @Builder.Default
    protected Status status = null;

    /**
     * @return the display name of the resource
     */
    @Transient
    @JsonIgnore
    @BsonIgnore
    public String getDisplayName() {
        return String.format("%s/%s/%s/%s", getKind(), getApiVersion(), getNamespace(), getName());
    }

    @Transient
    @JsonIgnore
    @BsonIgnore
    public void increaseGeneration() {
        generation++;
    }

    @Transient
    @JsonIgnore
    @BsonIgnore
    public Optional<D> getData() {
        return Optional.ofNullable(spec);
    }

    @Transient
    @JsonIgnore
    @BsonIgnore
    public Optional<Status> getState() {
        return Optional.ofNullable(status);
    }
}
