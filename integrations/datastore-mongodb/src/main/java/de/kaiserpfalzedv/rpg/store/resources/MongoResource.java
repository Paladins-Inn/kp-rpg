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

package de.kaiserpfalzedv.rpg.store.resources;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.kaiserpfalzedv.rpg.core.resources.Resource;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.mongodb.panache.PanacheUpdate;
import io.quarkus.mongodb.panache.runtime.JavaMongoOperations;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.impl.GenerateBridge;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.beans.Transient;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


/**
 * MongoResource -- The base mongo resource for all kp-rpg mongo entities.
 * <p>
 * This is basically an opinionated copy of the {@link io.quarkus.mongodb.panache.PanacheMongoEntity} since I need the
 * lombok {@link SuperBuilder} which won't work when not all classes up to the base class are annotated with it.
 *
 * @param <T> The resource type to save.
 */
@SuppressWarnings("unused")
@MongoEntity
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class MongoResource<T extends Resource<?>> implements Serializable {
    public ObjectId id;

    @EqualsAndHashCode.Include
    @BsonProperty("uid")
    public UUID uid;

    @BsonProperty("nameSpace")
    public String nameSpace;
    @BsonProperty("name")
    public String name;


    @BsonProperty("metadata")
    public MongoMetaData metadata;
    @BsonProperty("status")
    public MongoResourceStatus status;

    @JsonIgnore
    @Transient
    @GenerateBridge(
            targetReturnTypeErased = true
    )
    public static <T extends PanacheMongoEntityBase> T findById(Object id) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Optional<T> findByIdOptional(Object id) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> PanacheQuery<T> find(String query, Object... params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> PanacheQuery<T> find(String query, Sort sort, Object... params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> PanacheQuery<T> find(String query, Map<String, Object> params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> PanacheQuery<T> find(String query, Sort sort, Map<String, Object> params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> PanacheQuery<T> find(String query, Parameters params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> PanacheQuery<T> find(String query, Sort sort, Parameters params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> PanacheQuery<T> find(Document query) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> PanacheQuery<T> find(Document query, Document sort) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> PanacheQuery<T> findAll() {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> PanacheQuery<T> findAll(Sort sort) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> List<T> list(String query, Object... params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> List<T> list(String query, Sort sort, Object... params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> List<T> list(String query, Map<String, Object> params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> List<T> list(String query, Sort sort, Map<String, Object> params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> List<T> list(String query, Parameters params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> List<T> list(String query, Sort sort, Parameters params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> List<T> list(Document query) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> List<T> list(Document query, Document sort) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> List<T> listAll() {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> List<T> listAll(Sort sort) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Stream<T> stream(String query, Object... params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Stream<T> stream(String query, Sort sort, Object... params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Stream<T> stream(String query, Map<String, Object> params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Stream<T> stream(String query, Sort sort, Map<String, Object> params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Stream<T> stream(String query, Parameters params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Stream<T> stream(String query, Sort sort, Parameters params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Stream<T> stream(Document query) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Stream<T> stream(Document query, Document sort) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Stream<T> streamAll() {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> Stream<T> streamAll(Sort sort) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static long count() {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static long count(String query, Object... params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static long count(String query, Map<String, Object> params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static long count(String query, Parameters params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static long count(Document query) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static long deleteAll() {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static boolean deleteById(Object id) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static long delete(String query, Object... params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static long delete(String query, Map<String, Object> params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static long delete(String query, Parameters params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static long delete(Document query) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    public static void persist(Iterable<?> entities) {
        JavaMongoOperations.INSTANCE.persist(entities);
    }

    @JsonIgnore
    @Transient
    public static void persist(Stream<?> entities) {
        JavaMongoOperations.INSTANCE.persist(entities);
    }

    @JsonIgnore
    @Transient
    public static void persist(Object firstEntity, Object... entities) {
        JavaMongoOperations.INSTANCE.persist(firstEntity, entities);
    }

    @JsonIgnore
    @Transient
    public static void update(Iterable<?> entities) {
        JavaMongoOperations.INSTANCE.update(entities);
    }

    @JsonIgnore
    @Transient
    public static void update(Stream<?> entities) {
        JavaMongoOperations.INSTANCE.update(entities);
    }

    @JsonIgnore
    @Transient
    public static void update(Object firstEntity, Object... entities) {
        JavaMongoOperations.INSTANCE.update(firstEntity, entities);
    }

    @JsonIgnore
    @Transient
    public static void persistOrUpdate(Iterable<?> entities) {
        JavaMongoOperations.INSTANCE.persistOrUpdate(entities);
    }

    @JsonIgnore
    @Transient
    public static void persistOrUpdate(Stream<?> entities) {
        JavaMongoOperations.INSTANCE.persistOrUpdate(entities);
    }

    public static void persistOrUpdate(Object firstEntity, Object... entities) {
        JavaMongoOperations.INSTANCE.persistOrUpdate(firstEntity, entities);
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static PanacheUpdate update(String update, Object... params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static PanacheUpdate update(String update, Map<String, Object> params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static PanacheUpdate update(String update, Parameters params) {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static <T extends PanacheMongoEntityBase> MongoCollection<T> mongoCollection() {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    @JsonIgnore
    @Transient
    @GenerateBridge
    public static MongoDatabase mongoDatabase() {
        throw JavaMongoOperations.INSTANCE.implementationInjectionMissing();
    }

    /**
     * Loads data from the Resource.
     *
     * @param data The resource to load data from.
     */
    @JsonInclude
    @Transient
    public void data(final T data) {
        if (data.getMetadata().isAnnotated("mongo-id")) {
            id = new ObjectId(data.getMetadata().getAnnotations().get("mongo-id")); // reload the mongodb id from the annotations.
        }
        uid = data.getUid();
        nameSpace = data.getNameSpace();
        name = data.getName();

        metadata = new MongoMetaData(data.getMetadata());
        metadata.annotations.remove("mongo-id"); // remove the mongodb id if it is in there ...

        status = new MongoResourceStatus(data.getStatus());
    }

    @JsonInclude
    @Transient
    public void updateHistory() {
        if (status == null) {
            status = new MongoResourceStatus();
        }
        status.updateHistory(metadata);
    }

    /**
     * @return The Resource compatible equivalent of the local data.
     */
    @JsonIgnore
    @Transient
    public abstract T data();

    @JsonIgnore
    @Transient
    public void persist() {
        JavaMongoOperations.INSTANCE.persist(this);
    }

    @JsonIgnore
    @Transient
    public void update() {
        JavaMongoOperations.INSTANCE.update(this);
    }

    @JsonIgnore
    @Transient
    public void persistOrUpdate() {
        JavaMongoOperations.INSTANCE.persistOrUpdate(this);
    }

    @JsonIgnore
    @Transient
    public void delete() {
        JavaMongoOperations.INSTANCE.delete(this);
    }
}
