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

import javax.enterprise.inject.Instance;
import java.util.ArrayList;
import java.util.List;


/**
 * DriveThruRPGPluginProvder -- a generic provider for DiscordPluginCommands.
 * <p>
 * Just subclass it and boom ...
 *
 * @param <T> The commands to be provided.
 */
public abstract class AbstractDiscordPluginCommandsProvider<T extends DiscordPluginCommand> {
    /**
     * The injected commands.
     */
    private final Instance<T> rawCommands;

    public AbstractDiscordPluginCommandsProvider(
            final Instance<T> rawCommands
    ) {
        this.rawCommands = rawCommands;
    }

    /**
     * Need to call this from your concrete class ...
     *
     * @return a list of the plugins.
     */
    protected List<T> producer() {
        ArrayList<T> result = new ArrayList<>();

        rawCommands.forEach(result::add);

        return result;
    }
}
