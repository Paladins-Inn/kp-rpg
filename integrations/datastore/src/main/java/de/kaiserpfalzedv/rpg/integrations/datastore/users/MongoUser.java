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

package de.kaiserpfalzedv.rpg.integrations.datastore.users;

import de.kaiserpfalzedv.rpg.core.resources.ResourceStatus;
import de.kaiserpfalzedv.rpg.core.user.ImmutableUser;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.integrations.datastore.resources.MongoMetaData;
import de.kaiserpfalzedv.rpg.integrations.datastore.resources.MongoResourceStatus;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.UUID;

/**
 * The stored user data.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-30
 */
@MongoEntity(collection = "users")
public class MongoUser extends PanacheMongoEntityBase {
    @BsonId
    public UUID uid;

    public String nameSpace;
    public String name;
    public MongoMetaData metadata;
    public MongoUserData spec;
    public MongoResourceStatus status;


    public MongoUser() {}

    public MongoUser(final User orig) {
        uid = orig.getMetadata().getUid();
        nameSpace = orig.getMetadata().getNamespace();
        name = orig.getMetadata().getName();

        metadata = new MongoMetaData(orig.getMetadata());
        status = orig.getStatus().isPresent() ? new MongoResourceStatus(orig.getStatus().get()) : null;

        if (orig.getSpec().isPresent()) {
            spec = new MongoUserData(orig.getSpec().get());
        }
    }

    @BsonIgnore
    @Transient
    public User user() {
        return ImmutableUser.builder()
                .metadata(metadata.metadata())
                .spec(spec != null ? spec.userData() : null)
                .status(status != null ? status.status() : null)
                .build();
    }
}
