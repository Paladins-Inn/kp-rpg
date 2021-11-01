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

package de.kaiserpfalzedv.rpg.test.oauth2;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * This is the wiremock for oauth2 services for test and dev.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.1.0 2021-01-22
 */
public class Oauth2WireMock implements QuarkusTestResourceLifecycleManager {
    private static final Logger LOG = LoggerFactory.getLogger(Oauth2WireMock.class);

    private WireMockServer wireMockServer;

    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        WireMock.stubFor(
                WireMock.post("/introspect")
                        .willReturn(
                                WireMock.aResponse()
                                .withBody(
                                        "{\"active\":true,\"scope\":\"openid,guilds\",\"username\":\"klenkes74\",\"iat\":1562315654,\"exp\":1562317454,\"expires_in\":1458,\"client_id\":\"my_client_id\"}"
                                )
                        )
        );

        LOG.info("Oauth2 server wiremock started on '{}/introspect'", wireMockServer.baseUrl());
        return Collections.singletonMap("quarkus.oauth2.introspection-url", wireMockServer.baseUrl() + "/introspect");
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();

            LOG.info("Oauth2 server wiremock '{}/introspect' stopped.", wireMockServer.baseUrl());
        }
    }
}
