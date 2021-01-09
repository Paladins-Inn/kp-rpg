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

package de.kaiserpfalzedv.rpg.core.files.mongodb;

import de.kaiserpfalzedv.rpg.core.files.FileData;
import de.kaiserpfalzedv.rpg.core.files.FileResource;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ResourceStatus;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.Optional;
import java.util.UUID;

/**
 * File -- The entity for storing the file resource.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-09
 */
@MongoEntity
public class File extends PanacheMongoEntityBase implements FileResource {
    /** ID of the document. */
    @BsonId
    public UUID uid;

    /** The resource meta data. */
    public ResourceMetadata metadata;

    /** The status of the resource. */
    public ResourceStatus<String> status;


    @BsonIgnore
    @Transient
    @Override
    public ResourceMetadata getMetadata() {
        return metadata;
    }

    @BsonIgnore
    @Transient
    @Override
    public Optional<FileData> getSpec() {
        return Optional.empty();
    }

    @BsonIgnore
    @Transient
    @Override
    public Optional<ResourceStatus<String>> getStatus() {
        return Optional.ofNullable(status);
    }
}
