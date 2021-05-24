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

import de.kaiserpfalzedv.commons.core.discord.DiscordMessageHandler;
import de.kaiserpfalzedv.commons.core.user.User;
import de.kaiserpfalzedv.commons.core.user.UserData;
import de.kaiserpfalzedv.commons.core.user.UserStoreService;
import de.kaiserpfalzedv.commons.discord.DiscordPluginContext;
import de.kaiserpfalzedv.commons.discord.DiscordPluginException;
import de.kaiserpfalzedv.commons.discord.DontWorkOnDiscordEventException;
import de.kaiserpfalzedv.rpg.bot.drivethru.DriveThruRPGPluginCommand;
import de.kaiserpfalzedv.rpg.bot.drivethru.InvalidDriveThruRPGTokenException;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.ChannelType;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicReference;

/**
 * UserRegisterApiKeyCommand -- This command will register the DriveThruRPG API Key for the given user.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-04
 */
@Dependent
@ToString
@EqualsAndHashCode
@Slf4j
public class UserRegisterApiKeyCommand implements DriveThruRPGPluginCommand {
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
    @Inject
    public UserRegisterApiKeyCommand(
            final UserStoreService userStore,
            final DiscordMessageHandler sender
    ) {
        this.userStore = userStore;
        this.sender = sender;

        log.trace("Created {}: store={}, sender={}", getClass().getSimpleName(), userStore, sender);
    }

    @Override
    public void execute(final DiscordPluginContext context) throws DiscordPluginException {
        validate(context);

        User user = loadUserFromStoreOrCreateNewUser(context, userStore);

        user = addTokenToUser(user, context.getArgument());
        user = userStore.save(user);

        sender.sendDM(context.getUser(), String.format("Added the api key '%s' to your user data.", context.getArgument()));
        log.info("Added DriveThruRPG API Key: user='{}', api-key='{}'", user.getDisplayName(), context.getArgument());
    }

    private void validate(final DiscordPluginContext context) throws DiscordPluginException {
        if (!context.getArgument().matches("[0-9a-fA-F]+")) {
            throw new InvalidDriveThruRPGTokenException(context.getPlugin(), context.getArgument());
        }

        if (!ChannelType.PRIVATE.equals(context.getChannel().getType())) {
            throw new DontWorkOnDiscordEventException(context.getPlugin());
        }
    }

    private User addTokenToUser(final User user, final String apiKey) {
        AtomicReference<UserData> spec = new AtomicReference<>();

        user.getData().ifPresentOrElse(
                d -> spec.set(d.toBuilder()
                        .withDriveThruRPGKey(apiKey)
                        .build()),
                () -> spec.set(UserData.builder()
                        .withDriveThruRPGKey(apiKey)
                        .build())
        );

        return userStore.save(user.toBuilder()
                .withSpec(spec.get())
                .build());
    }


    @Override
    public String getCommand() {
        return "token";
    }


    @Override
    public String getHelp() {
        return formatHelp("<api-key> Register your api-key for DriveThruRPG");
    }
}
