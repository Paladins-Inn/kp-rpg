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
import de.kaiserpfalzedv.commons.discord.guilds.Guild;
import de.kaiserpfalzedv.commons.discord.guilds.GuildData;
import de.kaiserpfalzedv.commons.discord.guilds.GuildStoreService;
import io.quarkus.arc.AlternativePriority;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * GuildRepository --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@ApplicationScoped
@AlternativePriority(500)
@Slf4j
@ToString
public class GuildRepository extends BaseMongoRepository<Guild, GuildData> implements GuildStoreService {
    @Override
    public Optional<Guild> findByNameSpaceAndName(final String nameSpace, final String name) {
        log.trace("Loading guild by namespace and name: '{}/{}'", nameSpace, name);
        return find(
                "namespace = :nameSpace and name = :name",
                Map.of(
                        "nameSpace", nameSpace,
                        "name", name
                )
        )
                .firstResultOptional();
    }

    @Override
    public Optional<Guild> findByUid(final UUID uid) {
        log.trace("Loading guild by uid={}", uid);
        return Optional.ofNullable(findById(uid));
    }

    @Override
    public Guild save(final Guild object) throws OptimisticLockStoreException, DuplicateStoreException {
        log.trace("Saving guild: '{}'", object.getSelfLink());

        Guild stored = prepareStorage(object.toBuilder().build());
        persistOrUpdate(stored);

        return stored;
    }

    @Override
    public void remove(final Guild object) {
        log.trace("Deleting guild: '{}'", object.getSelfLink());
        delete(object);
    }

    @Override
    public void remove(final String nameSpace, final String name) {
        log.trace("Deleting guild '{}/{}'.", nameSpace, name);

        findByNameSpaceAndName(nameSpace, name).ifPresentOrElse(
                this::delete,
                () -> log.debug("Guild '{}/{}' did not exist.", nameSpace, name)
        );

        log.debug("Guild '{}/{}' deleted.", nameSpace, name);
    }

    @Override
    public void remove(final UUID uid) {
        deleteById(uid);
    }
}
