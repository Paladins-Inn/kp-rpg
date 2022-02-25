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

import de.kaiserpfalzedv.commons.core.resources.History;
import de.kaiserpfalzedv.commons.core.resources.Metadata;
import de.kaiserpfalzedv.commons.core.resources.Pointer;
import de.kaiserpfalzedv.commons.core.resources.Status;
import de.kaiserpfalzedv.commons.discord.guilds.Guild;
import de.kaiserpfalzedv.commons.discord.guilds.GuildData;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TestGuildRepository -- tests the basic methods for storing, manipulating and deleting users.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-30
 */
@QuarkusTest
@Slf4j
public class TestJPAGuildStore {
    private static final String NAMESPACE = Guild.DISCORD_NAMESPACE;
    private static final String NAME = "Paladins Inn";
    private static final UUID UID = UUID.randomUUID();
    private static final OffsetDateTime CREATED = OffsetDateTime.now(ZoneOffset.UTC);
    private static final String PREFIX = "test!";

    private static final HashMap<String, String> DATA_ANNOTATIONS = new HashMap<>();
    /**
     * Default data created during setup of tests.
     */
    private static final Guild DATA = Guild.builder()
            .metadata(Metadata.builder()
                    .identity(Pointer.builder()
                            .kind(Guild.KIND)
                            .apiVersion(Guild.API_VERSION)
                            .nameSpace(NAMESPACE)
                            .name(NAME)
                            .build()
                    )
                    .uid(UID)
                    .created(CREATED)
                    .annotations(DATA_ANNOTATIONS)
                    .generation(0)
                    .build()
            )
            .spec(
                    GuildData.builder()
                            .prefix(PREFIX)
                            .properties(new HashMap<>())
                            .build()
            )
            .status(
                    Status.builder()
                            .observedGeneration(0)
                            .history(Collections.singletonList(
                                    History.builder()
                                            .status("created")
                                            .timeStamp(CREATED)
                                            .build()
                            ))
                            .build()
            )
            .build();

    static {
        DATA_ANNOTATIONS.put("test", "true");
    }

    @Inject
    GuildRepository sut;

    @Test
    public void shouldBeMongoBasedImplementation() {
        MDC.put("test", "mongo-based-service");

        log.trace("sut is of type: {}", sut.getClass().getCanonicalName());
        assertTrue(GuildRepository.class.isAssignableFrom(sut.getClass()));
    }

    @Test
    public void shouldReturnEmptyOptionalWhenThereIsNoGuild() {
        MDC.put("test", "not-present-without-guild");

        Optional<Guild> result = sut.findByNameSpaceAndName("no-namespace", "no-guild");
        log.trace("result={}", result);

        assertFalse(result.isPresent(), "There should be no guild no-namespace/no-guild");
    }


    @Test
    public void shouldUseTheMongoGuildStore() {
        MDC.put("test", "use-mongo-store");

        assertNotNull(sut);
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @Test
    public void shouldReturnGuildByNamespaceAndNameWhenThereIsAnGuild() {
        MDC.put("test", "load-guild-by-namespace-and-name");

        Optional<Guild> result = sut.findByNameSpaceAndName(NAMESPACE, NAME);
        log.trace("result={}", result);

        assertTrue(result.isPresent(), "There should be an user " + NAMESPACE + "/" + NAME);
    }

    @Test
    public void shouldReturnGuildByUidWhenThereIsAnGuild() {
        MDC.put("test", "load-user-by-uid");

        Optional<Guild> result = sut.findByUid(UID);
        log.trace("result={}", result);

        assertTrue(result.isPresent(), "There should be a guild with UID " + UID);
    }


    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestJPAGuildStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        new GuildRepository().remove(DATA);
        MDC.clear();
    }

    @BeforeEach
    void loadData() {
        sut.save(DATA);
    }

    @AfterEach
    void deleteData() {
        sut.remove(DATA);
    }
}
