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

package de.kaiserpfalzedv.rpg.integrations.drivethru;

import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceMetadata;
import de.kaiserpfalzedv.rpg.core.user.ImmutableUser;
import de.kaiserpfalzedv.rpg.core.user.ImmutableUserData;
import de.kaiserpfalzedv.rpg.core.user.InvalidUserException;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.integrations.drivethru.publishers.OwnedProduct;
import de.kaiserpfalzedv.rpg.integrations.drivethru.publishers.Product;
import de.kaiserpfalzedv.rpg.integrations.drivethru.publishers.Publisher;
import de.kaiserpfalzedv.rpg.integrations.drivethru.publishers.Token;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoDriveThruRPGAPIKeyDefinedException;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoValidTokenException;
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
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(DriveThruRPGMockService.class)
public class TestDriveThruRPGService {
    static private final Logger LOG = LoggerFactory.getLogger(TestDriveThruRPGService.class);
    private static final User DEFAULT_USER = ImmutableUser.builder()
            .metadata(
                    ImmutableResourceMetadata.builder()
                            .kind(User.KIND)
                            .apiVersion(User.API_VERSION)

                            .namespace("discord")
                            .name("test#1234")
                            .uid(UUID.randomUUID())

                            .generation(0L)
                            .created(OffsetDateTime.now(Clock.systemUTC()))

                            .build()
            )
            .spec(
                    ImmutableUserData.builder()
                            .description("Test-user for API calls")
                            .driveThruRPGApiKey("API-KEY")
                            .build()
            )
            .build();
    @Inject
    DriveThruRPGService sut;

    @Test
    public void shouldRetrieveOwnedProductsWhenCorrectAPIKeyIsGiven() throws NoValidTokenException {
        MDC.put("test", "retrieve-owned-products");

        List<OwnedProduct> result = sut.getOwnedProducts(DEFAULT_USER);

        LOG.trace("results={}", result);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void shouldRetrieveProduct() {
        MDC.put("test", "retrieve-single-product");

        Optional<Product> result = sut.getProduct("PRODUCT");

        assertTrue(result.isPresent());
    }

    @Test
    public void shouldRetrievePublisher() {
        MDC.put("test", "retrieve-single-publisher");

        Optional<Publisher> result = sut.getPublisher("PUBLISHER");

        assertTrue(result.isPresent());
    }


    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", LOG.getName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @Test
    public void shouldRetrieveATokenWhenCorrectAPIKeyIsGiven() throws NoValidTokenException, InvalidUserException, NoDriveThruRPGAPIKeyDefinedException {
        MDC.put("test", "retrieve-token");

        Token result = sut.getToken(DEFAULT_USER);

        LOG.trace("result={}", result);
        assertNotNull(result);
    }
}
