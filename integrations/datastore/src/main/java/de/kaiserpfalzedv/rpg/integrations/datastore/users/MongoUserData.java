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

package de.kaiserpfalzedv.rpg.integrations.datastore.users;

import de.kaiserpfalzedv.rpg.core.user.ImmutableUserData;
import de.kaiserpfalzedv.rpg.core.user.UserData;
import de.kaiserpfalzedv.rpg.integrations.datastore.resources.MongoResourcePointer;

public class MongoUserData {
    public String description;
    public MongoResourcePointer picture;
    public String driveThruApiKey;

    public MongoUserData() {}

    public MongoUserData(UserData orig) {
        if (orig.getDescription().isPresent()) {
            description = orig.getDescription().get();
        }

        if (orig.getPicture().isPresent()) {
            picture = new MongoResourcePointer(orig.getPicture().get());
        }

        if (orig.getDriveThruRPGApiKey().isPresent()) {
            driveThruApiKey = orig.getDriveThruRPGApiKey().get();
        }
    }

    public UserData data() {
        ImmutableUserData.Builder result = ImmutableUserData.builder();

        if (description != null) result.description(description);
        if (picture != null) result.picture(picture.data());
        if (driveThruApiKey != null) result.driveThruRPGApiKey(driveThruApiKey);

        return result.build();
    }
}
