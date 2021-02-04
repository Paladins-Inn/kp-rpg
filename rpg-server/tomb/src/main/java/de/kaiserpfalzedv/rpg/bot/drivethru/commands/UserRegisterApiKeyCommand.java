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

import de.kaiserpfalzedv.rpg.bot.drivethru.DriveThruRPGPluginCommand;
import de.kaiserpfalzedv.rpg.core.resources.ImmutableResourceMetadata;
import de.kaiserpfalzedv.rpg.core.user.ImmutableUser;
import de.kaiserpfalzedv.rpg.core.user.ImmutableUserData;
import de.kaiserpfalzedv.rpg.core.user.User;
import de.kaiserpfalzedv.rpg.core.user.UserStoreService;
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginContext;
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginException;
import de.kaiserpfalzedv.rpg.integrations.discord.DontWorkOnDiscordEventException;
import de.kaiserpfalzedv.rpg.integrations.discord.text.DiscordMessageHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

import static de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild.DISCORD_NAMESPACE;

/**
 * UserRegisterApiKeyCommand -- This command will register the DriveThruRPG API Key for the given user.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-04
 */
@Dependent
public class UserRegisterApiKeyCommand implements DriveThruRPGPluginCommand {
    private static final Logger LOG = LoggerFactory.getLogger(UserRegisterApiKeyCommand.class);

    /**
     * The user store service to be used for loading/saving the user data.
     */
    private final UserStoreService userStore;
    /**
     * The discord message sender to be used for sending discord messages.
     */
    private final DiscordMessageHandler sender;

    /**
     * Creates a new UserRegisterApiKeyCommand.
     *
     * @param userStore The user store service to load/save user data from/to.
     */
    @SuppressWarnings("CdiInjectionPointsInspection")
    public UserRegisterApiKeyCommand(
            final UserStoreService userStore,
            final DiscordMessageHandler sender
    ) {
        this.userStore = userStore;
        this.sender = sender;
    }

    @Override
    public void execute(final DiscordPluginContext context) throws DiscordPluginException {
        validate(context);

        User user = loadUserFromStoreOrCreateNewUser(context);

        user = addTokenToUser(user, context.getArgument());
        user = userStore.save(user);

        sender.sendDM(context.getUser(), String.format("Added the api key '%s' to your user data.", context.getArgument()));
        LOG.info("Added DriveThruRPG API Key: user='{}', api-key='{}'", user.getDisplayName(), context.getArgument());
    }

    private void validate(final DiscordPluginContext context) throws DiscordPluginException {
        if (!context.getArgument().matches("[0-9a-fA-F]+")) {
            throw new InvalidDriveThruRPGTokenException(context.getPlugin(), context.getArgument());
        }

        if (!ChannelType.PRIVATE.equals(context.getChannel().getType())) {
            throw new DontWorkOnDiscordEventException(context.getPlugin());
        }
    }

    private User loadUserFromStoreOrCreateNewUser(DiscordPluginContext context) {
        net.dv8tion.jda.api.entities.User user = context.getUser();
        Optional<User> store = userStore.findByNameSpaceAndName(DISCORD_NAMESPACE, user.getName());
        User result;
        if (store.isEmpty()) {
            result = ImmutableUser.builder()
                    .metadata(
                            ImmutableResourceMetadata.builder()
                                    .kind(User.KIND)
                                    .apiVersion(User.API_VERSION)

                                    .namespace(DISCORD_NAMESPACE)
                                    .name(context.getUser().getName())
                                    .uid(UUID.randomUUID())
                                    .generation(0L)
                                    .created(OffsetDateTime.now(Clock.systemUTC()))

                                    .putAnnotations("discord-id", user.getId())
                                    .putAnnotations("discord-avatar-id", user.getAvatarId())
                                    .putAnnotations("discord-avatar-url", user.getEffectiveAvatarUrl())

                                    .build()
                    )
                    .build();
        } else {
            result = store.get();
        }

        return result;
    }

    private User addTokenToUser(final User user, final String apiKey) {
        return ImmutableUser.builder()
                .from(user)

                .spec(
                        ImmutableUserData.builder()
                                .driveThruRPGApiKey(apiKey)
                                .build()
                )
                .build();
    }


    @Override
    public String getCommand() {
        return "token";
    }

    @Override
    public String getHelp() {
        return formatHelp("Register your api-key for DriveThruRPG");
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", getClass().getSimpleName() + "[", "]")
                .add("identity=" + System.identityHashCode(this))
                .toString();
    }
}
