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

import de.kaiserpfalzedv.rpg.bot.drivethru.ListOwnedProductCommand;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.core.user.UserData;
import de.kaiserpfalzedv.rpg.core.user.UserStoreService;
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginContext;
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginException;
import de.kaiserpfalzedv.rpg.integrations.discord.fake.FakeDiscordMessageChannelPlugin;
import de.kaiserpfalzedv.rpg.integrations.discord.fake.FakeMessageChannel;
import de.kaiserpfalzedv.rpg.integrations.discord.fake.FakeUser;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildData;
import de.kaiserpfalzedv.rpg.integrations.drivethru.DriveThruRPGService;
import de.kaiserpfalzedv.rpg.integrations.drivethru.DriveThruRPGServiceMock;
import de.kaiserpfalzedv.rpg.test.discord.DiscordMessageHandlerMock;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


@QuarkusTest
@QuarkusTestResource(MongoDBResource.class)
public class TestListOwnedProductCommand {
    public static final String VALID_USER = "klenkes74#0355";
    public static final String INVALID_USER = "invalid#0003";
    private static final Logger LOG = LoggerFactory.getLogger(TestListOwnedProductCommand.class);
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
     * The user store to store data into.
     */
    private final UserStoreService userStore;

    /**
     * The message handling service for discord. This mock can be checked afterwards.
     */
    private final DiscordMessageHandlerMock sender;

    /**
     * The API Key registration command.
     */
    private final ListOwnedProductCommand sut;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    public TestListOwnedProductCommand(
            final UserStoreService userStore
    ) {
        DriveThruRPGService service = new DriveThruRPGServiceMock();
        this.userStore = userStore;
        this.sender = new DiscordMessageHandlerMock();

        this.sut = new ListOwnedProductCommand(service, userStore, sender);
    }

    @BeforeAll
    static void setUp() {
        MDC.put("test-class", TestListOwnedProductCommand.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() {
        MDC.clear();
    }

    @Test
    public void shouldReturnTheOwnedProductsWhenTheApiKeyIsValid() throws DiscordPluginException {
        MDC.put("test", "load-with-valid-api-key");

        User valid = User.builder()
                .metadata(
                        ResourceMetadata.builder()
                                .kind(User.KIND)
                                .apiVersion(User.API_VERSION)

                                .namespace(Guild.DISCORD_NAMESPACE)
                                .name(VALID_USER)
                                .uid(UUID.randomUUID())

                                .created(OffsetDateTime.now(Clock.systemUTC()))

                                .build()
                )
                .spec(
                        UserData.builder()
                                .driveThruRPGApiKey(VALID_DISCORD_API_KEY)
                                .build()
                )
                .build();
        userStore.save(valid);

        DiscordPluginContext ctx = DiscordPluginContext.builder()
                .plugin(new FakeDiscordMessageChannelPlugin())
                .guild(GUILD)
                .channel(new FakeMessageChannel("the-channel", ChannelType.TEXT))
                .user(new FakeUser(VALID_USER))
                .argument("")
                .build();

        sut.execute(ctx);

        assertTrue(sender.isTextMessageSent());
    }

    @Test
    public void shouldThrowAnExceptionWhenTheApiKeyIsInvalid() {
        User invalid = User.builder()
                .metadata(
                        ResourceMetadata.builder()
                                .kind(User.KIND)
                                .apiVersion(User.API_VERSION)

                                .namespace(Guild.DISCORD_NAMESPACE)
                                .name(INVALID_USER)
                                .uid(UUID.randomUUID())

                                .created(OffsetDateTime.now(Clock.systemUTC()))

                                .build()
                )
                .build();
        userStore.save(invalid);

        MDC.put("test", "load-with-invalid-api-key");
        DiscordPluginContext ctx = DiscordPluginContext.builder()
                .plugin(new FakeDiscordMessageChannelPlugin())
                .guild(GUILD)
                .channel(new FakeMessageChannel("the-channel", ChannelType.TEXT))
                .user(new FakeUser(INVALID_USER))
                .argument("")
                .build();

        try {
            sut.execute(ctx);

            fail("There should be an exception!");
        } catch (DiscordPluginException e) {
            assertTrue(sender.isDMSent());
            LOG.debug("Found expected exception: '" + e.getMessage() + "'", e);
        }
    }

    @AfterEach
    void tearDownEach() {
        sender.clear(); // resets the message handler
        MDC.remove("test");
    }
}
