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

import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.store.OptimisticLockStoreException;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.core.user.UserStoreService;
import io.quarkus.arc.AlternativePriority;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@AlternativePriority(1000)
public class UserRepository implements UserStoreService, PanacheMongoRepository<MongoUser> {
    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    public Optional<User> findByNameSpaceAndName(final String nameSpace, final String name) {
        LOG.trace("loading: type=user, nameSpace={}, name={}", nameSpace, name);

        MongoUser result = MongoUser.find("{'nameSpace': ?1, 'name': ?2}", nameSpace, name).firstResult();
        if (result == null) {
            LOG.info("not found: type=user, nameSpace='{}', name='{}'", nameSpace, name);
            return Optional.empty();
        }

        LOG.debug("Loaded: {}, metadata={}", result, result.metadata);
        return Optional.of(result.user());
    }

    @Override
    public Optional<User> findByUid(final UUID uid) {
        LOG.trace("loading: type=user, uid={}", uid);

        MongoUser result = MongoUser.findById(uid);
        if (result == null) {
            LOG.info("not found: type=user, uid='{}'", uid);
            return Optional.empty();
        }

        LOG.debug("Loaded: {}, metadata={}", result, result.metadata);
        return Optional.of(result.user());
    }

    @Override
    public User persist(final User object) {
        LOG.trace("persisting: user={}", object);

        MongoUser toSave = new MongoUser(object);

        Optional<User> stored = findByUid(object.getUid());
        if (stored.isPresent()) {
            checkGeneration(stored.get().getMetadata(), object.getMetadata());
        } else {
            if (toSave.uid == null) {
                toSave.uid = UUID.randomUUID();

                LOG.warn("Added UID to user: uid={}, nameSpace='{}', name='{}'", toSave.uid, toSave.nameSpace, toSave.name);
            }
        }

        toSave.status.updateHistory();
        persistOrUpdate(toSave);
        LOG.debug("persisted: user={}", toSave);
        return toSave.user();
    }

    private void checkGeneration(final ResourceMetadata stored, final ResourceMetadata data) {
        if (stored.getGeneration() > data.getGeneration()) {
            throw new OptimisticLockStoreException(stored.getGeneration(), data.getGeneration());
        }
    }


    @Override
    public void delete(final User object) {
        LOG.info("remove: type=user, uid={}, nameSpace={}, name={}",
                object.getMetadata().getUid(),
                object.getMetadata().getNamespace(),
                object.getMetadata().getName()
        );

        delete(new MongoUser(object));
    }

    @Override
    public void delete(String nameSpace, String name) {
        LOG.trace("remove: type=user, nameSpace={}, name={}", nameSpace, name);

        Optional<User> object = findByNameSpaceAndName(nameSpace, name);

        object.ifPresent(this::delete);
    }

    @Override
    public void delete(final UUID uid) {
        LOG.trace("remove: type=user, uid={}", uid);

        Optional<User> object = findByUid(uid);

        object.ifPresent(this::delete);
    }
}
