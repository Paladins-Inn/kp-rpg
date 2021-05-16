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
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.ChannelType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import javax.inject.Inject;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


@QuarkusTest
@QuarkusTestResource(MongoDBResource.class)
@Slf4j
public class TestListOwnedProductCommand {
    public static final String VALID_USER = "klenkes74#0355";
    public static final String INVALID_USER = "invalid#0003";
    private static final Guild GUILD = Guild.builder()
            .withMetadata(
                    ResourceMetadata.builder()
                            .withKind(Guild.KIND)
                            .withApiVersion(Guild.API_VERSION)

                            .withNamespace(Guild.DISCORD_NAMESPACE)
                            .withName("the-guild")

                            .build()
            )
            .withSpec(Optional.of(
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
                .withMetadata(
                        ResourceMetadata.builder()
                                .withKind(User.KIND)
                                .withApiVersion(User.API_VERSION)

                                .withNamespace(Guild.DISCORD_NAMESPACE)
                                .withName(VALID_USER)

                                .build()
                )
                .withSpec(Optional.of(
                        UserData.builder()
                                .withDriveThruRPGApiKey(Optional.of(VALID_DISCORD_API_KEY))
                                .build()
                ))
                .build();
        userStore.save(valid);

        DiscordPluginContext.DiscordPluginContextBuilder ctxBuilder = DiscordPluginContext.builder();

        ctxBuilder
                .withPlugin(new FakeDiscordMessageChannelPlugin())
                .withGuild(GUILD)
                .withChannel(new FakeMessageChannel("the-channel", ChannelType.TEXT))
                .withUser(new FakeUser(VALID_USER))
                .withArgument("");

        sut.execute(ctxBuilder.build());

        assertTrue(sender.isTextMessageSent());
    }

    @Test
    public void shouldThrowAnExceptionWhenTheApiKeyIsInvalid() {
        User invalid = User.builder()
                .withMetadata(
                        ResourceMetadata.builder()
                                .withKind(User.KIND)
                                .withApiVersion(User.API_VERSION)

                                .withNamespace(Guild.DISCORD_NAMESPACE)
                                .withName(INVALID_USER)

                                .withCreated(OffsetDateTime.now(Clock.systemUTC()))

                                .build()
                )
                .build();
        userStore.save(invalid);

        MDC.put("test", "load-with-invalid-api-key");
        DiscordPluginContext ctx = DiscordPluginContext.builder()
                .withPlugin(new FakeDiscordMessageChannelPlugin())
                .withGuild(GUILD)
                .withChannel(new FakeMessageChannel("the-channel", ChannelType.TEXT))
                .withUser(new FakeUser(INVALID_USER))
                .withArgument("")
                .build();

        try {
            sut.execute(ctx);

            fail("There should be an exception!");
        } catch (DiscordPluginException e) {
            assertTrue(sender.isDMSent());
        }
    }

    @AfterEach
    void tearDownEach() {
        sender.clear(); // resets the message handler
        MDC.remove("test");
    }
}