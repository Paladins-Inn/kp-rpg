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

import de.kaiserpfalzedv.rpg.integrations.drivethru.token.DriveThruRPGToken;
import de.kaiserpfalzedv.rpg.integrations.drivethru.token.DriveThruRPGTokenService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class TestDriveThruRPGTokenService {
    static private final Logger LOG = LoggerFactory.getLogger(TestDriveThruRPGTokenService.class);

    @Inject
    DriveThruRPGTokenService sut;

    @Test
    public void shouldRetrieveATokenWhenCorrectAPIKeyIsGiven() {
        MDC.put("test", "retrive-by-apiKey");

        // Authorization: Bearer 910ac8e5c953f774bb29c09a593c2e875be82704
        //

        DriveThruRPGToken result = sut.getDriveThruRPGToken("910ac8e5c953f774bb29c09a593c2e875be82704");

        LOG.trace("result={}", result);
        assertNotNull(result);
    }


    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", LOG.getName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }
}
