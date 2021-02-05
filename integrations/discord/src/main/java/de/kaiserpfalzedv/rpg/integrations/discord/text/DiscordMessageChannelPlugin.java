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

package de.kaiserpfalzedv.rpg.integrations.discord.text;

import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPlugin;
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginNotAllowedException;
import de.kaiserpfalzedv.rpg.integrations.discord.DontWorkOnDiscordEventException;
import de.kaiserpfalzedv.rpg.integrations.discord.IgnoreBotsException;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * DiscordTextChannelPlugin -- A plugin working on Discord TextChannels.
 * <p>
 * The plugin for all text channel plugins. The plugin has to create the answer and send it.
 * This is a "fire and forget" interface.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
public interface DiscordMessageChannelPlugin extends DiscordPlugin {
    Logger LOG = LoggerFactory.getLogger(DiscordMessageChannelPlugin.class);

    /**
     * Executes the plugin.
     *
     * @param messageReceivedEvent the event received.
     * @throws DontWorkOnDiscordEventException When the plugin does not work on this event.
     */
    @SuppressWarnings({"unused", "RedundantThrows", "RedundantSuppression"})
    default void workOnMessage(
            @SuppressWarnings("unused") final Guild guild,
            @SuppressWarnings("unused") final MessageReceivedEvent messageReceivedEvent
    ) throws DontWorkOnDiscordEventException, DiscordPluginNotAllowedException {
        throw new DontWorkOnDiscordEventException(this);
    }

    /**
     * Executes the plugin.
     *
     * @param messageReceivedEvent the event received.
     * @throws DontWorkOnDiscordEventException When the plugin does not work on this event.
     */
    default void workOnReaction(
            @SuppressWarnings("unused") final Guild guild,
            @SuppressWarnings("unused") final GenericMessageReactionEvent messageReceivedEvent
    ) throws DontWorkOnDiscordEventException {
        throw new DontWorkOnDiscordEventException(this);
    }

    default void checkUserPermission(
            @SuppressWarnings("unused") final Guild guild,
            final TextChannel channel, final User user
    ) throws DiscordPluginNotAllowedException, IgnoreBotsException {
        List<String> requiredRoles = rolesRequired();

        checkWorkForBots(user);

        // no required roles - we may stop immediately.
        if (requiredRoles.isEmpty()) {
            LOG.trace("No required roles to check permission against.");

            return;
        }

        for (Member member : channel.getMembers()) { // for every member of the channel
            if (member.getUser().equals(user)) { // only if it is the current user
                for (Role role : member.getRoles()) { // check all roles
                    if (requiredRoles.contains(role.getName())) { // if any matches the required role
                        LOG.trace("Permitted: user={}, role={}, required={}",
                                user.getName(), role.getName(), requiredRoles);
                        return;
                    }
                }
            }
        }

        throw new DiscordPluginNotAllowedException(this, requiredRoles, true);
    }

    /**
     * Checks if the event is from a bot and throws the {@link IgnoreBotsException} if so.
     *
     * @param user The user to be checked.
     * @throws IgnoreBotsException The user is a bot.
     */
    default void checkWorkForBots(final User user) throws IgnoreBotsException {
        if (user.isBot()) {
            LOG.debug("Ignoring event from bot: bot={}", user.getName());

            throw new IgnoreBotsException(this);
        }
    }
}
