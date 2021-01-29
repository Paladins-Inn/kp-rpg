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

package de.kaiserpfalzedv.rpg.integrations.drivethru.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.core.cards.ImmutableCard;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.io.Serializable;

@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableDriveThruRPGWrapper.class)
@JsonDeserialize(builder = ImmutableDriveThruRPGWrapper.Builder.class)
@Schema(name = "DriveThruRPGWrapper", description = "The wrapper class for DriveThruRPG answers.")
public interface DriveThruRPGWrapper<T extends Serializable> extends Serializable {
    /**
     * The message itself.
     * @return Either "success" or "error"
     */
    @JsonProperty("status")
    String getStatus();

    /**
     * In case of error the error string. In case of success the result object.
     *
     * @return Either the error string or the result object.
     */
    @JsonProperty("message")
    T getMessage();
}
