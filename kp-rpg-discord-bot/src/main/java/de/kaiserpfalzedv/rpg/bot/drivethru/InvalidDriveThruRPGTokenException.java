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

package de.kaiserpfalzedv.rpg.bot.drivethru;

import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPlugin;
import de.kaiserpfalzedv.rpg.integrations.discord.DiscordPluginException;

/**
 * InvalidDriveThruRPGTokenException -- The API KEY given is no valid DriveThruRPG api key.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-04
 */
public class InvalidDriveThruRPGTokenException extends DiscordPluginException {
    final String apiKey;

    /**
     * @param plugin The plugin throwing this exception.
     * @param apiKey The invalid API KEY.
     */
    public InvalidDriveThruRPGTokenException(final DiscordPlugin plugin, final String apiKey) {
        super(plugin, String.format("Invalid DriveThruRPG api-key: '%s'", apiKey));

        this.apiKey = apiKey;
    }

    /**
     * @return the invalid api key.
     */
    public String getInvalidApiKey() {
        return apiKey;
    }
}