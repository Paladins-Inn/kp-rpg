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
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginException;
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginNotAllowedException;
import de.kaiserpfalzedv.rpg.integrations.discord.DontWorkOnDiscordEventException;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.Guild;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * DiscordTextChannelPlugin -- A plugin working on Discord TextChannels.
 *
 * The plugin for all text channel plugins. The plugin has to create the answer and send it.
 * This is a "fire and forget" interface.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
public interface DiscordTextChannelPlugin extends DiscordPlugin {
    Logger LOG = LoggerFactory.getLogger(DiscordTextChannelPlugin.class);

    /**
     * Checks if this plugin should be executed for this event.
     *
     * @param messageReceivedEvent the event received.
     * @throws DontWorkOnDiscordEventException When the plugin does not work on this event.
     */
    @SuppressWarnings({"unused", "RedundantThrows", "RedundantSuppression"})
    default void workOn(
            @SuppressWarnings("unused") final Guild guild,
            @SuppressWarnings("unused") final MessageReceivedEvent messageReceivedEvent
    ) throws DontWorkOnDiscordEventException {
        throw new DontWorkOnDiscordEventException(this);
    }

    /**
     * Checks if this plugin should be executed for this event.
     *
     * @param messageReceivedEvent the event received.
     * @throws DontWorkOnDiscordEventException When the plugin does not work on this event.
     */
    default void workOn(
            @SuppressWarnings("unused") final Guild guild,
            @SuppressWarnings("unused") final GenericGuildMessageReactionEvent messageReceivedEvent
    ) throws DontWorkOnDiscordEventException {
        throw new DontWorkOnDiscordEventException(this);
    }

    default void checkUserPermission(
            @SuppressWarnings("unused") final Guild guild,
            final TextChannel channel, final User user) throws DiscordPluginNotAllowedException {
        List<String> requiredRoles = rolesRequired();

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

        throw new DiscordPluginNotAllowedException(
                true,
                String.format("Plugin '%s' requires any of the following roles: [%s]",
                        getClass().getSimpleName(),
                        String.join(",", requiredRoles)
                )
        );
    }

    /**
     * The command execution of this plugin. All plugins get all events and have to decide to react on it or not.
     *
     * @param event the event to work on.
     * @throws DiscordPluginException something happened.
     */
    @SuppressWarnings({"unused", "RedundantThrows", "RedundantSuppression"})
    default void work(final MessageReceivedEvent event) throws DiscordPluginException {}

    /**
     *
     * @param event the event to work on.
     * @throws DiscordPluginException something happened.
     */
    @SuppressWarnings({"unused", "RedundantThrows", "RedundantSuppression"})
    default void work(final GuildMessageReactionAddEvent event) throws DiscordPluginException {}

    /**
     *
     * @param event the event to work on.
     * @throws DiscordPluginException something happened.
     */
    @SuppressWarnings({"unused", "RedundantThrows", "RedundantSuppression"})
    default void work(final GuildMessageReactionRemoveEvent event) throws DiscordPluginException {}
}
