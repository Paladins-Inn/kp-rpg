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

package de.kaiserpfalzedv.rpg.store.guilds;

import de.kaiserpfalzedv.rpg.core.resources.ResourceHistory;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ResourceStatus;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildData;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildStoreService;
import de.kaiserpfalzedv.rpg.test.mongodb.MongoDBResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.time.Clock;
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
@QuarkusTestResource(MongoDBResource.class)
@Slf4j
public class TestMongoGuildStore {
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
            .withMetadata(
                    ResourceMetadata.builder()
                            .withKind(Guild.KIND)
                            .withApiVersion(Guild.API_VERSION)

                            .withNamespace(NAMESPACE)
                            .withName(NAME)
                            .withUid(UID)

                            .withCreated(CREATED)

                            .withAnnotations(DATA_ANNOTATIONS)

                            .build()
            )
            .withSpec(Optional.of(
                    GuildData.builder()
                            .withPrefix(PREFIX)
                            .withProperties(new HashMap<>())
                            .build()
            ))
            .withStatus(Optional.of(
                    ResourceStatus.builder()
                            .withObservedGeneration(0L)
                            .withHistory(Collections.singletonList(
                                    ResourceHistory.builder()
                                            .withStatus("created")
                                            .withTimeStamp(CREATED)
                                            .build()
                            ))
                            .build()
            ))
            .build();

    static {
        DATA_ANNOTATIONS.put("test", "true");
    }

    private final GuildStoreService sut;

    @Inject
    public TestMongoGuildStore(final GuildStoreService store) {
        this.sut = store;

        log.debug("Loaded store: {}", store);
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestMongoGuildStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        new MongoGuildStore().remove(DATA);
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

    @Test
    public void shouldBeMongoBasedImplementation() {
        MDC.put("test", "mongo-based-service");

        log.trace("sut is of type: {}", sut.getClass().getCanonicalName());
        assertTrue(MongoGuildStore.class.isAssignableFrom(sut.getClass()));
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

        assertTrue(sut instanceof MongoGuildStore);
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

    @Test
    public void shouldRewriteGuildsWithKindGuildWhenTheStoredDataIsOfKindUser() {
        MDC.put("test", "repair-wrong-saved-guild");

        Guild invalid = Guild.builder()
                .withMetadata(
                        ResourceMetadata.builder()
                                .withKind(User.KIND)
                                .withNamespace(DATA.getNameSpace())
                                .withName("invalid-kind")
                                .withGeneration(DATA.getGeneration())

                                .withOwner(DATA.getMetadata().getOwner())
                                .withAnnotations(DATA.getMetadata().getAnnotations())
                                .withLabels(DATA.getMetadata().getLabels())

                                .withCreated(OffsetDateTime.now(Clock.systemUTC()))
                                .withDeleted(DATA.getMetadata().getDeleted())

                                .build()
                )
                .withSpec(Optional.empty())
                .withStatus(Optional.empty())
                .build();

        sut.save(invalid);

        Optional<Guild> result = sut.findByNameSpaceAndName(DATA.getNameSpace(), "invalid-kind");
        log.trace("result={}", result);

        assertTrue(result.isPresent());
        assertEquals(Guild.KIND, result.get().getKind());
    }
}
