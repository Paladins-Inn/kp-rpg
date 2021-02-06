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
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.OwnedProduct;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Product;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Publisher;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Token;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoDriveThruRPGAPIKeyDefinedException;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.NoValidTokenException;
import io.quarkus.cache.CacheResult;

import java.util.List;
import java.util.Optional;

public interface DriveThruRPGService {
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
    Token getToken(User user) throws InvalidUserException, NoDriveThruRPGAPIKeyDefinedException, NoValidTokenException;

    /**
     * Loads the product data for a single product.
     *
     * @param productId The product id to load the data for.
     * @return The product.
     */
    @CacheResult(cacheName = "drivethrurpg-products")
    Optional<Product> getProduct(String productId);

    /**
     * Retrieves the publisher data for the given id.
     *
     * @param publisherId The pubisher id to load the data for.
     * @return The data of the publisher.
     */
    @CacheResult(cacheName = "drivethrurpg-publishers")
    Optional<Publisher> getPublisher(String publisherId);

    /**
     * Loads all products owned at DriveThruRPG for the given user.
     *
     * @param user The user to load the data as.
     * @return The products owned by the user.
     * @throws NoValidTokenException The access_token could not be retrieved.
     */
    @CacheResult(cacheName = "drivethrurpg-ownedproducts")
    List<OwnedProduct> getOwnedProducts(User user) throws NoValidTokenException, NoDriveThruRPGAPIKeyDefinedException, InvalidUserException;
}
