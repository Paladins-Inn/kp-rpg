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

package de.kaiserpfalzedv.rpg.integrations.datastore.guilds;

import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildData;
import de.kaiserpfalzedv.rpg.integrations.discord.guilds.ImmutableGuildData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MongoGuildData -- The spec for the Guild datastore.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
public class MongoGuildData {
    public String prefix;

    public List<String> adminRoles;

    public HashMap<String, String> properties;


    @SuppressWarnings("unused")
    public MongoGuildData() {
    }

    public MongoGuildData(final GuildData orig) {
        adminRoles = new ArrayList<>();
        properties = new HashMap<>();

        if (orig.getPrefix().isPresent()) prefix = orig.getPrefix().get();

        if (orig.getAdminRoles() != null) adminRoles.addAll(orig.getAdminRoles());
        if (orig.getProperties() != null) properties.putAll(orig.getProperties());
    }

    public GuildData data() {
        ImmutableGuildData.Builder result = ImmutableGuildData.builder();

        if (prefix != null) result.prefix(prefix);

        result.addAllAdminRoles(adminRoles);
        result.properties(properties);

        return result.build();
    }
}
