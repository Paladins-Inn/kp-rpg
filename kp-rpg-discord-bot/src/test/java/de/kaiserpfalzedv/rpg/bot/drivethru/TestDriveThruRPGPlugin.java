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

package de.kaiserpfalzedv.rpg.bot.drivethru;

import de.kaiserpfalzedv.commons.core.resources.Metadata;
import de.kaiserpfalzedv.commons.core.user.UserStoreService;
import de.kaiserpfalzedv.commons.discord.guilds.Guild;
import de.kaiserpfalzedv.commons.discord.guilds.GuildData;
import de.kaiserpfalzedv.commons.test.discord.*;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Test for the DriveThruRPG plugin for discord.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-05
 */
@QuarkusTest
@Slf4j
public class TestDriveThruRPGPlugin {
    /**
     * A valid api key (40 digits, hex).
     */
    private static final String VALID_DISCORD_API_KEY = "deadb3afdeadbeafdeadbeafdeadbeafdeadbeaf";
    /**
     * An invalid api key.
     */
    private static final String INVALID_DISCORD_API_KEY = "deadbeafdeadbeafd-adbeafdeadbeafdeadbeaf";

    private static final Guild GUILD = Guild.builder()
            .withKind(Guild.KIND)
            .withApiVersion(Guild.API_VERSION)
            .withNamespace(Guild.DISCORD_NAMESPACE)
            .withName("the-guild")

            .withMetadata(
                    Metadata.builder()
                            .withCreated(OffsetDateTime.now(Clock.systemUTC()))
                            .build()
            )
            .withSpec(
                    GuildData.builder()
                            .withPrefix("tb!")
                            .build()
            )
            .build();
    private static final net.dv8tion.jda.api.entities.Guild DISCORD_GUILD = new TestGuild("discord-guild");
    private static final TextChannel CHANNEL = new TestTextChannel(1L, DISCORD_GUILD, "test-channel", ChannelType.PRIVATE);
    private static final User DISCORD_USER = new TestUser(1L, "user#0001");
    /**
     * The plugin to test.
     */
    private final DriveThruRPGPlugin sut;
    private final UserStoreService userStore;

    @Inject
    public TestDriveThruRPGPlugin(
            final DriveThruRPGPlugin sut,
            final UserStoreService userStore
    ) {
        this.sut = sut;
        this.userStore = userStore;
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestDriveThruRPGPlugin.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    //@Test
    void shouldRegisterTheTokenWhenTokenIsValid() {
        MessageReceivedEvent input = new TestMessageEvent(
                1L,
                new TestMessage(
                        null,
                        CHANNEL,
                        DISCORD_USER,
                        "tb!dtr token " + VALID_DISCORD_API_KEY
                )
        );

        sut.workOnMessage(GUILD, input);

        Optional<de.kaiserpfalzedv.commons.core.user.User> user = userStore.findByNameSpaceAndName(Guild.DISCORD_NAMESPACE, DISCORD_USER.getName());
        log.debug("user={}", user);

        user.ifPresentOrElse(
                u -> u.getData().ifPresentOrElse(
                        s -> s.getDriveThruRPGApiKey().ifPresentOrElse(
                                k -> assertThat(k, is(VALID_DISCORD_API_KEY)),
                                () -> fail("No API Key defined")
                        ),
                        () -> fail("No user spec loaded.")
                ),
                () -> fail("No user found.")
        );
    }

    @Test
    void shouldThrowAnExceptionWhenApiKeyIsInvalid() {
        MessageReceivedEvent input = new TestMessageEvent(
                1L,
                new TestMessage(
                        null,
                        CHANNEL,
                        DISCORD_USER,
                        "tb!dtr token " + INVALID_DISCORD_API_KEY
                )
        );

        sut.workOnMessage(GUILD, input);

        Optional<de.kaiserpfalzedv.commons.core.user.User> user = userStore.findByNameSpaceAndName(Guild.DISCORD_NAMESPACE, DISCORD_USER.getName());

        if (user.isPresent() && user.get().getData().isPresent() && user.get().getData().get().getDriveThruRPGApiKey().isPresent()) {
            assertNotEquals(INVALID_DISCORD_API_KEY, user.orElseThrow().getData().orElseThrow().getDriveThruRPGApiKey().orElseThrow());
        }
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }
}

