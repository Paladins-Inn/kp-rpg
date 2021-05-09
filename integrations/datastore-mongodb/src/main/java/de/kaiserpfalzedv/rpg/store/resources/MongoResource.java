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

import de.kaiserpfalzedv.rpg.core.resources.Resource;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@MongoEntity
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class MongoResource<T extends Resource<?>> extends PanacheMongoEntity {
    @EqualsAndHashCode.Include
    @BsonProperty("uid")
    public UUID uid;

    @BsonProperty("nameSpace")
    public String nameSpace;
    @BsonProperty("name")
    public String name;


    @BsonProperty("metadata")
    public MongoMetaData metadata;
    @BsonProperty("status")
    public MongoResourceStatus status;


    /**
     * Loads data from the Resource.
     *
     * @param data The resource to load data from.
     */
    public void data(final T data) {
        if (data.getMetadata().isAnnotated("mongo-id")) {
            id = new ObjectId(data.getMetadata().getAnnotations().get("mongo-id")); // reload the mongodb id from the annotations.
        }
        uid = data.getUid();
        nameSpace = data.getNameSpace();
        name = data.getName();

        metadata = new MongoMetaData(data.getMetadata());
        metadata.annotations.remove("mongo-id"); // remove the mongodb id if it is in there ...

        if (data.getStatus() != null && data.getStatus().isPresent()) {
            status = new MongoResourceStatus(data.getStatus().get());
        }
    }

    public void updateHistory() {
        if (status == null) {
            status = new MongoResourceStatus();
        }
        status.updateHistory(metadata);
    }

    /**
     * @return The Resource compatible equivalent of the local data.
     */
    public abstract T data();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MongoResource)) return false;
        MongoResource<?> that = (MongoResource<?>) o;
        return uid.equals(that.uid) && nameSpace.equals(that.nameSpace) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, nameSpace, name);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MongoResource.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .add("id=" + id)
                .add("uid='" + uid + "'")
                .add("nameSpace='" + nameSpace + "'")
                .add("name='" + name + "'")
                .add("metadata=" + metadata)
                .add("status=" + status)
                .toString();
    }
}
