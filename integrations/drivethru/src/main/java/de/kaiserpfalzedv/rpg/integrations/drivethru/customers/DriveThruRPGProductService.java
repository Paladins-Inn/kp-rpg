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

package de.kaiserpfalzedv.rpg.integrations.drivethru.customers;

import de.kaiserpfalzedv.rpg.integrations.drivethru.token.DriveThruRPGToken;
import de.kaiserpfalzedv.rpg.integrations.drivethru.token.DriveThruRPGTokenService;
import io.quarkus.cache.CacheResult;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;


/**
 * The service for accessing the owned products of a person at DriveThruRPG.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-29
 */
@ApplicationScoped
public class DriveThruRPGProductService {
    private static final Logger LOG = LoggerFactory.getLogger(DriveThruRPGProductService.class);

    @Inject
    @RestClient
    DriveThruRPGProductClient client;

    @Inject
    DriveThruRPGTokenService tokenService;

    @CacheResult(cacheName = "drivethrurpg-products")
    public DriveThruRPGOwnedProduct[] getOwnedProducts(final String apiKey) {
        DriveThruRPGToken token = tokenService.getDriveThruRPGToken(apiKey);
        LOG.trace("retrieving products for: apiKey={}, token={}", apiKey, token);

        try {
            DriveThruRPGProductMessage result = client.getProducts(
                    "Bearer " + token.getAccessToken(),
                    token.getCustomerId(),
                    1,
                    1000,
                    0,
                    "products_id"
            );

            LOG.debug("Loaded {} products for {}", result.getProducts().length, apiKey);
            return result.getProducts();

        } catch (WebApplicationException e) {
            LOG.error("Could not retrieve the token: {}", e.getResponse().getHeaders());

            throw e;
        }
    }
}
