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

package de.kaiserpfalzedv.rpg.store;

import de.kaiserpfalzedv.commons.core.mongodb.BaseMongoRepository;
import de.kaiserpfalzedv.commons.core.store.DuplicateStoreException;
import de.kaiserpfalzedv.commons.core.store.OptimisticLockStoreException;
import de.kaiserpfalzedv.commons.core.user.User;
import de.kaiserpfalzedv.commons.core.user.UserData;
import de.kaiserpfalzedv.commons.core.user.UserStoreService;
import io.quarkus.arc.AlternativePriority;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@ApplicationScoped
@AlternativePriority(500)
@Slf4j
public class UserRepository extends BaseMongoRepository<User, UserData> implements UserStoreService {
    @Override
    public Optional<User> findByNameSpaceAndName(String nameSpace, String name) {
        return find(
                "namespace = :namespace and name = :name",
                Map.of(
                        "namespace", nameSpace,
                        "name", name
                ))
                .firstResultOptional();
    }

    @Override
    public Optional<User> findByUid(UUID uid) {
        return Optional.ofNullable(findById(uid));
    }

    @Override
    public User save(final User object) throws OptimisticLockStoreException, DuplicateStoreException {
        persistOrUpdate(object);

        return object;
    }

    @Override
    public void remove(User object) {
        delete(object);

        log.debug("User '{}' deleted.", object.getSelfLink());
    }

    @Override
    public void remove(String nameSpace, String name) {
        log.trace("Deleting user '{}/{}'.", nameSpace, name);

        findByNameSpaceAndName(nameSpace, name).ifPresentOrElse(
                this::delete,
                () -> log.debug("User '{}/{}' did not exist.", nameSpace, name)
        );

        log.debug("User '{}/{}' deleted.", nameSpace, name);
    }

    @Override
    public void remove(UUID uid) {
        deleteById(uid);

        log.debug("User with UUID '{}' deleted.", uid);
    }
}
