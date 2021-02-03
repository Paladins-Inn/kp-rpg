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
import de.kaiserpfalzedv.rpg.core.user.ImmutableUser;
import de.kaiserpfalzedv.rpg.core.user.ImmutableUserData;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.test.mongodb.MongoDBResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
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
    private static final UUID UID = UUID.randomUUID();
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
                            .uid(UID)

                            .created(CREATED)

                            .putAnnotations("test", "true")

                            .build()
            )
            .spec(
                    ImmutableUserData.builder()
                            .description("A discord user.")
                            .driveThruRPGApiKey("API-KEY")
                            .properties(new HashMap<>())
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
    MongoUserRepository sut;

    @Test
    public void shouldReturnEmptyOptionalWhenThereIsNoUser() {
        MDC.put("test", "not-present-without-user");

        Optional<User> result = sut.findByNameSpaceAndName("no-namespace", "no-card");
        LOG.trace("result={}", result);

        assertFalse(result.isPresent(), "There should be no card no-namespace/no-card");
    }

    @Test
    public void shouldReturnUserByNamespaceAndNameWhenThereIsAnUser() {
        MDC.put("test", "load-user-by-namespace-and-name");

        sut.save(data);

        Optional<User> result = sut.findByNameSpaceAndName(NAMESPACE, NAME);
        LOG.trace("result={}", result);

        assertTrue(result.isPresent(), "There should be an user " + NAMESPACE + "/" + NAME);
    }

    @Test
    public void shouldReturnUserByUidWhenThereIsAnUser() {
        MDC.put("test", "load-user-by-uid");

        sut.save(data);

        Optional<User> result = sut.findByUid(UID);
        LOG.trace("result={}", result);

        assertTrue(result.isPresent(), "There should be an user with UID " + UID.toString());
    }


    @AfterEach
    void tearDownEach() {
        sut.remove(data.getNameSpace(), data.getName());
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
