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

package de.kaiserpfalzedv.rpg.core.dice.history;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * TestFallbackRollHistoryStoreProvider -- Tests if the RollHistoryStoreService memory provider will be injected.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-05
 */
@QuarkusTest
public class TestFallbackRollHistoryStoreProvider {
    private final RollHistoryStoreService sut;

    @Inject
    public TestFallbackRollHistoryStoreProvider(final RollHistoryStoreService store) {
        this.sut = store;
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestMemoryRollHistoryStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @Test
    public void shouldReturnMemoryUserStore() {
        MDC.put("test", "retrieve-memory-user-store");

        assertNotNull(sut);
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }
}
