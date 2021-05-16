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

package de.kaiserpfalzedv.rpg.store.resources;

import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ResourcePointer;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class MongoMetaData implements ResourcePointer {
    public String kind;
    public String apiVersion;

    public String namespace;
    public String name;
    public UUID uid;

    public MongoPointer owner;

    public Long generation;

    public MongoOffsetDateTime created;
    public MongoOffsetDateTime deleted;

    @Builder.Default
    public HashMap<String, String> annotations = new HashMap<>();
    @Builder.Default
    public HashMap<String, String> labels = new HashMap<>();


    public MongoMetaData(final ResourcePointer orig) {
        kind = orig.getKind();
        apiVersion = orig.getApiVersion();

        namespace = orig.getNamespace();
        name = orig.getName();
        uid = orig.getUid();
    }

    public MongoMetaData(final ResourceMetadata orig) {
        this((ResourcePointer) orig);

        if (orig.getOwner() != null) {
            orig.getOwner().ifPresent(o -> owner = new MongoPointer(o));
        }

        generation = orig.getGeneration();

        created = new MongoOffsetDateTime(orig.getCreated());
        if (orig.getDeleted() != null) {
            orig.getDeleted().ifPresent(d -> deleted = new MongoOffsetDateTime(d));
        }

        annotations = new HashMap<>(orig.getAnnotations());
        labels = new HashMap<>(orig.getLabels());
    }


    @BsonIgnore
    @Transient
    public ResourceMetadata data(final ObjectId id) {
        annotations.put("mongo-id", id.toHexString());

        return ResourceMetadata.builder()
                .withKind(kind)
                .withApiVersion(apiVersion)

                .withNamespace(namespace)
                .withName(name)
                .withUid(uid)
                .withGeneration(generation)

                .withCreated(created.timeStamp())

                .withAnnotations(annotations)
                .withLabels(labels)

                .withOwner(Optional.ofNullable(owner != null ? owner.data() : null))
                .withDeleted(Optional.ofNullable(deleted != null ? deleted.timeStamp() : null))

                .build();
    }
}
