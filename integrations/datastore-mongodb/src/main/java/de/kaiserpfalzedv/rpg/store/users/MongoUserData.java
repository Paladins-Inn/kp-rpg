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

package de.kaiserpfalzedv.rpg.store.users;

import de.kaiserpfalzedv.rpg.core.user.UserData;
import de.kaiserpfalzedv.rpg.store.resources.MongoPointer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MongoUserData {
    public String description;
    public MongoPointer picture;
    public String driveThruApiKey;

    public HashMap<String, String> properties;

    public MongoUserData(@NotNull final UserData orig) {
        properties = new HashMap<>();

        if (orig != null) {
            orig.getDescription().ifPresentOrElse(
                    d -> description = d,
                    () -> description = null
            );

            if (orig.getPicture() != null)
                orig.getPicture().ifPresentOrElse(
                        p -> picture = MongoPointer.builder()
                                .kind(p.getKind())
                                .apiVersion(p.getApiVersion())
                                .namespace(p.getNamespace())
                                .name(p.getName())
                                .uid(p.getUid())
                                .build(),
                        () -> picture = null
                );

            if (orig.getDriveThruRPGApiKey() != null)
                orig.getDriveThruRPGApiKey().ifPresentOrElse(
                        k -> driveThruApiKey = k,
                        () -> driveThruApiKey = null
                );

            if (orig.getProperties() != null)
                properties.putAll(orig.getProperties());
        }
    }

    @Transient
    @BsonIgnore
    public UserData data() {
        UserData.UserDataBuilder result = UserData.builder()
                .withDescription(Optional.ofNullable(description))
                .withProperties(properties)
                .withDriveThruRPGApiKey(Optional.ofNullable(driveThruApiKey));

        if (picture != null)
            result.withPicture(Optional.ofNullable(picture.data()));

        return result.build();
    }
}
