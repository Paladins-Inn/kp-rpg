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

package de.kaiserpfalzedv.rpg.core.store;

import de.kaiserpfalzedv.rpg.core.resources.Resource;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * GenericStoreService -- an ephemeral store for Resources.
 * <p>
 * This is a memory alternative for a persistent data store.
 *
 * @param <T> The resource to be stored inside the data store.
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
@ToString
@EqualsAndHashCode
@Slf4j
public abstract class GenericStoreService<T extends Resource<?>> implements StoreService<T> {
    /**
     * The name based memory store for guilds.
     */
    private final HashMap<String, T> namedStore = new HashMap<>(10);

    /**
     * The uid based memory store for guilds.
     */
    private final HashMap<UUID, T> uidStore = new HashMap<>(10);


    @Override
    public Optional<T> findByNameSpaceAndName(final String nameSpace, final String name) {
        String key = generateStoreKey(nameSpace, name);

        return Optional.ofNullable(namedStore.get(key));
    }

    @Override
    public Optional<T> findByUid(final UUID uid) {
        return Optional.ofNullable(uidStore.get(uid));
    }

    private String generateStoreKey(final String nameSpace, final String name) {
        return nameSpace + "-" + name;
    }

    @Override
    public T save(final T object) throws OptimisticLockStoreException {
        log.trace("Saving: {}", object);

        String key = generateStoreKey(object.getNamespace(), object.getName());

        //noinspection unchecked
        T data = (!namedStore.containsKey(key))
                ? object
                : (T) object.toBuilder().withGeneration(object.getGeneration() + 1).build();

        checkOptimisticLocking(key, data);

        namedStore.put(key, data);
        uidStore.put(object.getUid(), data);
        return data;
    }

    private void checkOptimisticLocking(final String key, final T object) {
        if (
                namedStore.containsKey(key)
                        && (namedStore.get(key).getGeneration() >= object.getGeneration())
        ) {
            throw new OptimisticLockStoreException(namedStore.get(key).getGeneration(), object.getGeneration());
        }
    }

    @Override
    public void remove(final T object) {
        String key = generateStoreKey(object.getNamespace(), object.getName());

        namedStore.remove(key);
        uidStore.remove(object.getUid());
    }

    @Override
    public void remove(final String nameSpace, final String name) {
        String key = generateStoreKey(nameSpace, name);

        if (namedStore.containsKey(key)) {
            uidStore.remove(namedStore.get(key).getUid());
            namedStore.remove(key);
        }
    }

    @Override
    public void remove(final UUID uid) {
        if (uidStore.containsKey(uid)) {
            T data = uidStore.get(uid);
            String key = generateStoreKey(data.getNamespace(), data.getName());

            namedStore.remove(key);
            uidStore.remove(uid);
        }
    }
}
