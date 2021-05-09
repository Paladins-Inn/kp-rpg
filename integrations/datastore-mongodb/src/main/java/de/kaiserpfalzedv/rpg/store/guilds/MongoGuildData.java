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

package de.kaiserpfalzedv.rpg.store.guilds;

import de.kaiserpfalzedv.rpg.integrations.discord.guilds.GuildData;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MongoGuildData -- The spec for the Guild datastore.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-01-31
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MongoGuildData {
    public String prefix;

    public List<String> adminRoles;

    public HashMap<String, String> properties;

    public MongoGuildData(final GuildData orig) {
        adminRoles = new ArrayList<>();
        properties = new HashMap<>();

        prefix = orig.getPrefix();
        if (orig.getAdminRoles() != null) {
            adminRoles.addAll(orig.getAdminRoles());
        }

        if (orig.getProperties() != null) {
            properties.putAll(orig.getProperties());
        }
    }

    public GuildData data() {
        GuildData.GuildDataBuilder result = GuildData.builder();

        if (prefix != null) result.prefix(prefix);

        result
                .adminRoles(adminRoles)
                .properties(properties);

        return result.build();
    }
}
