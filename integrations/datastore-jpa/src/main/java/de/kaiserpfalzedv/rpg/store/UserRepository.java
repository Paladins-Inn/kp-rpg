/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti.
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

import de.kaiserpfalzedv.commons.core.store.DuplicateStoreException;
import de.kaiserpfalzedv.commons.core.store.OptimisticLockStoreException;
import de.kaiserpfalzedv.commons.core.user.User;
import de.kaiserpfalzedv.commons.core.user.UserStoreService;
import io.quarkus.arc.Priority;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
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
@Alternative
@Priority(400)
@Slf4j
public class UserRepository implements PanacheRepositoryBase<User, UUID>, UserStoreService {
    public Optional<User> findByNameSpaceAndName(String nameSpace, String name) {
        return find(
                "namespace = :namespace and name = :name",
                Map.of(
                        "namespace", nameSpace,
                        "name", name
                ))
                .firstResultOptional();
    }

    public Optional<User> findByUid(UUID uid) {
        return Optional.ofNullable(findById(uid));
    }

    public User save(final User object) throws OptimisticLockStoreException, DuplicateStoreException {
        persist(object);

        return object;
    }

    public void remove(User object) {
        delete(object);
    }

    public void remove(String nameSpace, String name) {
        findByNameSpaceAndName(nameSpace, name).ifPresentOrElse(
                this::delete,
                () -> log.info("User {}/{} did not exist.", nameSpace, name)
        );
    }

    public void remove(UUID uid) {
        deleteById(uid);
    }
}
