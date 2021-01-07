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

package de.kaiserpfalzedv.rpg.core.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Optional;


/**
 * A single history entry. Basic data is the timestamp, the status and the message.
 *
 * @param <T> The additional data.
 */
@Value.Immutable
@Value.Modifiable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableResourceHistory.class)
@JsonDeserialize(builder = ImmutableResourceHistory.Builder.class)
public interface ResourceHistory<T extends Serializable> extends Serializable {
    OffsetDateTime getTimeStamp();
    String getStatus();
    Optional<String> getMessage();
    Optional<ResourceAddress> getReference();
    Optional<T> getData();
}
