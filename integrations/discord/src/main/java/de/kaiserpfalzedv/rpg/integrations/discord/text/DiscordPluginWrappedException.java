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

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * This exception wraps another exception for using in {@link DiscordTextChannelPlugin}.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
public class DiscordPluginWrappedException extends DiscordPluginException {
    final Throwable wrapped;

    public DiscordPluginWrappedException(final DiscordPlugin plugin, final Throwable wrapped) {
        super(plugin, String.format("%s (wrapped)", wrapped.getMessage()));
        this.wrapped = wrapped;
    }

    @Override
    public String getMessage() {
        return wrapped.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return wrapped.getLocalizedMessage();
    }

    @Override
    public Throwable getCause() {
        return wrapped.getCause();
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }

    @Override
    public void printStackTrace() {
        wrapped.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        wrapped.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        wrapped.printStackTrace(s);
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return wrapped.getStackTrace();
    }
}
