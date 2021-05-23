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

package de.kaiserpfalzedv.rpg.integrations.discord;

import de.kaiserpfalzedv.rpg.core.resources.Metadata;
import de.kaiserpfalzedv.rpg.core.store.OptimisticLockStoreException;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildData;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildStoreService;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.MemoryGuildStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.slf4j.MDC;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalToObject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TestMemoryGuildStore -- checks if the memory store behaves correctly.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
@Slf4j
public class TestMemoryGuildStore {
    private static final UUID DATA_UID = UUID.randomUUID();
    private static final String DATA_NAMESPACE = "testNS";
    private static final String DATA_NAME = "testName";
    private static final OffsetDateTime DATA_CREATED = OffsetDateTime.now(Clock.systemUTC());
    private static final String DATA_PREFIX = "data!";

    private static final UUID OTHER_UID = UUID.randomUUID();
    private static final String OTHER_NAMESPACE = "otherNS";
    private static final String OTHER_NAME = "otherName";
    private static final OffsetDateTime OTHER_CREATED = OffsetDateTime.now(Clock.systemUTC());
    private static final String OTHER_PREFIX = "other!";

    private static final Guild DATA = Guild.builder()
            .withKind(Guild.KIND)
            .withApiVersion(Guild.API_VERSION)
            .withNamespace(DATA_NAMESPACE)
            .withName(DATA_NAME)
            .withUid(DATA_UID)
            .withGeneration(0L)

            .withMetadata(
                    generateMetadata(DATA_CREATED, null)
            )
            .withSpec(
                    GuildData.builder()
                            .withPrefix(DATA_PREFIX)
                            .withProperties(new HashMap<>())
                            .build()
            )
            .build();

    private static final Guild OTHER = Guild.builder()
            .withNamespace(OTHER_NAMESPACE)
            .withName(OTHER_NAME)
            .withUid(OTHER_UID)
            .withMetadata(
                    generateMetadata(OTHER_CREATED, null)
            )
            .withSpec(
                    GuildData.builder()
                            .withPrefix(OTHER_PREFIX)
                            .withProperties(new HashMap<>())
                            .build()
            )
            .build();


    /**
     * service under test.
     */
    private GuildStoreService sut;

    public TestMemoryGuildStore() {
        this.sut = new MemoryGuildStore();
    }

    @Test
    void shouldBeAMemoryBasedService() {
        MDC.put("test", "memory-based-store");

        assertTrue(sut instanceof MemoryGuildStore);
    }

    /**
     * Sets up a metadata set.
     *
     * @return The generated metadata
     */
    private static Metadata generateMetadata(
            final OffsetDateTime created,
            final OffsetDateTime deleted
    ) {
        return Metadata.builder()
                .withCreated(created)
                .withDeleted(deleted)

                .build();
    }

    @Test
    void shouldSaveNewDataWhenDataIsNotStoredYet() {
        MDC.put("test", "store-new-data");

        sut.save(DATA);

        Optional<Guild> result = sut.findByNameSpaceAndName(DATA_NAMESPACE, DATA_NAME);
        log.trace("result: {}", result);

        assertTrue(result.isPresent(), "The data should have been stored!");
        assertEquals(DATA, result.get());
    }

    @Test
    void shouldSaveNewDataWhenDataIsAlreadyStoredYet() {
        MDC.put("test", "update-stored-data");

        sut.save(DATA); // store data first time

        sut.save(DATA); // update data

        Optional<Guild> result = sut.findByNameSpaceAndName(DATA_NAMESPACE, DATA_NAME);
        log.trace("result: {}", result);

        assertTrue(result.isPresent(), "The data should have been stored!");
        assertThat(result.get(), equalToObject(
                DATA.toBuilder()
                        .withGeneration(DATA.getGeneration() + 1)
                        .build()
                )
        );
        assertEquals(DATA.getGeneration() + 1, result.get().getGeneration());
    }

    @Test
    public void shouldSaveOtherDataSetsWhenDataIsAlreadyStored() {
        MDC.put("test", "save-other-data");

        sut.save(DATA);

        sut.save(OTHER);

        Optional<Guild> result = sut.findByUid(OTHER_UID);

        assertTrue(result.isPresent(), "there should be a user resource defined by this UID!");
        assertEquals(OTHER, result.get());
    }

    @Test
    public void shouldDeleteByNameWhenTheDataExists() {
        MDC.put("test", "delete-existing-by-name");

        sut.save(DATA);
        sut.remove(DATA_NAMESPACE, DATA_NAME);

        Optional<Guild> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByUidWhenTheDataExists() {
        MDC.put("test", "delete-existing-by-uid");

        sut.save(DATA);
        sut.remove(DATA_UID);

        Optional<Guild> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByObjectWhenTheDataExists() {
        MDC.put("test", "delete-existing-by-uid");

        sut.save(DATA);
        sut.remove(DATA);

        Optional<Guild> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }
    @Test
    public void shouldDeleteByNameWhenTheDataDoesNotExists() {
        MDC.put("test", "delete-non-existing-by-name");

        sut.remove(DATA_NAMESPACE, DATA_NAME);

        Optional<Guild> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByUidWhenTheDataDoesNotExists() {
        MDC.put("test", "delete-non-existing-by-uid");

        sut.remove(DATA_UID);

        Optional<Guild> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }

    @Test
    public void shouldDeleteByObjectWhenTheDataDoesNotExists() {
        MDC.put("test", "delete-non-existing-by-uid");

        sut.remove(DATA);

        Optional<Guild> result = sut.findByUid(DATA_UID);
        assertFalse(result.isPresent(), "Data should have been deleted!");
    }


    @BeforeEach
    void setUpEach() {
        sut = new MemoryGuildStore();
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestMemoryGuildStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldThrowOptimisticLockExceptionWhenTheNewGenerationIsNotHighEnough() {
        MDC.put("test", "throw-optimistic-lock-exception");

        sut.save(
                DATA.toBuilder()
                        .withGeneration(DATA.getGeneration() + 100)
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
