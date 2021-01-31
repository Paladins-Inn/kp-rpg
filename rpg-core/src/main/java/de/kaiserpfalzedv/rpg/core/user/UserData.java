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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.core.resources.ResourcePointer;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.io.Serializable;
import java.util.Optional;

/**
 * The basic data for every card.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.0.0 2021-01-06
 */
@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableUserData.class)
@JsonDeserialize(builder = ImmutableUserData.Builder.class)
@Schema(name="userData", description = "Registered User.")
public interface UserData extends Serializable {
    @Schema(name = "picture", description="The resource address of the picture of this card.")
    Optional<ResourcePointer> getPicture();

    @Schema(name = "description", description = "A description of the card.")
    Optional<String> getDescription();

    @Schema(name = "driveThruRPGApiKey", description = "The API Key for DriveThruRPG.")
    Optional<String> getDriveThruRPGApiKey();
}
