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

package de.kaiserpfalzedv.rpg.core.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.kaiserpfalzedv.rpg.core.resources.DefaultResourceSpec;
import de.kaiserpfalzedv.rpg.core.resources.Pointer;
import de.kaiserpfalzedv.rpg.core.resources.ResourcePointer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

/**
 * The basic data for every user.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = UserData.UserDataBuilder.class)
@Schema(name = "userData", description = "Registered User.")
public class UserData extends DefaultResourceSpec {
    public static String CAMPAIGNS = "campaigns";
    public static String GAMES = "games";

    public static String[] STRUCTURED_PROPERTIES = {
            CAMPAIGNS,
            GAMES
    };

    @Builder.Default
    private final String description = null;
    @Builder.Default
    private final Pointer picture = null;

    @Schema(name = "driveThruRPGApiKey", description = "The API Key for DriveThruRPG.")
    @Builder.Default
    private final String driveThruRPGKey = null;


    @Transient
    @BsonIgnore
    @JsonIgnore
    @Override
    public String[] getDefaultProperties() {
        return STRUCTURED_PROPERTIES;
    }

    /**
     * @return The campaigns owned by this user.
     */
    @Transient
    @BsonIgnore
    @JsonIgnore
    public List<ResourcePointer> getCampaigns() {
        return getResourcePointers(CAMPAIGNS);
    }

    /**
     * @return The games owned by this user.
     */
    @Transient
    @BsonIgnore
    @JsonIgnore
    public List<ResourcePointer> getGames() {
        return getResourcePointers(GAMES);
    }

    @Transient
    @BsonIgnore
    @JsonIgnore
    public Optional<String> getDriveThruRPGApiKey() {
        return Optional.ofNullable(driveThruRPGKey);
    }
}
