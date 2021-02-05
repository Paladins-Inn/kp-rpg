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

package de.kaiserpfalzedv.rpg.core.dice.history;

import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.SerializableList;
import de.kaiserpfalzedv.rpg.core.store.OptimisticLockStoreException;
import de.kaiserpfalzedv.rpg.core.user.User;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TestMemoryUserStore -- checks if the memory store behaves correctly.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
@QuarkusTest
public class TestMemoryRollHistoryStore {
    private static final Logger LOG = LoggerFactory.getLogger(TestMemoryRollHistoryStore.class);

    private static final UUID DATA_UID = UUID.randomUUID();
    private static final String DATA_NAMESPACE = "testNS";
    private static final String DATA_NAME = "testName";
    private static final OffsetDateTime DATA_CREATED = OffsetDateTime.now(Clock.systemUTC());

    private static final SerializableList<RollHistoryEntry> ENTRIES = new SerializableList<>();

    private static final RollHistory DATA = ImmutableRollHistory.builder()
            .metadata(
                    generateMetadata(DATA_NAMESPACE, DATA_NAME, DATA_UID, DATA_CREATED)
            )
            .spec(ENTRIES)
            .build();


    /**
     * service under test.
     */
    private final RollHistoryStoreService sut;

    @Inject
    public TestMemoryRollHistoryStore(final RollHistoryStoreService store) {
        this.sut = store;
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestMemoryRollHistoryStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    /**
     * Sets up a metadata set.
     *
     * @param namespace the namespace of the data set.
     * @param name      the name of the data set.
     * @param uid       UUID of the data set.
     * @return The generated metadata
     */
    @SuppressWarnings("SameParameterValue")
    private static ImmutableResourceMetadata generateMetadata(
            final String namespace,
            final String name,
            final UUID uid,
            final OffsetDateTime created
    ) {
        return ImmutableResourceMetadata.builder()
                .kind(User.KIND)
                .apiVersion(User.API_VERSION)

                .namespace(namespace)
                .name(name)
                .uid(uid)

                .created(created)

                .build();
    }

    @Test
    void shouldBeAMemoryUserStoreService() {
        MDC.put("test", "store-is-memory-based");

        assertNotNull(sut);
    }

    @Test
    void shouldSaveNewDataWhenDataIsNotStoredYet() {
        MDC.put("test", "store-new-data");

        sut.save(DATA);

        Optional<RollHistory> result = sut.findByNameSpaceAndName(DATA_NAMESPACE, DATA_NAME);
        LOG.trace("result: {}", result);

        assertTrue(result.isPresent(), "The data should have been stored!");
        assertEquals(DATA, result.get());
    }

    @Test
    void shouldSaveNewDataWhenDataIsAlreadyStoredYet() {
        MDC.put("test", "update-stored-data");

        sut.save(DATA); // store data first time

        sut.save(DATA); // update data

        Optional<RollHistory> result = sut.findByNameSpaceAndName(DATA_NAMESPACE, DATA_NAME);
        LOG.trace("result: {}", result);

        assertTrue(result.isPresent(), "The data should have been stored!");
        assertNotEquals(DATA, result.get());

        assertEquals(1L, result.get().getMetadata().getGeneration());
    }

    @Test
    void shouldThrowOptimisticLockExceptionWhenTheNewGenerationIsNotHighEnough() {
        MDC.put("test", "throw-optimistic-lock-exception");

        sut.save(
                ImmutableRollHistory.builder()
                        .from(DATA)
                        .metadata(
                                ImmutableResourceMetadata.builder()
                                        .from(DATA.getMetadata())
                                        .generation(100L)
                                        .build()
                        )
                        .build()
        );

        try {
            sut.save(DATA);

            fail("There should have been an OptimisticLockStoreException!");
        } catch (OptimisticLockStoreException e) {
            // every thing is fine. We wanted this exception
        }
    }

    @Test
    public void shouldDeleteByNameWhenTheDataExists() {
        MDC.put("test", "delete-existing-by-name");

        sut.save(DATA);
        sut.remove(DATA_NAMESPACE, DATA_NAME);

        Optional<RollHistory> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByUidWhenTheDataExists() {
        MDC.put("test", "delete-existing-by-uid");

        sut.save(DATA);
        sut.remove(DATA_UID);

        Optional<RollHistory> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByObjectWhenTheDataExists() {
        MDC.put("test", "delete-existing-by-uid");

        sut.save(DATA);
        sut.remove(DATA);

        Optional<RollHistory> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByNameWhenTheDataDoesNotExists() {
        MDC.put("test", "delete-non-existing-by-name");

        sut.remove(DATA_NAMESPACE, DATA_NAME);

        Optional<RollHistory> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByUidWhenTheDataDoesNotExists() {
        MDC.put("test", "delete-non-existing-by-uid");

        sut.remove(DATA_UID);

        Optional<RollHistory> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByObjectWhenTheDataDoesNotExists() {
        MDC.put("test", "delete-non-existing-by-uid");

        sut.remove(DATA);

        Optional<RollHistory> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }
}
