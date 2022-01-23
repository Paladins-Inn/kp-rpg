/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.store;

import de.kaiserpfalzedv.commons.core.resources.History;
import de.kaiserpfalzedv.commons.core.resources.Metadata;
import de.kaiserpfalzedv.commons.core.resources.Pointer;
import de.kaiserpfalzedv.commons.core.resources.Status;
import de.kaiserpfalzedv.commons.core.user.User;
import de.kaiserpfalzedv.commons.core.user.UserData;
import de.kaiserpfalzedv.commons.discord.guilds.Guild;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TestUserRepository -- tests the basic methods for storing, manipulating and deleting users.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-30
 */
@QuarkusTest
@Slf4j
public class TestMongoUserStore {
    private static final String NAMESPACE = Guild.DISCORD_NAMESPACE;
    private static final String NAME = "klenkes74#0355";
    private static final UUID UID = UUID.randomUUID();
    private static final OffsetDateTime CREATED = OffsetDateTime.now(ZoneOffset.UTC);

    private static final HashMap<String, String> DATA_ANNOTATIONS = new HashMap<>();
    /**
     * Default data created during setup of tests.
     */
    private static final User data = User.builder()
            .metadata(
                    Metadata.builder()
                            .identity(
                                    Pointer.builder()
                                            .kind(User.KIND)
                                            .apiVersion(User.API_VERSION)
                                            .nameSpace(NAMESPACE)
                                            .name(NAME)
                                            .build()
                            )
                            .uid(UID)
                            .created(CREATED)

                            .annotations(DATA_ANNOTATIONS)

                            .build()
            )
            .spec(
                    UserData.builder()
                            .description("A discord user.")
                            .driveThruRPGKey("API-KEY")
                            .properties(new HashMap<>())
                            .build()
            )
            .status(
                    Status.builder()
                            .observedGeneration(1)
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
    UserRepository sut;

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestMongoUserStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @Test
    public void shouldReturnEmptyOptionalWhenThereIsNoUser() {
        MDC.put("test", "not-present-without-user");

        Optional<User> result = sut.findByNameSpaceAndName("no-namespace", "no-card");
        log.trace("result={}", result);

        assertFalse(result.isPresent(), "There should be no card no-namespace/no-card");
    }

    @Test
    public void shouldReturnUserByNamespaceAndNameWhenThereIsAnUser() {
        MDC.put("test", "load-user-by-namespace-and-name");

        sut.save(data);

        Optional<User> result = sut.findByNameSpaceAndName(NAMESPACE, NAME);
        log.trace("result={}", result);

        assertTrue(result.isPresent(), "There should be an user " + NAMESPACE + "/" + NAME);
    }

    @Test
    public void shouldReturnUserByUidWhenThereIsAnUser() {
        MDC.put("test", "load-user-by-uid");

        sut.save(data);

        Optional<User> result = sut.findByUid(UID);
        log.trace("result={}", result);

        assertTrue(result.isPresent(), "There should be an user with UID " + UID);
    }

    @Test
    public void shouldBeAMongoBasedImplementation() {
        MDC.put("test", "mongo-based-implementation");

        assertNotNull(sut);
    }

    @AfterEach
    void tearDownEach() {
        sut.remove(data);
        MDC.remove("test");
    }
}
