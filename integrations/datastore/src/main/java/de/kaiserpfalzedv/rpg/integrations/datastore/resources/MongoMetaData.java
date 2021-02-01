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

package de.kaiserpfalzedv.rpg.integrations.datastore.resources;

import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringJoiner;

public class MongoMetaData extends MongoResourcePointer {
    public MongoResourcePointer owner;

    public Long generation;

    public MongoOffsetDateTime created;
    public MongoOffsetDateTime deleted;

    public HashMap<String, String> annotations;
    public HashMap<String, String> labels;

    @SuppressWarnings("unused")
    public MongoMetaData() {}

    public MongoMetaData(final ResourceMetadata orig) {
        super(orig);

        kind = orig.getKind();
        apiVersion =orig.getApiVersion();

        if (orig.getOwner().isPresent()) {
            owner = new MongoResourcePointer(orig.getOwner().get());
        }

        nameSpace = orig.getNamespace();
        name = orig.getName();
        uid = orig.getUid();
        generation = orig.getGeneration();

        created = new MongoOffsetDateTime(orig.getCreated());
        if (orig.getDeleted().isPresent()) {
            deleted = new MongoOffsetDateTime(orig.getDeleted().get());
        }

        annotations = new HashMap<>(orig.getAnnotations());
        labels = new HashMap<>(orig.getLabels());
    }


    @BsonIgnore
    @Transient
    public ResourceMetadata data() {
        ImmutableResourceMetadata.Builder result = ImmutableResourceMetadata.builder()
                .kind(kind)
                .apiVersion(apiVersion)

                .namespace(nameSpace)
                .name(name)
                .uid(uid)
                .generation(generation)
                .selfLink(ResourceMetadata.generateSelfLink("", kind, apiVersion, uid))

                .created(created.timeStamp())

                .annotations(annotations)
                .labels(labels);

        if (owner != null) result.owner(owner.data());
        if (deleted != null) result.deleted(deleted.timeStamp());

        return result.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MongoMetaData)) return false;
        if (!super.equals(o)) return false;
        MongoMetaData that = (MongoMetaData) o;
        return Objects.equals(owner, that.owner) && generation.equals(that.generation) && created.equals(that.created) && Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), owner, generation, created, deleted);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MongoMetaData.class.getSimpleName() + "[", "]")
                .add("owner=" + owner)
                .add("generation=" + generation)
                .add("kind='" + kind + "'")
                .add("apiVersion='" + apiVersion + "'")
                .add("nameSpace='" + nameSpace + "'")
                .add("name='" + name + "'")
                .add("uid='" + uid + "'")
                .toString();
    }
}
