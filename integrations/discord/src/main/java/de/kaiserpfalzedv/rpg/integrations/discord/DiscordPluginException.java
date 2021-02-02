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

package de.kaiserpfalzedv.rpg.integrations.discord;

import de.kaiserpfalzedv.rpg.integrations.discord.text.DiscordPluginWrappedException;
import de.kaiserpfalzedv.rpg.integrations.discord.text.DiscordTextChannelPlugin;

/**
 * This is an exception thrown by a discord plugin. All exceptions thrown by any {@link DiscordTextChannelPlugin} should be at
 * least wrapped in {@link DiscordPluginWrappedException}.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
public abstract class DiscordPluginException extends Exception {
    private final DiscordPlugin plugin;

    public DiscordPluginException(final DiscordPlugin plugin, final String message) {
        super(String.format("%s (Plugin: '%s')", message, plugin.getName()));

        this.plugin = plugin;
    }

    public DiscordPluginException(final DiscordPlugin plugin, final String message, final Throwable cause) {
        super(String.format("%s (Plugin: '%s')", message, plugin.getName()), cause);

        this.plugin = plugin;
    }

    public DiscordPluginException(final DiscordPlugin plugin, final Throwable cause) {
        super(String.format("%s (Plugin: '%s')", cause.getMessage(), plugin.getName()), cause);

        this.plugin = plugin;
    }


    public DiscordPluginException(final DiscordPlugin plugin, final String message, final Throwable cause, final boolean enableSuppression, boolean writableStackTrace) {
        super(String.format("%s (Plugin: '%s')", cause.getMessage(), plugin.getName()), cause, enableSuppression, writableStackTrace);

        this.plugin = plugin;
    }

    public DiscordPlugin getPlugin() {
        return plugin;
    }
}
