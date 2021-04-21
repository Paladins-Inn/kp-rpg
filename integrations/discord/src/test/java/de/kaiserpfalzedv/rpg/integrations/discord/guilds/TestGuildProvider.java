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

package de.kaiserpfalzedv.rpg.integrations.discord.guilds;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Checks the guild provider.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-02
 */
public class TestGuildProvider {
    private static final Logger LOG = LoggerFactory.getLogger(TestGuildProvider.class);

    /**
     * service under test.
     */
    private final GuildProvider sut;

    @Inject
    public TestGuildProvider() {
        sut = new GuildProvider(new FallbackGuildStoreProvider().memoryGuildStore());
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestMemoryGuildStore.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @Test
    public void shouldCreateANewGuildWhenGuildIsNotFound() {
        MDC.put("test", "create-new-guild");

        Guild result = sut.retrieve("test");
        LOG.trace("result={}", result);

        assertEquals("test", result.getName());
    }

    @Test
    public void shouldLoadTheGuildWhenGuildAlreadyExists() {
        MDC.put("test", "load-guild");

        // create the guild.
        Guild created = sut.retrieve("new-guild");

        // need to save it to invalidate the cache.
        Guild saved = ImmutableGuild.builder()
                .from(created)
                .spec(
                        ImmutableGuildData.builder()
                                .putProperties("test-property", "data")
                                .build()
                )
                .build();
        sut.store(saved);

        // load the guild from the repository.
        Guild result = sut.refreshCache("new-guild");
        LOG.trace("result={}", result);

        assertEquals(created.getUid(), result.getUid());
        assertEquals(created.getDisplayName(), result.getDisplayName());

        GuildData data = result.getSpec().orElseThrow();
        assertEquals("data", data.getProperty("test-property").orElseThrow());
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }

}
