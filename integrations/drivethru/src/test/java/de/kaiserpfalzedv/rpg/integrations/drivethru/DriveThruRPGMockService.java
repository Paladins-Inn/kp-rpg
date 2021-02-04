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

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * A mocked version of the DriveThruRPG api.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-29
 */
public class DriveThruRPGMockService implements QuarkusTestResourceLifecycleManager {
    private WireMockServer mockServer;

    @Override
    public Map<String, String> start() {
        mockServer = new WireMockServer();
        mockServer.start();

        stubFor(post(urlEqualTo("/api/v1/token"))
                .willReturn(
                        aResponse()
                        .withHeader("Content-Type", "application/json")
                                .withBodyFile("token.json")
                )
        );


        stubFor(get(urlEqualTo("/api/v1/customers/CUST/products?page=1&page_size=1000&include_archived=0"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("projects-ids.json")
                )
        );

        stubFor(get(urlEqualTo("/api/v1/publishers/PUBLISHER"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("publisher-2.json")
                )
        );

        stubFor(get(urlEqualTo("/api/v1/products/PRODUCT"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("product-1.json")
                )
        );

        return Collections.singletonMap("tomb.drivethrurpg.api/mp-rest/url", mockServer.baseUrl());
    }

    @Override
    public void stop() {
        if (null != mockServer) {
            mockServer.start();
            mockServer = null;
        }
    }
}
