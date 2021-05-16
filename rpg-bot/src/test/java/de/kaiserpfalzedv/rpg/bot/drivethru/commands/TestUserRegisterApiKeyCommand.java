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

import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.core.user.UserStoreService;
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginContext;
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginException;
import de.kaiserpfalzedv.rpg.integrations.discord.fake.FakeDiscordMessageChannelPlugin;
import de.kaiserpfalzedv.rpg.integrations.discord.fake.FakeMessageChannel;
import de.kaiserpfalzedv.rpg.integrations.discord.fake.FakeUser;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildData;
import de.kaiserpfalzedv.rpg.test.mongodb.MongoDBResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import net.dv8tion.jda.api.entities.ChannelType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@QuarkusTestResource(MongoDBResource.class)
public class TestUserRegisterApiKeyCommand {
    private static final Logger LOG = LoggerFactory.getLogger(TestUserRegisterApiKeyCommand.class);
    private static final Guild GUILD = Guild.builder()
            .metadata(
                    ResourceMetadata.builder()
                            .kind(Guild.KIND)
                            .apiVersion(Guild.API_VERSION)

                            .namespace(Guild.DISCORD_NAMESPACE)
                            .name("the-guild")

                            .uid(UUID.randomUUID())
                            .generation(0L)
                            .created(OffsetDateTime.now(Clock.systemUTC()))

                            .build()
            )
            .spec(Optional.of(
                    GuildData.builder()
                            .build()
            ))
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

    @Test
    public void shouldSaveTheApiKeyWhenTheKeyIsValid() throws DiscordPluginException {
        MDC.put("test", "save-valid-api");
        DiscordPluginContext ctx = DiscordPluginContext.builder()
                .plugin(new FakeDiscordMessageChannelPlugin())
                .guild(GUILD)
                .channel(new FakeMessageChannel("the-channel", ChannelType.PRIVATE))
                .user(new FakeUser("klenkes74#0355"))
                .argument(VALID_DISCORD_API_KEY)
                .build();

        sut.execute(ctx);

        Optional<User> user = userStore.findByNameSpaceAndName(Guild.DISCORD_NAMESPACE, "klenkes74#0355");
        LOG.debug("result={}", user);

        assertTrue(user.isPresent() && user.get().getSpec().isPresent());
        assertEquals(VALID_DISCORD_API_KEY, user.get().getSpec().get().getDriveThruRPGApiKey().orElseThrow());
    }

    @Test
    public void shouldThrowAnExceptionWhenTheKeyIsInvalid() {
        MDC.put("test", "save-valid-api");
        DiscordPluginContext ctx = DiscordPluginContext.builder()
                .plugin(new FakeDiscordMessageChannelPlugin())
                .guild(GUILD)
                .channel(new FakeMessageChannel("the-channel", ChannelType.PRIVATE))
                .user(new FakeUser("klenkes74#0355"))
                .argument(INVALID_DISCORD_API_KEY)
                .build();

        try {
            sut.execute(ctx);

            fail("There should be an exception!");
        } catch (DiscordPluginException e) {
            LOG.debug("Found expected exception: '" + e.getMessage() + "'", e);
        }
    }

    @AfterEach
    void tearDownEach() {
        MDC.remove("test");
    }
}
