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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
@Slf4j
public class TestUser {
    private static final UUID DATA_UID = UUID.randomUUID();
    private static final String DATA_NAMESPACE = "testNS";
    private static final String DATA_NAME = "testName";
    private static final OffsetDateTime DATA_CREATED = OffsetDateTime.now(Clock.systemUTC());
    private static final String DATA_API_KEY = "test-api-key";
    private static final String DISCORD_ID = "123123591";

    private static final HashMap<String, String> DATA_PROPERTIES = new HashMap<>();

    static {
        DATA_PROPERTIES.put("discord-id", DISCORD_ID);
    }

    private static final User DATA = User.builder()
            .withMetadata(
                    generateMetadata(DATA_NAMESPACE, DATA_NAME, DATA_UID, DATA_CREATED)
            )
            .withSpec(Optional.of(
                    UserData.builder()
                            .withDriveThruRPGApiKey(Optional.of(DATA_API_KEY))
                            .withProperties(DATA_PROPERTIES)
                            .build()
            ))
            .build();

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestUser.class.getSimpleName());
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
    private static ResourceMetadata generateMetadata(
            final String namespace,
            final String name,
            final UUID uid,
            final OffsetDateTime created
    ) {
        HashMap<String, String> annotations = new HashMap<>(1);
        annotations.put("valid", "test");

        HashMap<String, String> labels = new HashMap<>();
        labels.put("test", "valid");

        return ResourceMetadata.builder()
                .withKind(User.KIND)
                .withApiVersion(User.API_VERSION)

                .withNamespace(namespace)
                .withName(name)
                .withUid(uid)

                .withCreated(created)

                .withAnnotations(annotations)
                .withLabels(labels)

                .build();
    }

    @Test
    void shouldReturnDiscordIdWhenUserHasAnDiscordIdSet() {
        MDC.put("test", "read-discord-id");

        assertEquals(DISCORD_ID, DATA.getSpec().orElseThrow().getProperty("discord-id").orElseThrow());
    }

    @Test
    void shouldFindAnnotationWhenItIsSet() {
        MDC.put("test", "read-valid-annotation");

        assertTrue(DATA.getMetadata().isAnnotated("valid"));
        assertEquals("test", DATA.getMetadata().getAnnotations().get("valid"));
    }

    @Test
    void shouldNotFindAnnotationWhenItIsAbsent() {
        MDC.put("test", "read-invalid-annotation");

        assertFalse(DATA.getMetadata().isAnnotated("invalid"));
    }

    @Test
    void shouldFindLabelWhenItIsSet() {
        MDC.put("test", "read-valid-label");

        assertTrue(DATA.getMetadata().isLabeled("test"));
        assertEquals("valid", DATA.getMetadata().getLabels().get("test"));
    }

    @Test
    void shouldNotFindLabelWhenItIsAbsent() {
        MDC.put("test", "read-invalid-label");

        assertFalse(DATA.getMetadata().isAnnotated("not-there"));
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }
}
