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

import de.kaiserpfalzedv.rpg.integrations.drivethru.model.OwnedProduct;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Product;
import de.kaiserpfalzedv.rpg.integrations.drivethru.model.Publisher;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.DriveThruMessage;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.DriveThruMultiMessage;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;

/**
 * The client to retrieve publisher data.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-03
 */
@Path("/api/v1")
@RegisterRestClient(configKey = "tomb.drivethrurpg.api")
public interface DriveThruRPGClient {
    @POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    DriveThruMessage<LinkedHashMap<String, String>> getToken(
            @HeaderParam("Authorization") String authorization
    );

    @GET
    @Path("/customers/{customerId}/products")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    DriveThruMultiMessage<OwnedProduct> getOwnedProducts(
            @HeaderParam("Authorization") String bearerToken,
            @PathParam("customerId") String customerId,
            @QueryParam("page") long page,
            @QueryParam("page_size") int size,
            @QueryParam("include_archived") int archived
    );


    @GET
    @Path("/publishers/{publisherId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    DriveThruMessage<Publisher> getPublisher(
            @PathParam("publisherId") String publisherId
    );

    @GET
    @Path("/products/{productId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    DriveThruMessage<Product> getProduct(
            @PathParam("productId") String productId
    );
}
