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

import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.Resource;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;

import java.util.HashMap;
import java.util.Optional;
import java.util.StringJoiner;
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
        String key = generateStoreKey(object.getMetadata().getNamespace(), object.getMetadata().getName());
        T data = object;

        if (namedStore.containsKey(key)) {
            T stored = namedStore.get(key);
            data = increaseGeneration(object);

            checkGeneration(stored, data);
        }

        namedStore.put(key, data);
        uidStore.put(data.getMetadata().getUid(), data);
        return data;
    }

    @Override
    public void remove(final T object) {
        String key = generateStoreKey(object.getMetadata().getNamespace(), object.getMetadata().getName());

        namedStore.remove(key);
        uidStore.remove(object.getMetadata().getUid());
    }

    @Override
    public void remove(final String nameSpace, final String name) {
        String key = generateStoreKey(nameSpace, name);

        if (namedStore.containsKey(key)) {
            uidStore.remove(namedStore.get(key).getMetadata().getUid());
            namedStore.remove(key);
        }
    }

    @Override
    public void remove(final UUID uid) {
        if (uidStore.containsKey(uid)) {
            T data = uidStore.get(uid);
            String key = generateStoreKey(data.getMetadata().getNamespace(), data.getMetadata().getName());

            namedStore.remove(key);
            uidStore.remove(uid);
        }
    }

    /**
     * Increases the generation of the data to store.
     *
     * @param data The data to store.
     * @return The data object with an increased generation.
     */
    public abstract T increaseGeneration(final T data);


    /**
     * Checks if the generation of stored >= generation of data.
     *
     * @param stored the stored resource.
     * @param data the resource to be stored.
     * @throws OptimisticLockStoreException If the generation of the new data is not higher than the already stored data
     */
    private void checkGeneration(final T stored, final T data) throws OptimisticLockStoreException {
        long storedGeneration = stored.getMetadata().getGeneration();
        long dataGeneration = data.getMetadata().getGeneration();

        if (storedGeneration > dataGeneration)
            throw new OptimisticLockStoreException(storedGeneration, dataGeneration);
    }

    /**
     * Increases the generation of the metadata by 1.
     *
     * @param metadata the original data.
     * @return the new metadata.
     */
    protected ResourceMetadata increaseGeneration(final ResourceMetadata metadata) {
        return ImmutableResourceMetadata.builder()
                .from(metadata)
                .generation(metadata.getGeneration() + 1)
                .build();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericStoreService.class.getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .add("namedStore=" + namedStore.size())
                .add("uidStore=" + uidStore.size())
                .toString();
    }
}
