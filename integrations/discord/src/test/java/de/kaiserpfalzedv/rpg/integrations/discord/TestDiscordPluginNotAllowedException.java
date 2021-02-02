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

package de.kaiserpfalzedv.rpg.integrations.discord;

import de.kaiserpfalzedv.rpg.integrations.discord.guilds.TestMemoryGuildStore;
import de.kaiserpfalzedv.rpg.integrations.discord.text.DiscordTextChannelPlugin;
import org.junit.jupiter.api.*;
import org.slf4j.MDC;

public class TestDiscordPluginNotAllowedException {
    @Test
    public void shouldGenerateAValidException() {
        DontWorkOnDiscordEventException cut = new DontWorkOnDiscordEventException(new TestPlugin());

        Assertions.assertEquals("Event not supported. (Plugin: 'TestPlugin')", cut.getMessage());
    }


    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestMemoryGuildStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    static class TestPlugin implements DiscordTextChannelPlugin {}
}
