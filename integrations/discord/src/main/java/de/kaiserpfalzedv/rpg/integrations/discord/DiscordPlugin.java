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

import java.util.Collections;
import java.util.List;

/**
 * DiscordPlugin -- The basic plugin for the discord handler.
 * <p>
 * The methods defined here are the most basic methods of every plugin.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0 2021-01-30
 */
public interface DiscordPlugin {
    /** The default prefix if not reset by the guild admin. */
    String DEFAULT_GUILD_PREFIX = "tb!";


    /**
     * @return The name of the plugin. Defaults to {@link Class#getSimpleName()}.
     */
    default String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Returns the roles needed for accessing this plugin. If the list is empty this command does not require any
     * special role.
     *
     * @return A list of roles the caller of this command needs to have.
     */
    default List<String> rolesRequired() {
        return Collections.emptyList();
    }

    /**
     * @return the help message for this plugin.
     */
    default String getHelpText() {
        return "";
    }
}
