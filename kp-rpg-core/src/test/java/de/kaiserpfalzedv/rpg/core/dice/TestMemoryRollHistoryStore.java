/*
 * Copyright (c) &today.year Kaiserpfalz EDV-Service, Roland T. Lichti
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.core.dice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import de.kaiserpfalzedv.commons.api.resources.SerializableList;
import de.kaiserpfalzedv.commons.api.store.OptimisticLockStoreException;
import de.kaiserpfalzedv.commons.api.user.User;
import de.kaiserpfalzedv.commons.core.resources.MetadataImpl;
import de.kaiserpfalzedv.commons.core.resources.PointerImpl;
import de.kaiserpfalzedv.rpg.core.dice.history.MemoryRollHistoryStore;
import de.kaiserpfalzedv.rpg.core.dice.history.RollHistory;
import de.kaiserpfalzedv.rpg.core.dice.history.RollHistoryEntry;
import de.kaiserpfalzedv.rpg.core.dice.history.RollHistoryStoreService;
import lombok.extern.slf4j.Slf4j;

/**
 * TestMemoryUserStore -- checks if the memory store behaves correctly.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
@Slf4j
public class TestMemoryRollHistoryStore {
    private static final UUID DATA_UID = UUID.randomUUID();
    private static final String DATA_NAMESPACE = "testNS";
    private static final String DATA_NAME = "testName";
    private static final OffsetDateTime DATA_CREATED = OffsetDateTime.now(Clock.systemUTC());

    private static final SerializableList<RollHistoryEntry> ENTRIES = new SerializableList<>();

    private static final RollHistory DATA = RollHistory.builder()
            .metadata(MetadataImpl.builder()
                    .identity(
                            PointerImpl.builder()
                                    .kind(User.KIND)
                                    .apiVersion(User.API_VERSION)
                                    .nameSpace(DATA_NAMESPACE)
                                    .name(DATA_NAME)
                                    .build()
                    )
                    .uid(DATA_UID)
                    .generation(0)
                    .created(DATA_CREATED)
                    .build()
            )
            .spec(ENTRIES)
            .build();


    /**
     * service under test.
     */
    private RollHistoryStoreService sut;


    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestMemoryRollHistoryStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @BeforeEach
    public void setupEach() {
        sut = new MemoryRollHistoryStore();
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
        log.trace("result: {}", result);

        assertTrue(result.isPresent(), "The data should have been stored!");
        assertEquals(DATA, result.get());
    }

    @Test
    void shouldSaveNewDataWhenDataIsAlreadyStoredYet() {
        MDC.put("test", "update-stored-data");

        RollHistory data = sut.save(DATA); // store data first time

        sut.save(data); // update data

        Optional<RollHistory> result = sut.findByNameSpaceAndName(DATA_NAMESPACE, DATA_NAME);
        log.trace("result: {}", result);
        log.trace("original: {}", DATA);
        assertTrue(result.isPresent(), "The data should have been stored!");

        assertEquals(1, result.get().getGeneration());
    }

    @Test
    void shouldThrowOptimisticLockExceptionWhenTheNewGenerationIsNotHighEnough() {
        MDC.put("test", "throw-optimistic-lock-exception");

        sut.save(
                DATA.toBuilder()
                        .metadata(((MetadataImpl)DATA.getMetadata()).toBuilder()
                                .generation(1)
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
