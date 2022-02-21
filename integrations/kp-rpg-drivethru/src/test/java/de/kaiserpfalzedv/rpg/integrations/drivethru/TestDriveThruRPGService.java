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

package de.kaiserpfalzedv.rpg.integrations.drivethru;

import de.kaiserpfalzedv.commons.core.resources.Metadata;
import de.kaiserpfalzedv.commons.core.resources.Pointer;
import de.kaiserpfalzedv.commons.core.user.InvalidUserException;
import de.kaiserpfalzedv.commons.core.user.User;
import de.kaiserpfalzedv.commons.core.user.UserData;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.OwnedProduct;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Product;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Publisher;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Token;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoDriveThruRPGAPIKeyDefinedException;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoValidTokenException;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.fail;

@QuarkusTest
@QuarkusTestResource(DriveThruRPGServiceResource.class)
@Slf4j
public class TestDriveThruRPGService {
    private static final User DEFAULT_USER = User.builder()
            .metadata(Metadata.builder()
                    .identity(Pointer.builder()
                            .kind(User.KIND)
                            .apiVersion(User.API_VERSION)
                            .nameSpace("discord")
                            .name("test#1234")
                            .build()
                    )
                    .created(OffsetDateTime.now(Clock.systemUTC()))
                    .build()
            )
            .spec(
                    UserData.builder()
                            .properties(new HashMap<>())
                            .description("Test-user for API calls")
                            .driveThruRPGKey("API-KEY")
                            .build()
            )
            .build();

    private final DriveThruRPGService sut;

    @Inject
    public TestDriveThruRPGService(final DriveThruRPGService sut) {
        this.sut = sut;
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", log.getName());
    }

    @Test
    public void shouldRetrieveOwnedProductsWhenCorrectAPIKeyIsGiven() throws NoValidTokenException, InvalidUserException, NoDriveThruRPGAPIKeyDefinedException {
        MDC.put("test", "retrieve-owned-products");

        List<OwnedProduct> result = sut.getOwnedProducts(DEFAULT_USER);
        log.trace("results={}", result);

        assertThat("No result found.", result, notNullValue());
        assertThat("Empty result set.", result.size(), greaterThan(0));

        assertThat("No ID for the first result element.", result.get(0).getId(), notNullValue());
    }

    @Test
    public void shouldRetrieveProduct() {
        MDC.put("test", "retrieve-single-product");

        Optional<Product> result = sut.getProduct("PRODUCT");

        result.ifPresentOrElse(
                p -> {
                    assertThat(p.getProductsId(), is("1"));
                    assertThat(p.getProductsName(), is("Dept. 7 Adv. Class Update: Scion of Masada"));
                },
                () -> fail("There is no product.")
        );
    }


    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @Test
    public void shouldRetrievePublisher() {
        MDC.put("test", "retrieve-single-publisher");

        Optional<Publisher> result = sut.getPublisher("PUBLISHER");

        result.ifPresentOrElse(
                t -> {
                    assertThat(t.getPublisherId(), is("2"));
                    assertThat(t.getPublisherName(), is("Chaosium"));
                },
                () -> fail("There was no publisher received!")
        );
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @Test
    public void shouldRetrieveATokenWhenCorrectAPIKeyIsGiven() throws NoValidTokenException, InvalidUserException, NoDriveThruRPGAPIKeyDefinedException {
        MDC.put("test", "retrieve-token");

        Token result = sut.getToken(DEFAULT_USER);
        log.trace("result={}", result);

        assertThat(result.getAccessToken(), is("TOKEN"));
        assertThat(result.getCustomerId(), is("CUST"));
        assertThat(result.getExpires(), is(3600L));
        assertThat(result.getServerTime(), is(LocalDateTime.parse("2021-01-29T16:58:08")));
    }
}
