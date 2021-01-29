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

package de.kaiserpfalzedv.rpg.integrations.drivethru.token;

import de.kaiserpfalzedv.rpg.core.rest.LoggingRestHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.ws.rs.*;
import java.util.LinkedHashMap;


/**
 * The tokenservice of DriveThruRPG to get the oauth2 token via the API key.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-29
 */
@Path("/api/v1")
@RegisterRestClient(configKey = "tomb.drivethrurpg.api")
@RegisterClientHeaders(LoggingRestHeaders.class)
public interface DriveThruRPGTokenClient {
    @POST
    @Path("/token")
    DriveThruRPGWrapper<LinkedHashMap> getToken(
            @HeaderParam("Authorization") String authorization
    );
}
