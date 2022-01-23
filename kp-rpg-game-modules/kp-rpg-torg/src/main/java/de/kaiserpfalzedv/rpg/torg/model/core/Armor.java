/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package de.kaiserpfalzedv.rpg.torg.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Set;

/**
 * Armor -- The data for all CharSheet armors (weapons, powers, ...).
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Armor {
    @Schema(description = "Name of the armor.", minLength = 5, maxLength = 50)
    private final String name;

    @Schema(description = "Axiom of this armor.", minItems = 1, maxItems = 1)
    private final Set<Axiom> axioms;

    @Schema(description = "This armor limits the dexterity to this value.", nullable = true, minimum = "1", maximum = "30")
    private final Integer maxDex;

    @Schema(description = "The armor bonus.")
    private final Integer bonus;

    @Schema(description = "If the use of this armor will fatigue the wearer of this armor.")
    @Builder.Default
    private final Boolean fatigues = false;
}
