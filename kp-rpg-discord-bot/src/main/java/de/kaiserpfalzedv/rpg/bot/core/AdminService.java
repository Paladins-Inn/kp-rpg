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

package de.kaiserpfalzedv.rpg.bot.core;

import io.quarkus.oidc.OidcConfigurationMetadata;
import io.quarkus.security.identity.SecurityIdentity;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.representations.idm.authorization.Permission;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Admin-Service of the KP RPG service.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2020-01-02
 */
@Path("/apis/v1/admin")
@Produces(MediaType.TEXT_PLAIN)
@ApplicationScoped
@Slf4j
public class AdminService {
    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/")
    public Response ping() {
        log.info("ping-pong.");

        return Response.ok("pong").build();
    }

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public List<Permission> me(@QueryParam("res") String resource) {
        log.info("who is me: {}", jwt);

        log.info(
                "JWT: tokenId={}, name={}, subject={}, audience={}, groups={}, claimNames={}, issuer={}, issued={}, expires={}",
                jwt.getTokenID(),
                jwt.getName(),
                jwt.getSubject(),
                jwt.getAudience(),
                jwt.getGroups(),
                jwt.getClaimNames(),
                jwt.getIssuer(),
                jwt.getIssuedAtTime(),
                jwt.getExpirationTime()
        );

        log.info(
                "Claim information: sub={}, resource_access={}, email_verified={}, iss={}, typ={}, preferred_username={}, given_name={}, "
                        + "aud={}, acr={}, nbf={}, realm_access={}, azp={}, auth_time={}, scope={}, name={}, exp={}, session_state={}, iat={}, " +
                        "family_name={}, jti={}, email={}",
                jwt.getClaim("sub"),
                jwt.getClaim("resource_access"),
                jwt.getClaim("email_verified"),
                jwt.getClaim("iss"),
                jwt.getClaim("typ"),
                jwt.getClaim("preferred_username"),
                jwt.getClaim("given_name"),
                jwt.getClaim("aud"),
                jwt.getClaim("acr"),
                jwt.getClaim("nbf"),
                jwt.getClaim("realm_access"),
                jwt.getClaim("azp"),
                jwt.getClaim("auth_time"),
                jwt.getClaim("scope"),
                jwt.getClaim("name"),
                jwt.getClaim("exp"),
                jwt.getClaim("session_state"),
                jwt.getClaim("iat"),
                jwt.getClaim("family_name"),
                jwt.getClaim("jti"),
                jwt.getClaim("email")
        );

        OidcConfigurationMetadata oidcMetadata = identity.getAttribute("configuration-metadata");
        log.info("propertyNames={}", oidcMetadata.getPropertyNames());
        return identity.getAttribute(resource);
    }
}
