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

package de.kaiserpfalzedv.rpg.integrations.datastore.cards;

import de.kaiserpfalzedv.rpg.core.cards.Card;
import de.kaiserpfalzedv.rpg.core.cards.ImmutableCard;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceHistory;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceStatus;
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

/**
 * TestCardRepository -- tests the basic methods for storing, manipulating and deleting cards.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-10
 */
@QuarkusTest
@QuarkusTestResource(MongoDBResource.class)
public class TestCardRepository {
    private static final Logger LOG = LoggerFactory.getLogger(TestCardRepository.class);

    private static final String NAMESPACE = "torg";
    private static final String NAME = "dramadeck-default";
    private static final UUID CARD_UID = UUID.randomUUID();
    private static final OffsetDateTime CREATED = OffsetDateTime.now(ZoneOffset.UTC);

    /**
     * Default data created during setup of tests.
     */
    private static final MongoCard data = new MongoCard(ImmutableCard.builder()
            .metadata(
                    ImmutableResourceMetadata.builder()
                            .kind(MongoCard.KIND)
                            .apiVersion(MongoCard.API_VERSION)

                            .namespace(NAMESPACE)
                            .name(NAME)
                            .uid(CARD_UID)
                            .selfLink("/apis/" + MongoCard.KIND + "/" + MongoCard.API_VERSION + "/" + CARD_UID)

                            .generation(1L)
                            .created(CREATED)

                            .putAnnotations("test", "true")

                            .build()

            )
            .status(
                    ImmutableResourceStatus.<String>builder()
                            .observedGeneration(1L)
                            .addHistory(
                                    ImmutableResourceHistory.<String>builder()
                                            .status("Created")
                                            .timeStamp(CREATED)
                                            .message("Card created.")
                                            .build()
                            )
                            .build()
            )
            .build());

    @Inject
    CardRepository sut;

    @Test
    public void shouldReturnEmptyOptionalWhenThereIsNoCard() {
        MDC.put("test", "not-present-without-card");

        Optional<Card> result = sut.findByNameSpaceAndName("no-namespace", "no-card");
        LOG.trace("result=", result);

        assertFalse(result.isPresent(), "There should be no card no-namespace/no-card");
    }

    /* FIXME 2021-01-09 rlichti Implement the MongoDB saving of data.
    @Test
    public void shouldReturnCardWhenThereIsACard() {
        MDC.put("test", "load-card");

        Optional<Card> result = sut.findByNameSpaceAndName(NAMESPACE, NAME);
        LOG.trace("result=", result);

        assertTrue(result.isPresent(), "There should be a card " + NAMESPACE + "/" + NAME);
    }
    */

    @BeforeEach
    void setUpEach() {
        sut.persistOrUpdate(data);
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestCardRepository.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }
}
