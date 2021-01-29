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

/**
 * The tokenservice of DriveThruRPG to get the oauth2 token via the API key.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-29
 */

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api/v1/customers")
@RegisterRestClient(configKey = "tomb.drivethrurpg.api")
public interface DriveThruRPGCustomerClient {
    @GET
    @Path("/{customerId}/products")
    DriveThruRPGOwnedProducts getProducts(
            @PathParam("customerId") String customerId,
            @QueryParam("page") long page,
            @QueryParam("page_size") int size,
            @QueryParam("include_archived") int archived,
            @QueryParam("fields") String[] fields
    );
}
