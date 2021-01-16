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

package de.kaiserpfalzedv.rpg.bot.discord;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * The plugin for all Discord plugins. The plugin has to create the answer and send it.
 * This is a "fire and forget" interface.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
public interface DiscordPlugin {
    /**
     * The command execution of this plugin. All plugins get all events and have to decide to react on it or not.
     *
     * @param event The event to work on.
     * @throws DiscordPluginException If any problem occurred.
     */
    void work(MessageReceivedEvent event) throws DiscordPluginException;
}
