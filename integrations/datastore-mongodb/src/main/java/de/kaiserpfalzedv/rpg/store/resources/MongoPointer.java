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

import de.kaiserpfalzedv.rpg.core.resources.Pointer;
import de.kaiserpfalzedv.rpg.core.resources.ResourcePointer;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import java.beans.Transient;
import java.util.UUID;

/**
 * A generic resource pointer.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class MongoPointer implements ResourcePointer {
    public String kind;
    public String apiVersion;

    public String namespace;
    public String name;
    public UUID uid;

    public MongoPointer(final ResourcePointer orig) {
        kind = orig.getKind();
        apiVersion = orig.getApiVersion();

        namespace = orig.getNamespace();
        name = orig.getName();
    }

    @BsonIgnore
    @Transient
    public Pointer data() {
        return Pointer.builder()
                .kind(kind)
                .apiVersion(apiVersion)
                .namespace(namespace)
                .name(name)
                .uid(uid)
                .build();
    }

    @BsonIgnore
    @Transient
    public MongoPointer data(final ObjectId id) {
        return MongoPointer.builder()
                .kind(kind)
                .apiVersion(apiVersion)

                .namespace(namespace)
                .name(name)

                .uid(uid)

                .build();
    }
}
