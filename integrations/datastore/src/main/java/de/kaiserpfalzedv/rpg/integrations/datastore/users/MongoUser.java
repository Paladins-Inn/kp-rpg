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

import de.kaiserpfalzedv.rpg.core.user.ImmutableUser;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.integrations.datastore.resources.MongoResource;
import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.StringJoiner;

/**
 * The stored user data.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-30
 */
@MongoEntity(collection = "users")
public class MongoUser extends MongoResource<User> {
    public MongoUserData spec;

    public MongoUser() {
    }

    public MongoUser(final User orig) {
        data(orig);
    }

    @Override
    @BsonIgnore
    @Transient
    public void data(final User orig) {
        super.data(orig);

        if (orig.getSpec().isPresent()) {
            spec = new MongoUserData(orig.getSpec().get());
        }
    }

    @Override
    @BsonIgnore
    @Transient
    public User data() {
        ImmutableUser.Builder result = ImmutableUser.builder()
                .metadata(metadata.data());

        if (spec != null) {
            result.spec(spec.data());
        }

        if (status != null) {
            result.status(status.data());
        }


        return result.build();
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", MongoUser.class.getSimpleName() + "[", "]")
                .add("hash=" + System.identityHashCode(this))
                .add("uid=" + uid)
                .add("nameSpace='" + nameSpace + "'")
                .add("name='" + name + "'")
                .toString();
    }
}
