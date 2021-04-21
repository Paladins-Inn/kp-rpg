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

package de.kaiserpfalzedv.rpg.core.user;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFallbackUserStoreProvider {
    private final UserStoreService sut;

    public TestFallbackUserStoreProvider() {
        this.sut = new FallbackUserStoreProvider().memoryGuildStore();
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestMemoryUserStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @Test
    public void shouldReturnMemoryUserStore() {
        MDC.put("test", "retrieve-memory-user-store");

        assertTrue(sut instanceof MemoryUserStore);
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }
}
