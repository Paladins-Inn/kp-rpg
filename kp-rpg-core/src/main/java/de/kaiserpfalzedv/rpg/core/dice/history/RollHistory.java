/*
 * Copyright (c) &today.year Kaiserpfalz EDV-Service, Roland T. Lichti
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.core.dice.history;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.kaiserpfalzedv.commons.core.resources.Resource;
import de.kaiserpfalzedv.commons.core.resources.ResourceImpl;
import de.kaiserpfalzedv.commons.core.resources.SerializableList;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * RollHistory -- The history of rolls for an user in a session.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-05
 */
@Jacksonized
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(name = "RollHistory", description = "The roll history of an user in a special channel.")
public class RollHistory extends ResourceImpl<SerializableList<RollHistoryEntry>> {
    public static String KIND = "RollHistory";
    public static String API_VERSION = "v1";

    /**
     * @return The list of history entries.
     */
    @JsonIgnore
    public List<RollHistoryEntry> getList() {
        try {
            return getData().orElseThrow();
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }
}
