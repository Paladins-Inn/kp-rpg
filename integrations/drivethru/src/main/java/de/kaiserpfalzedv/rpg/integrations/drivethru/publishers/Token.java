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

package de.kaiserpfalzedv.rpg.integrations.drivethru.publishers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.integrations.drivethru.resource.DriveThruResource;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.beans.Transient;
import java.time.LocalDateTime;

@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableToken.class)
@JsonDeserialize(builder = ImmutableToken.Builder.class)
@Schema(name = "DriveThruRPGToken", description = "The access token for DriveThruRPG.")
public interface Token extends DriveThruResource {
    @JsonProperty("access_token")
    String getAccessToken();

    @Transient
    @JsonIgnore
    @Value.Default
    default String getBearerToken() {
        return "Bearer " + getAccessToken();
    }


    @JsonProperty("customers_id")
    String getCustomerId();

    @JsonProperty("expires")
    LocalDateTime getExpireTime();

    @JsonProperty("server_time")
    LocalDateTime getServerTime();

    @JsonIgnore
    LocalDateTime getLocalTime();

    @JsonIgnore
    Long getExpires();
}
