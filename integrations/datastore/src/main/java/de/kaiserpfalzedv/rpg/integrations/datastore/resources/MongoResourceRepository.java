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

package de.kaiserpfalzedv.rpg.integrations.datastore.resources;

import de.kaiserpfalzedv.rpg.core.resources.Resource;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.store.DuplicateStoreException;
import de.kaiserpfalzedv.rpg.core.store.OptimisticLockStoreException;
import de.kaiserpfalzedv.rpg.core.store.StoreService;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * ResourceRepository -- The generic repository for structured Resources.
 *
 * @param <T> The default immutable resource.
 * @param <M> The MongoDB variant of the resource.
 */
public abstract class MongoResourceRepository<T extends Resource<?>, M extends MongoResource<T>> implements StoreService<T>, PanacheMongoRepository<M> {
    protected Logger LOG;

    @PostConstruct
    public MongoResourceRepository<T, M> setUp() {
        LOG = LoggerFactory.getLogger(getClass());

        LOG.info("MongoRepository created: resource={}", empty().getClass().getSimpleName());

        return this;
    }

    @PreDestroy
    public void tearDown() {
        LOG.info("MongoRepository destroyed: resource={}, count={}",
                empty().getClass().getSimpleName(),
                M.findAll().count()
        );
    }

    /**
     * @return an Empty instance of the Mongo resource.
     */
    public abstract M empty();

    /**
     * Creates a query. Needed to externalize the query to the concrete repositories - otherwise Panache
     * returned empty result sets.
     *
     * @param query      the query string.
     * @param parameters the parameters for the string.
     * @return The query to work with.
     */
    public abstract PanacheQuery<M> query(final String query, final Parameters parameters);


    @Override
    public Optional<T> findByNameSpaceAndName(final String nameSpace, final String name) {
        LOG.trace("loading: nameSpace={}, name={}", nameSpace, name);

        Optional<M> result = namespaceAndNameQuery(nameSpace, name).firstResultOptional();

        return convertResult(result);
    }

    @Override
    public Optional<T> findByUid(final UUID uid) {
        LOG.trace("loading: uid={}", uid);

        Optional<M> result = uidQuery(uid).firstResultOptional();

        return convertResult(result);
    }

    @Override
    public T save(final T object) throws OptimisticLockStoreException, DuplicateStoreException {
        return save(object, empty());
    }

    public T save(final T object, final M toSave) throws OptimisticLockStoreException, DuplicateStoreException {
        LOG.trace("persisting: {}", object);

        toSave.data(object);

        Optional<T> stored = findByNameSpaceAndName(object.getNameSpace(), object.getName());
        if (stored.isPresent()) {
            checkUid(stored.get().getMetadata(), object.getMetadata());
            checkGeneration(stored.get().getMetadata(), object.getMetadata());
        } else {
            if (toSave.uid == null) {
                toSave.uid = UUID.randomUUID();

                LOG.warn("Added UID to data: uid={}, nameSpace='{}', name='{}'", toSave.uid, toSave.nameSpace, toSave.name);
            }
        }

        toSave.updateHistory();
        persistOrUpdate(toSave);
        LOG.debug("persisted: {}", toSave);
        return toSave.data();
    }

    //
    private void checkUid(final ResourceMetadata stored, final ResourceMetadata data) throws DuplicateStoreException {
        if (
                stored.getNamespace().equals(data.getNamespace())
                        && stored.getName().equals(data.getName())
                        && !stored.getUid().equals(data.getUid())) {
            throw new DuplicateStoreException(stored, data);
        }
    }

    private void checkGeneration(final ResourceMetadata stored, final ResourceMetadata data) throws OptimisticLockStoreException {
        if (stored.getGeneration() > data.getGeneration()) {
            throw new OptimisticLockStoreException(stored.getGeneration(), data.getGeneration());
        }
    }


    @Override
    public void remove(final T object) {
        LOG.info("remove: uid={}, nameSpace={}, name={}",
                object.getUid(),
                object.getNameSpace(),
                object.getName()
        );

        remove(object.getUid());
    }

    @Override
    public void remove(String nameSpace, String name) {
        LOG.trace("remove: nameSpace='{}', name='{}'", nameSpace, name);

        namespaceAndNameQuery(nameSpace, name).stream().forEach(this::delete);
    }

    @Override
    public void remove(final UUID uid) {
        LOG.trace("remove: uid={}", uid);

        uidQuery(uid).stream().forEach(this::delete);
    }


    private PanacheQuery<M> namespaceAndNameQuery(String nameSpace, String name) {
        return query(
                "nameSpace = :nameSpace and name = :name",
                Parameters.with("nameSpace", nameSpace).and("name", name)
        );
    }

    private PanacheQuery<M> uidQuery(final UUID uid) {
        return query(
                "uid =:uid",
                Parameters.with("uid", uid)
        );
    }

    @NotNull
    private Optional<T> convertResult(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<M> result) {
        if (result.isEmpty()) {
            LOG.debug("query: no result found!");
            return Optional.empty();
        }

        LOG.debug("Loaded: {}", result);
        return Optional.of(result.get().data());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MongoResourceRepository.class.getSimpleName() + "[", "]")
                .add("hash=" + System.identityHashCode(this))
                .toString();
    }
}
