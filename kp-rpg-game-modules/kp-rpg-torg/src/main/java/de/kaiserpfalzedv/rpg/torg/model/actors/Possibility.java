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

package de.kaiserpfalzedv.rpg.torg.model.actors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.kaiserpfalzedv.rpg.torg.model.core.Cosm;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.HashMap;

/**
 * Possibility --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2.0.0  2021-06-06
 */
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(description = "Possibilities and probability to have possibilities")
public class Possibility {
    @Schema(description = "Number of maximum possibilities of the actor", nullable = true)
    private final Integer possibilities;

    @Schema(description = "The probability that the actor has possibilities (need to be checked with D20).", nullable = true)
    private final Integer probability;

    @ToString.Exclude
    @Schema(description = "Different cosms have different possibilities.", nullable = true)
    @Builder.Default
    private final HashMap<Cosm, Integer> cosmPossibilities = new HashMap<>();
}
