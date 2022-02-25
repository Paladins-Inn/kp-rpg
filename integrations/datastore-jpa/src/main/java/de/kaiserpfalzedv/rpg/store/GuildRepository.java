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
import de.kaiserpfalzedv.commons.discord.guilds.Guild;
import de.kaiserpfalzedv.commons.discord.guilds.GuildStoreService;
import io.quarkus.arc.Priority;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
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
@Alternative
@Priority(400)
@ToString
@Slf4j
public class GuildRepository implements PanacheRepositoryBase<Guild, UUID>, GuildStoreService {
    public Optional<Guild> findByNameSpaceAndName(final String nameSpace, final String name) {
        return find(
                "namespace = :nameSpace and name = :name",
                Map.of(
                        "nameSpace", nameSpace,
                        "name", name
                )
        ).firstResultOptional();
    }

    public Optional<Guild> findByUid(final UUID uid) {
        return Optional.ofNullable(findById(uid));
    }

    public Guild save(final Guild object) throws OptimisticLockStoreException, DuplicateStoreException {
        persistAndFlush(object);

        return object;
    }

    public void remove(final Guild object) {
        delete(object);
    }

    public void remove(final String nameSpace, final String name) {
        findByNameSpaceAndName(nameSpace, name).ifPresentOrElse(
                this::delete,
                () -> log.info("Guild {}/{} did not exist.", nameSpace, name)
        );
    }

    public void remove(final UUID uid) {
        deleteById(uid);
    }
}
