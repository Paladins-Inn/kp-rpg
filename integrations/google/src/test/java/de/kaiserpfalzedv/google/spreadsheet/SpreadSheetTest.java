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

package de.kaiserpfalzedv.google.spreadsheet;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.slf4j.MDC;

import java.io.IOException;
import java.security.GeneralSecurityException;


/**
 * SpredSheetTest --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 0.3.0  2021-05-23
 */
@Slf4j
public class SpreadSheetTest {
    private static final String APPLICATION_NAME = "test application";

    private SpreadSheet sut;

    @Test
    public void shouldOpenAnExistingSpreadSheetWhenValidIdAndAuthorizationIsGiven() {
        setTestName("open-existing-spreadsheet");

        Sheets service = sut.open();
    }


    @BeforeAll
    public static void setUpLogging() {
        MDC.put("test-class", SpreadSheetTest.class.getSimpleName());
    }

    @AfterAll
    public static void tearDownLogging() {
        log.info("Test '{}' finished.", MDC.get("test-class"));
        MDC.remove("test");
        MDC.remove("test-class");
    }

    @BeforeEach
    public void setUpService() throws GeneralSecurityException, IOException {
        sut = SpreadSheet.builder()
                .withTransport(GoogleNetHttpTransport.newTrustedTransport())
                .build();
    }

    @AfterEach
    public void removeTestName() {
        MDC.remove("test");
    }


    private void setTestName(final String name, final String... parameters) {
        MDC.put("test", name);
        log.debug("Testing '{}': {}", name, parameters);
    }
}
