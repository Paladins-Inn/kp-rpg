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

package de.kaiserpfalzedv.rpg.bot.drivethru.commands;

import de.kaiserpfalzedv.commons.core.resources.Metadata;
import de.kaiserpfalzedv.commons.core.user.User;
import de.kaiserpfalzedv.commons.core.user.UserStoreService;
import de.kaiserpfalzedv.commons.discord.DiscordPluginContext;
import de.kaiserpfalzedv.commons.discord.DiscordPluginException;
import de.kaiserpfalzedv.commons.discord.fake.FakeDiscordMessageChannelPlugin;
import de.kaiserpfalzedv.commons.discord.fake.FakeMessageChannel;
import de.kaiserpfalzedv.commons.discord.fake.FakeUser;
import de.kaiserpfalzedv.commons.discord.guilds.Guild;
import de.kaiserpfalzedv.commons.test.mongodb.MongoDBResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.ChannelType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@QuarkusTestResource(MongoDBResource.class)
@Slf4j
public class TestUserRegisterApiKeyCommand {
    private static final Guild GUILD = Guild.builder()
            .withKind(Guild.KIND)
            .withApiVersion(Guild.API_VERSION)
            .withNamespace(Guild.DISCORD_NAMESPACE)
            .withName("the-guild")

            .withMetadata(Metadata.builder().build())

            .build();

    /**
     * A valid api key (40 digits, hex).
     */
    private static final String VALID_DISCORD_API_KEY = "deadb3afdeadbeafdeadbeafdeadbeafdeadbeaf";
    /**
     * An invalid api key.
     */
    private static final String INVALID_DISCORD_API_KEY = "deadbeafdeadbeafd-adbeafdeadbeafdeadbeaf";


    /**
     * The user store to store data into.
     */
    private final UserStoreService userStore;


    /**
     * The API Key registration command.
     */
    private final UserRegisterApiKeyCommand sut;


    @Inject
    public TestUserRegisterApiKeyCommand(
            final UserRegisterApiKeyCommand sut,
            final UserStoreService userStore
    ) {
        this.sut = sut;
        this.userStore = userStore;
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestUserRegisterApiKeyCommand.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    //@Test
    public void shouldSaveTheApiKeyWhenTheKeyIsValid() throws DiscordPluginException {
        MDC.put("test", "save-valid-api");
        DiscordPluginContext ctx = DiscordPluginContext.builder()
                .withPlugin(new FakeDiscordMessageChannelPlugin())
                .withGuild(GUILD)
                .withChannel(new FakeMessageChannel("the-channel", ChannelType.PRIVATE))
                .withUser(new FakeUser("klenkes74#0355"))
                .withArgument(VALID_DISCORD_API_KEY)
                .build();

        sut.execute(ctx);

        Optional<User> user = userStore.findByNameSpaceAndName(Guild.DISCORD_NAMESPACE, "klenkes74#0355");
        log.debug("result={}", user);

        assertTrue(user.isPresent(), "No user found!");
        assertTrue(user.get().getData().isPresent(), "No spec in user!");
        assertEquals(VALID_DISCORD_API_KEY, user.get().getData().get().getDriveThruRPGApiKey().orElseThrow());
    }

    //@Test
    public void shouldThrowAnExceptionWhenTheKeyIsInvalid() {
        MDC.put("test", "save-valid-api");
        DiscordPluginContext ctx = DiscordPluginContext.builder()
                .withPlugin(new FakeDiscordMessageChannelPlugin())
                .withGuild(GUILD)
                .withChannel(new FakeMessageChannel("the-channel", ChannelType.PRIVATE))
                .withUser(new FakeUser("klenkes74#0355"))
                .withArgument(INVALID_DISCORD_API_KEY)
                .build();

        try {
            sut.execute(ctx);

            fail("There should be an exception!");
        } catch (DiscordPluginException e) {
            log.debug("Found expected exception: '" + e.getMessage() + "'", e);
        }
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }
}
