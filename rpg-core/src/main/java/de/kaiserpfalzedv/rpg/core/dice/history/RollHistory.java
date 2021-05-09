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
import de.kaiserpfalzedv.rpg.core.resources.Resource;
import de.kaiserpfalzedv.rpg.core.resources.ResourceMetadata;
import de.kaiserpfalzedv.rpg.core.resources.ResourceStatus;
import de.kaiserpfalzedv.rpg.core.resources.SerializableList;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * RollHistory -- The history of rolls for an user in a session.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-02-05
 */
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(name = "RollHistory", description = "The roll history of an user in a special channel.")
public class RollHistory extends Resource<SerializableList<RollHistoryEntry>> {
    public static String KIND = "RollHistory";
    public static String API_VERSION = "v1";


    @Builder
    public RollHistory(
            @NotNull final ResourceMetadata metadata,
            final SerializableList<RollHistoryEntry> spec,
            final ResourceStatus state
    ) {
        super(metadata, Optional.ofNullable(spec), Optional.ofNullable(state));
    }

    /**
     * @return The list of history entries.
     */
    public List<RollHistoryEntry> getList() {
        try {
            return getSpec().orElseThrow();
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }
}
