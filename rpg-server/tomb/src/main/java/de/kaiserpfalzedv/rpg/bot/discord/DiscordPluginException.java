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

/**
 * This is an exception thrown by a discord plugin. All exceptions thrown by any {@link DiscordPlugin} should be at
 * least wrapped in {@link DiscordPluginWrappedException}.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
public class DiscordPluginException extends Exception {
    public DiscordPluginException() {
        super("Developer too lazy to use a meaningful exception");
    }

    public DiscordPluginException(final String message) {
        super(message);
    }

    public DiscordPluginException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DiscordPluginException(final Throwable cause) {
        super(cause);
    }


    public DiscordPluginException(final String message, final Throwable cause, final boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
