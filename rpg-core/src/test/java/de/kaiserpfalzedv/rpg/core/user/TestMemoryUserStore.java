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

package de.kaiserpfalzedv.rpg.core.user;

import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.store.OptimisticLockStoreException;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TestMemoryUserStore -- checks if the memory store behaves correctly.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
public class TestMemoryUserStore {
    private static final Logger LOG = LoggerFactory.getLogger(TestMemoryUserStore.class);

    private static final UUID DATA_UID = UUID.randomUUID();
    private static final String DATA_NAMESPACE = "testNS";
    private static final String DATA_NAME = "testName";
    private static final OffsetDateTime DATA_CREATED = OffsetDateTime.now(Clock.systemUTC());
    private static final String DATA_API_KEY = "test-api-key";

    private static final UUID OTHER_UID = UUID.randomUUID();
    private static final String OTHER_NAMESPACE = "otherNS";
    private static final String OTHER_NAME = "otherName";
    private static final OffsetDateTime OTHER_CREATED = OffsetDateTime.now(Clock.systemUTC());
    private static final String OTHER_API_KEY = "other-api-key";

    private static final User DATA = User.builder()
            .withMetadata(
                    generateMetadata(DATA_NAMESPACE, DATA_NAME, DATA_UID, DATA_CREATED, null, 0L)
            )
            .withSpec(Optional.of(
                    UserData.builder()
                            .withDriveThruRPGApiKey(Optional.of(DATA_API_KEY))
                            .withProperties(new HashMap<>())
                            .build()
            ))
            .build();

    private static final User OTHER =
            User.builder()
                    .withMetadata(
                            generateMetadata(OTHER_NAMESPACE, OTHER_NAME, OTHER_UID, OTHER_CREATED, null, 0L)
                    )
                    .withSpec(Optional.of(
                            UserData.builder()
                                    .withDriveThruRPGApiKey(Optional.of(OTHER_API_KEY))
                                    .withProperties(new HashMap<>())
                                    .build()
                    ))
                    .build();


    /**
     * service under test.
     */
    private final UserStoreService sut;

    public TestMemoryUserStore() {
        this.sut = new MemoryUserStore();
    }

    @Test
    void shouldBeAMemoryUserStoreService() {
        MDC.put("test", "store-is-memory-based");

        assertTrue(sut instanceof MemoryUserStore);
    }

    @Test
    void shouldSaveNewDataWhenDataIsNotStoredYet() {
        MDC.put("test", "store-new-data");

        sut.save(DATA);

        Optional<User> result = sut.findByNameSpaceAndName(DATA_NAMESPACE, DATA_NAME);
        LOG.trace("result: {}", result);

        assertTrue(result.isPresent(), "The data should have been stored!");
        assertEquals(DATA, result.get());
    }

    @Test
    void shouldSaveNewDataWhenDataIsAlreadyStoredYet() {
        MDC.put("test", "update-stored-data");

        sut.save(DATA); // store data first time

        sut.save(DATA); // update data

        Optional<User> result = sut.findByNameSpaceAndName(DATA_NAMESPACE, DATA_NAME);
        LOG.trace("result: {}", result);

        assertTrue(result.isPresent(), "The data should have been stored!");
        assertNotEquals(DATA, result.get());

        assertEquals(1L, result.get().getMetadata().getGeneration());
    }

    /**
     * Sets up a metadata set.
     *
     * @param namespace the namespace of the data set.
     * @param name      the name of the data set.
     * @param uid       UUID of the data set.
     * @return The generated metadata
     */
    private static ResourceMetadata generateMetadata(
            @NotNull final String namespace,
            @NotNull final String name,
            @NotNull final UUID uid,
            @NotNull final OffsetDateTime created,
            @SuppressWarnings("SameParameterValue") final OffsetDateTime deleted,
            final Long generation
    ) {
        return ResourceMetadata.builder()
                .withKind(User.KIND)
                .withApiVersion(User.API_VERSION)

                .withNamespace(namespace)
                .withName(name)
                .withUid(uid)

                .withCreated(created)
                .withDeleted(Optional.ofNullable(deleted))
                .withGeneration(generation)

                .build();
    }

    @Test
    public void shouldSaveOtherDataSetsWhenDataIsAlreadyStored() {
        MDC.put("test", "save-other-data");

        sut.save(DATA);

        sut.save(OTHER);

        Optional<User> result = sut.findByUid(OTHER_UID);

        assertTrue(result.isPresent(), "there should be a user resource defined by this UID!");
        assertEquals(OTHER, result.get());
    }

    @Test
    public void shouldDeleteByNameWhenTheDataExists() {
        MDC.put("test", "delete-existing-by-name");

        sut.save(DATA);
        sut.remove(DATA_NAMESPACE, DATA_NAME);

        Optional<User> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByUidWhenTheDataExists() {
        MDC.put("test", "delete-existing-by-uid");

        sut.save(DATA);
        sut.remove(DATA_UID);

        Optional<User> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByObjectWhenTheDataExists() {
        MDC.put("test", "delete-existing-by-uid");

        sut.save(DATA);
        sut.remove(DATA);

        Optional<User> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }
    @Test
    public void shouldDeleteByNameWhenTheDataDoesNotExists() {
        MDC.put("test", "delete-non-existing-by-name");

        sut.remove(DATA_NAMESPACE, DATA_NAME);

        Optional<User> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByUidWhenTheDataDoesNotExists() {
        MDC.put("test", "delete-non-existing-by-uid");

        sut.remove(DATA_UID);

        Optional<User> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByObjectWhenTheDataDoesNotExists() {
        MDC.put("test", "delete-non-existing-by-uid");

        sut.remove(DATA);

        Optional<User> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }


    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestMemoryUserStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldThrowOptimisticLockExceptionWhenTheNewGenerationIsNotHighEnough() {
        MDC.put("test", "throw-optimistic-lock-exception");

        sut.save(
                User.builder()
                        .withMetadata(
                                generateMetadata(DATA_NAMESPACE, DATA_NAME, DATA_UID, DATA_CREATED, null, 100L)

                        )
                        .withSpec(Optional.of(
                                UserData.builder()
                                        .withDriveThruRPGApiKey(DATA.getSpec().orElseThrow().getDriveThruRPGApiKey())
                                        .withProperties(DATA.getSpec().get().getProperties())
                                        .build()
                        ))
                        .build()
        );

        try {
            sut.save(DATA);

            fail("There should have been an OptimisticLockStoreException!");
        } catch (OptimisticLockStoreException e) {
            // every thing is fine. We wanted this exception
        }
    }
}
