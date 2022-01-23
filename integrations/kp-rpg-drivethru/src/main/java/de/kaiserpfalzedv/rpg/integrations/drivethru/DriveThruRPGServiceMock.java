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

package de.kaiserpfalzedv.rpg.integrations.drivethru;

import de.kaiserpfalzedv.commons.core.user.InvalidUserException;
import de.kaiserpfalzedv.commons.core.user.User;
import de.kaiserpfalzedv.rpg.core.dice.bag.D100;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.OwnedProduct;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Product;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.ProductFiles;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Publisher;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Token;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoDriveThruRPGAPIKeyDefinedException;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoValidTokenException;
import io.quarkus.arc.Priority;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.inject.Alternative;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;


/**
 * DriveThruRPGServiceMock -- a mocked service for using in unit tests.
 * <p>
 * The following cases are defined for tests. They depend on the user name of the specified user:
 *
 * <dl>
 *     <dt>invalid#0001</dt><dd>Throws an {@link InvalidUserException}</dd>
 *     <dt>invalid#0002</dt><dd>Throws an {@link NoDriveThruRPGAPIKeyDefinedException}</dd>
 *     <dt>invalid#0003</dt><dd>Returns an expired token</dd>
 *     <dt>invalid#0004</dt><dd>Throws an {@link NoValidTokenException}</dd>
 *     <dt><em>any other name</em></dt><dd>Return a token valid for the next hour</dd>
 * </dl>
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-06
 */
@Alternative
@Priority(50)
@Slf4j
public class DriveThruRPGServiceMock implements DriveThruRPGService {
    /**
     * Mocks the token service.
     *
     * @param user The user to authorize to DriveThruRPG.
     * @return a token.
     * @throws InvalidUserException                 for username {@literal invalid#0001}
     * @throws NoDriveThruRPGAPIKeyDefinedException for username {@literal invalid#0002}
     * @throws NoValidTokenException                for username {@literal invalid#0004}
     */
    @Override
    public Token getToken(final User user) throws InvalidUserException, NoDriveThruRPGAPIKeyDefinedException, NoValidTokenException {
        switch (user.getName().toLowerCase(Locale.ROOT)) {
            case "invalid#0001":
                log.debug("Throwing InvalidUserException. user='{}'", user.getSelfLink());
                throw new InvalidUserException(user);

            case "invalid#0002":
                log.debug("Throwing NoDriveThruRPGAPIKeyDefinedException. user='{}'", user.getSelfLink());
                throw new NoDriveThruRPGAPIKeyDefinedException(user);

            case "invalid#0004":
                log.debug("Throwing NoValidTokenException. user='{}'", user.getSelfLink());
                throw new NoValidTokenException(user);
        }

        LocalDateTime now = LocalDateTime.now(UTC);

        Token token = Token.builder()
                .accessToken(UUID.randomUUID().toString())
                .customerId(user.getName().split("#")[1])

                .expireTime(LocalDateTime.MAX)
                .serverTime(now)
                .localTime(now)
                .expires(LocalDateTime.MAX.toEpochSecond(UTC) - now.toEpochSecond(UTC))

                .build();

        if ("invalid#0003".equalsIgnoreCase(user.getName())) {
            token = Token.builder()
                    .accessToken(UUID.randomUUID().toString())
                    .customerId(user.getName().split("#")[1])

                    .expireTime(now)
                    .serverTime(now.minusHours(1))
                    .localTime(now.minusHours(1))
                    .expires(3600L)

                    .build();
        }

        log.trace("Token created. user='{}', token={}", user.getSelfLink(), token);
        return token;
    }

    /**
     * getProduct -- mocks the product.
     *
     * @param productId The product id to load the data for. If the product id is a negative integer, an empty optional
     *                  will be returned.
     * @return The product dataset.
     */
    @Override
    public Optional<Product> getProduct(final String productId) {
        if (Integer.parseInt(productId, 10) < 0) {
            log.trace("Product not found. productId={}", productId);
            return Optional.empty();
        }

        log.trace("Product created. productId={}", productId);
        String productUrl = "https://nowhere/product/" + productId;

        Set<ProductFiles> files = Set.of(
                ProductFiles.builder()
                        .bundleId("Bundle 0")
                        .filename("filename.pdf")
                        .filesizeBytes(1024L^3)
                        .fileSizeMegaBytes(1.0d)
                        .build()
        );

        return Optional.of(
                Product.builder()
                        .productsId(productId).productsName("Product Nr. 1")
                        .publisherId("1").publisherName("Publisher Nr. 1")
                        .coverURL(productUrl + "/cover")
                        .thumbnail(productUrl + "/thumb")
                        .thumbnail100(productUrl + "/thumb100")
                        .thumbnail80(productUrl + "/thumb80")
                        .thumbnail40(productUrl + "/thumb40")
                        .files(files)
                        .build()
        );
    }

    /**
     * getPublisher -- mocks the publisher service.
     *
     * @param publisherId The pubisher id to load the data for. If the publisherId is a negative integer, an empty
     *                    optional will be returned.
     * @return the publisher dataset.
     */
    @Override
    public Optional<Publisher> getPublisher(final String publisherId) {
        if (Integer.parseInt(publisherId, 10) < 0) {
            log.trace("No publisher found. publisherId={}", publisherId);
            return Optional.empty();
        }

        log.trace("Publisher created. publisherId={}", publisherId);
        return Optional.of(
                Publisher.builder()
                        .publisherId(publisherId)
                        .publisherName("Publisher Nr. " + publisherId)
                        .build()
        );
    }

    /**
     * getOwnedProducts -- mocks the owned product.
     * <p>
     * A token will be requested, so the behavior described in {@link #getToken(User)} will also be mimicked by this
     * method.
     * <p>
     * In Addition, the name {@literal valid#0001} will return an empty list. Every other name will return a D100
     * entries.
     *
     * @param user The user to load the data as.
     * @return The owned products.
     * @throws InvalidUserException                 for username {@literal invalid#0001}
     * @throws NoDriveThruRPGAPIKeyDefinedException for username {@literal invalid#0002}
     * @throws NoValidTokenException                for username {@literal invalid#0004}
     */
    @Override
    public List<OwnedProduct> getOwnedProducts(final User user) throws NoValidTokenException, NoDriveThruRPGAPIKeyDefinedException, InvalidUserException {
        getToken(user);

        int count = Integer.parseInt(new D100().roll().getRolls()[0], 10);
        if ("valid#0001".equalsIgnoreCase(user.getName())) {
            count = 0;
        }

        List<OwnedProduct> result = new ArrayList<>(count);

        for (int i = 1; i < count + 1; i++) {
            result.add(
                    OwnedProduct.builder()
                            .id(Integer.toString(i, 10))
                            .name("Product Nr. " + i)
                            .coverURL(Optional.of("https://nowhere/product/" + i + "/cover"))

                            .datePurchased(Optional.of(OffsetDateTime.now(UTC).minusMonths(3L)))
                            .archived(Optional.of("0"))
                            .build()
            );
        }

        log.trace("Created owned products: user='{}', count={}", user.getSelfLink(), result.size());
        return result;
    }
}
