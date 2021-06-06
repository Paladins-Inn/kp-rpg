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

package de.kaiserpfalzedv.rpg.torg.model.items;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.kaiserpfalzedv.rpg.torg.model.core.Armor;
import de.kaiserpfalzedv.rpg.torg.model.core.Attack;
import de.kaiserpfalzedv.rpg.torg.model.core.Axiom;
import de.kaiserpfalzedv.rpg.torg.model.core.Cosm;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * ItemData -- Data for an item.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@SuperBuilder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@JsonDeserialize(builder = ItemData.ItemDataBuilder.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@Schema(description = "Gear and equipment.")
public class ItemData implements Serializable {
    @Size(max = 5000, message = "The textual description must not be larger than 5000 characters.")
    @Schema(description = "A textual description of this spell.", nullable = true, maxLength = 5000)
    private final String description;

    @Schema(description = "Notes to this spell.", minItems = 0)
    @Builder.Default
    private final Set<String> notes = new HashSet<>();

    @Schema(description = "Infos to this spell.", minItems = 0)
    @Builder.Default
    private final Set<String> info = new HashSet<>();

    @Schema(description = "The cosms for this gear.", minItems = 1)
    @Builder.Default
    private final Set<Cosm> cosms = new HashSet<>();

    @Schema(description = "The axioms of this spell", minItems = 1, maxItems = 2)
    @Builder.Default
    private final Set<Axiom> axioms = new HashSet<>();

    @Min(1)
    @Schema(description = "The price in $.", minimum = "1")
    private final int price = 100;

    @Min(1)
    @Schema(description = "The DN to get the equipment from the Delphi Council.", minimum = "1")
    private final int delphiDN = 10;

    @Schema(description = "Possible attack (most spells are treated as attack to use the CharSheet for rolling).", minItems = 0)
    @Builder.Default
    private final Set<Attack> attack = new HashSet<>();

    @Schema(description = "Possible armor (some spells provide armor depending on success levels).", minItems = 0)
    @Builder.Default
    private final Set<Armor> armor = new HashSet<>();
}
