/*
 * Copyright (c) 2022 Kaiserpfalz EDV-Service, Roland T. Lichti.
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

package de.kaiserpfalzedv.rpg.torg.model.perks.magic;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.kaiserpfalzedv.rpg.torg.model.actors.Clearance;
import de.kaiserpfalzedv.rpg.torg.model.core.Armor;
import de.kaiserpfalzedv.rpg.torg.model.core.Attack;
import de.kaiserpfalzedv.rpg.torg.model.core.Axiom;
import de.kaiserpfalzedv.rpg.torg.model.perks.Prerequisites;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * SpellData -- Data for a single magic spell.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@Jacksonized
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = false)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(description = "The spell definition.")
public class SpellData implements Serializable {
    @Size(max = 5000, message = "The textual description must not be larger than 5000 characters.")
    @Schema(description = "A textual description of this spell.", nullable = true, maxLength = 5000)
    private final String description;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Schema(description = "The minimum clearance level for this spell.")
    private final Clearance clearance;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Schema(description = "The axioms of this spell", minItems = 1, maxItems = 2)
    private final Set<Axiom> axioms;

    @Schema(description = "Notes to this spell.", minItems = 0)
    private final Set<String> notes;

    @Schema(description = "Infos to this spell.", minItems = 0)
    private final Set<String> info;

    @Schema(description = "Prerequisites for obtaining the spell", minItems = 0)
    private final Set<Prerequisites> prerequisites;

    @Schema(description = "Possible attack (most spells are treated as attack to use the CharSheet for rolling).", minItems = 0)
    private final Set<Attack> attack;

    @Schema(description = "Possible armor (some spells provide armor depending on success levels).", minItems = 0)
    private final Set<Armor> armor;
}
