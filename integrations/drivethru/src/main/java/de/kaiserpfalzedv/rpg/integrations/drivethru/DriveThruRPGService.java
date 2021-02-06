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

import de.kaiserpfalzedv.rpg.core.user.InvalidUserException;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.core.user.UserData;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.*;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.DriveThruMessage;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.DriveThruMultiMessage;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoDriveThruRPGAPIKeyDefinedException;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoValidTokenException;
import io.quarkus.cache.CacheResult;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * The service to access publisher data.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-03
 */
@ApplicationScoped
public class DriveThruRPGService {
    private static final Logger LOG = LoggerFactory.getLogger(DriveThruRPGService.class);

    /**
     * Date/time formatter for parsing the json results.
     */
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * The DriveThruRPG client.
     */
    private final DriveThruRPGClient client;

    @Inject
    public DriveThruRPGService(@RestClient final DriveThruRPGClient client) {
        this.client = client;
    }

    /**
     * Retrieves an access token from the given API Key from the user definition.
     *
     * @param user The user to authorize to DriveThruRPG.
     * @return An access token with customer id.
     * @throws InvalidUserException                 The user has no user spec.
     * @throws NoDriveThruRPGAPIKeyDefinedException The user spac has no driveThruRPG API Key defined.
     * @throws NoValidTokenException                The retrieved token is not valid.
     */
    @CacheResult(cacheName = "drivethrurpg-token")
    public Token getToken(final User user) throws InvalidUserException, NoDriveThruRPGAPIKeyDefinedException, NoValidTokenException {
        if (user.getSpec().isEmpty()) {
            throw new InvalidUserException(user);
        }
        UserData u = user.getSpec().get();

        if (u.getDriveThruRPGApiKey().isEmpty()) {
            throw new NoDriveThruRPGAPIKeyDefinedException(user);
        }

        LOG.trace("Loading access token for user='{}/{}', apiKey='{}'", user.getNameSpace(), user.getName(), u.getDriveThruRPGApiKey().get());

        LinkedHashMap<String, String> response = client.getToken(u.getDriveThruRPGApiKey().get()).getMessage();

        LocalDateTime serverTime = parse(response.get("server_time"));
        LocalDateTime expireTime = parse(response.get("expires"));
        LocalDateTime localTime = LocalDateTime.now();

        Duration duration = Duration.between(serverTime, expireTime);

        Token result = ImmutableToken.builder()
                .accessToken(response.get("access_token"))
                .customerId(response.get("customers_id"))
                .expireTime(expireTime)
                .serverTime(serverTime)
                .localTime(localTime)
                .expires(duration.getSeconds())
                .build();

        LOG.debug("DriveThru: token={}", result);
        return result;
    }

    private LocalDateTime parse(final String input) {
        return LocalDateTime.parse(input, dateFormatter);
    }

    /**
     * Loads the product data for a single product.
     *
     * @param productId The product id to load the data for.
     * @return The product.
     */
    @CacheResult(cacheName = "drivethrurpg-products")
    public Optional<Product> getProduct(final String productId) {
        LOG.trace("retrieving products for: product={}", productId);

        DriveThruMessage<Product> result = client.getProduct(productId);

        LOG.debug("DriveThru: product={}", result.getData());
        return result.getData();
    }


    /**
     * Retrieves the publisher data for the given id.
     *
     * @param publisherId The pubisher id to load the data for.
     * @return The data of the publisher.
     */
    @CacheResult(cacheName = "drivethrurpg-publishers")
    public Optional<Publisher> getPublisher(final String publisherId) {
        LOG.trace("retrieving publisher for: publisher={}", publisherId);

        DriveThruMessage<Publisher> result = client.getPublisher(publisherId);

        LOG.debug("DriveThru: publisher={}", result.getData());
        return result.getData();
    }

    /**
     * Loads all products owned at DriveThruRPG for the given user.
     *
     * @param user The user to load the data as.
     * @return The products owned by the user.
     * @throws NoValidTokenException The access_token could not be retrieved.
     */
    @CacheResult(cacheName = "drivethrurpg-ownedproducts")
    public List<OwnedProduct> getOwnedProducts(final User user) throws NoValidTokenException {
        Token token;
        try {
            token = getToken(user);
        } catch (NoValidTokenException | InvalidUserException | NoDriveThruRPGAPIKeyDefinedException e) {
            throw new NoValidTokenException(user);
        }

        LOG.trace("retrieving owned products for: user={}, token={}", user, token);

        DriveThruMultiMessage<OwnedProduct> result = client.getOwnedProducts(token.getBearerToken(), token.getCustomerId(), 1L, 1000, 0);

        LOG.debug("DriveThru: ownedProducts.count={}", result.getData().size());
        return result.getData();
    }
}
