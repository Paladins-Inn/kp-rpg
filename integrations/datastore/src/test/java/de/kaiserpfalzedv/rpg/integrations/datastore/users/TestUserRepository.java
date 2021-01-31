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

package de.kaiserpfalzedv.rpg.integrations.datastore.users;

import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceHistory;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceStatus;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.user.ImmutableUser;
import de.kaiserpfalzedv.rpg.core.user.ImmutableUserData;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.test.mongodb.MongoDBResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TestUserRepository -- tests the basic methods for storing, manipulating and deleting users.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-30
 */
@QuarkusTest
@QuarkusTestResource(MongoDBResource.class)
public class TestUserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(TestUserRepository.class);

    private static final String NAMESPACE = "discord";
    private static final String NAME = "klenkes74#0355";
    private static final UUID USER_UID = UUID.randomUUID();
    private static final OffsetDateTime CREATED = OffsetDateTime.now(ZoneOffset.UTC);

    /**
     * Default data created during setup of tests.
     */
    private static final User data = ImmutableUser.builder()
            .metadata(
                    ImmutableResourceMetadata.builder()
                            .kind(User.KIND)
                            .apiVersion(User.API_VERSION)

                            .namespace(NAMESPACE)
                            .name(NAME)
                            .uid(USER_UID)
                            .selfLink(ResourceMetadata.generateSelfLink("", User.KIND, User.API_VERSION, USER_UID))

                            .created(CREATED)
                            .generation(0L)

                            .putAnnotations("test", "true")

                            .build()
            )
            .spec(
                    ImmutableUserData.builder()
                            .description("A discord user.")
                            .driveThruRPGApiKey("API-KEY")
                    .build()
            )
            .status(
                    ImmutableResourceStatus.builder()
                            .observedGeneration(1L)
                            .addHistory(
                                    ImmutableResourceHistory.builder()
                                            .status("created")
                                            .timeStamp(CREATED)
                                            .build()
                            )
                            .build()
            )
            .build();

    @Inject
    UserRepository sut;

    @Test
    public void shouldReturnEmptyOptionalWhenThereIsNoUser() {
        MDC.put("test", "not-present-without-user");

        Optional<User> result = sut.findByNameSpaceAndName("no-namespace", "no-card");
        LOG.trace("result=", result);

        assertFalse(result.isPresent(), "There should be no card no-namespace/no-card");
    }

    @Test
    public void shouldReturnUserByNamespaceAndNameWhenThereIsAnUser() {
        MDC.put("test", "load-user-by-namespace-and-name");

        Optional<User> result = sut.findByNameSpaceAndName(NAMESPACE, NAME);
        LOG.trace("result=", result);

        assertTrue(result.isPresent(), "There should be an user " + NAMESPACE + "/" + NAME);
    }

    @Test
    public void shouldReturnUserByUidWhenThereIsAnUser() {
        MDC.put("test", "load-user-by-uid");

        Optional<User> result = sut.findByUid(USER_UID);
        LOG.trace("result=", result);

        assertTrue(result.isPresent(), "There should be an user with UID " + USER_UID.toString());
    }

    @BeforeEach
    void setUpEach() {
        sut.persist(data);
    }

    @AfterEach
    void tearDownEach() {
        sut.delete(data);

        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestUserRepository.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }
}
