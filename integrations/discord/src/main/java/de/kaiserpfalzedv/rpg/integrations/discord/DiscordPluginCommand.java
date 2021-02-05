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

/**
 * A single command of a plugin. For your plugin define a marker interface from this and let inject the matching
 * classes into your plugin.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-04
 */
public interface DiscordPluginCommand {
    /**
     * @return The command.
     */
    String getCommand();

    /**
     * @param input The input message.
     * @return returns the argument (without the command).
     */
    default String getArgument(final String input) {
        return input.replaceFirst(getCommand() + "\\s+", "");
    }

    /**
     * @return the help line for this command.
     */
    default String getHelp() {
        return formatHelp("sorry, no help defined.");
    }

    /**
     * Formats the help line if you overwrite {@link #getHelp()} you should use this helper method.
     *
     * @param description The line describing the command.
     * @return the formatted line.
     */
    default String formatHelp(final String description) {
        return String.format("%10s %s", getCommand(), description);
    }

    /**
     * Executes with the message (stripped of the prefixes and the command).
     *
     * @param context The context to run the command.
     * @throws DiscordPluginException If there is a problem.
     */
    void execute(final DiscordPluginContext context) throws DiscordPluginException;
}
