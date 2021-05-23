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

import de.kaiserpfalzedv.rpg.core.resources.Resource;
import de.kaiserpfalzedv.rpg.core.store.OptimisticLockStoreException;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

/**
 * BaseMongoRepository -- The base methods for all stores.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@Slf4j
public class BaseMongoRepository<T extends Resource<D>, D extends Serializable> implements PanacheMongoRepositoryBase<T, UUID> {
    protected T prepareStorage(T object) throws OptimisticLockStoreException {
        Optional<T> stored = findByIdOptional(object.getUid());

        stored.ifPresentOrElse(
                t -> {
                    if (t.getGeneration() == object.getGeneration()) {
                        object.increaseGeneration();

                        log.trace("Increase the generation to: {}", object.getGeneration());
                    } else if (t.getGeneration() > object.getGeneration()) {
                        log.warn(
                                "Generation of database object is higher than the object to store. object='{}', oldGeneration={}, newGeneration={}",
                                object.getSelfLink(), t.getGeneration(), object.getGeneration()
                        );
                        throw new OptimisticLockStoreException(t.getGeneration(), object.getGeneration());
                    }
                },
                () -> log.debug("Initial storing of object: '{}'", object.getSelfLink())
        );

        return object;
    }
}
