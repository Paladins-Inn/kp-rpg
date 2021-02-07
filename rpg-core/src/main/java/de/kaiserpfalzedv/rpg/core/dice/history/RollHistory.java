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

package de.kaiserpfalzedv.rpg.core.dice.history;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.kaiserpfalzedv.rpg.core.resources.Resource;
import de.kaiserpfalzedv.rpg.core.resources.SerializableList;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.immutables.value.Value;

import java.util.List;

/**
 * RollHistory -- The history of rolls for an user in a session.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-05
 */
@Value.Immutable
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonSerialize(as = ImmutableRollHistory.class)
@JsonDeserialize(builder = ImmutableRollHistory.Builder.class)
@Schema(name = "RollHistory", description = "The roll history of an user in a special channel.")
public interface RollHistory extends Resource<SerializableList<RollHistoryEntry>> {
    String KIND = "RollHistory";
    String API_VERSION = "v1";

    /**
     * @return The list of history entries.
     */
    @Value.Default
    default List<RollHistoryEntry> getList() {
        return getSpec().orElseThrow();
    }
}
