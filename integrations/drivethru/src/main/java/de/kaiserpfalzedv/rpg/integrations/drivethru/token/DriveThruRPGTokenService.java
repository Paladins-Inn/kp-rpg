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

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

/**
 * The internal token service.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-29
 */
@ApplicationScoped
public class DriveThruRPGTokenService {
    private static final Logger LOG = LoggerFactory.getLogger(DriveThruRPGTokenService.class);

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Inject
    @RestClient
    DriveThruRPGTokenClient client;

    public DriveThruRPGToken getDriveThruRPGToken(final String apiKey) {
        LOG.trace("retrieving oauth2 token for: {}", apiKey);

        try {
            LinkedHashMap<String, String> response = client
                    .getToken("Bearer " + apiKey)
                    .getMessage();

            LocalDateTime serverTime = parse(response.get("server_time"));
            LocalDateTime expireTime = parse(response.get("expires"));
            LocalDateTime localTime = LocalDateTime.now();

            Duration duration = Duration.between(serverTime, expireTime);

            return ImmutableDriveThruRPGToken.builder()
                    .accessToken(response.get("access_token"))
                    .customerId(response.get("customers_id"))
                    .expireTime(expireTime)
                    .serverTime(serverTime)
                    .localTime(localTime)
                    .expires(duration.getSeconds())
                    .build();
        } catch (WebApplicationException e) {
            LOG.error("Could not retrieve the token: {}", e.getResponse().getHeaders());

            throw e;
        }
    }

    private LocalDateTime parse(final String input) {
        return LocalDateTime.parse(input, dateFormatter);
    }
}
