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
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoDriveThruRPGAPIKeyDefinedException;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoValidTokenException;
import io.quarkus.arc.AlternativePriority;
import io.quarkus.cache.CacheResult;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
@AlternativePriority(100)
@Slf4j
public class DriveThruRPGServiceRestImpl implements DriveThruRPGService {
    /**
     * Date/time formatter for parsing the json results.
     */
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * The DriveThruRPG client.
     */
    private final DriveThruRPGClient client;

    @Inject
    public DriveThruRPGServiceRestImpl(@RestClient final DriveThruRPGClient client) {
        this.client = client;

        log.debug("Created {}. client={}", getClass().getSimpleName(), client);
    }

    @Override
    @CacheResult(cacheName = "drivethrurpg-token")
    public Token getToken(final User user) throws InvalidUserException, NoDriveThruRPGAPIKeyDefinedException, NoValidTokenException {
        if (user.getSpec().isEmpty()) {
            throw new InvalidUserException(user);
        }
        UserData u = user.getSpec().get();

        if (u.getDriveThruRPGApiKey().isEmpty()) {
            throw new NoDriveThruRPGAPIKeyDefinedException(user);
        }

        log.trace("Loading access token for user='{}/{}', apiKey='{}'", user.getNameSpace(), user.getName(), u.getDriveThruRPGApiKey().get());

        LinkedHashMap<String, String> response = client.getToken(u.getDriveThruRPGApiKey().get()).getMessage();
        log.trace("Token loaded. token={}", response);

        LocalDateTime serverTime = parse(response.get("server_time"));
        LocalDateTime expireTime = parse(response.get("expires"));
        LocalDateTime localTime = LocalDateTime.now();

        Duration duration = Duration.between(serverTime, expireTime);

        Token result = Token.builder()
                .withAccessToken(response.get("access_token"))
                .withCustomerId(response.get("customers_id"))
                .withExpireTime(expireTime)
                .withServerTime(serverTime)
                .withLocalTime(localTime)
                .withExpires(duration.getSeconds())
                .build();

        log.debug("DriveThru: token={}", result);
        return result;
    }

    private LocalDateTime parse(final String input) {
        return LocalDateTime.parse(input, dateFormatter);
    }

    @Override
    @CacheResult(cacheName = "drivethrurpg-products")
    public Optional<Product> getProduct(final String productId) {
        log.trace("retrieving products for: product={}", productId);

        ProductMessage result = client.getProduct(productId);

        log.debug("DriveThru: product={}", result.getData());
        return result.getData();
    }


    @Override
    @CacheResult(cacheName = "drivethrurpg-publishers")
    public Optional<Publisher> getPublisher(final String publisherId) {
        log.trace("retrieving publisher for: publisher={}", publisherId);

        PublisherMessage result = client.getPublisher(publisherId);
        log.debug("DriveThru: publisher={}", result);

        return result.getData();
    }

    @Override
    @CacheResult(cacheName = "drivethrurpg-ownedProducts")
    public List<OwnedProduct> getOwnedProducts(final User user) throws NoValidTokenException {
        Token token;
        try {
            token = getToken(user);
        } catch (NoValidTokenException | InvalidUserException | NoDriveThruRPGAPIKeyDefinedException e) {
            throw new NoValidTokenException(user);
        }

        log.trace("retrieving owned products for: user={}, token={}", user, token);

        OwnedProductMessage result = client.getOwnedProducts(token.getBearerToken(), token.getCustomerId(), 1L, 1000, 0);

        log.debug("DriveThru: ownedProducts.count={}", result.getData().size());
        return result.getData() != null ? result.getData() : new ArrayList<>();
    }
}
