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

package de.kaiserpfalzedv.rpg.store.users;

import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.store.resources.MongoResource;
import io.quarkus.mongodb.panache.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;

/**
 * The stored user data.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-30
 */
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@MongoEntity(collection = "users")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class MongoUser extends MongoResource<User> {
    public MongoUserData spec;

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
        User.UserBuilder result = User.builder()
                .metadata(metadata.data(id));

        if (spec != null) {
            result.spec(spec.data());
        }

        if (status != null) {
            result.state(status.data());
        }


        return result.build();
    }
}
