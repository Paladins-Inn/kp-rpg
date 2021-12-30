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

package de.kaiserpfalzedv.rpg.torg.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Set;

/**
 * AttackData -- The data for all CharSheet attacks (weapons, powers, ...).
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@Getter
@ToString
@JsonDeserialize(builder = Attack.AttackBuilder.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Attack {
    @Schema(description = "Name of the attack.", minLength = 5, maxLength = 50)
    private final String name;

    @Schema(description = "Up to 2 axioms for this attack.", minItems = 1, maxItems = 2)
    private final Set<Axiom> axioms;

    @Schema(description = "Minimal strength to use this attack.", nullable = true, minimum = "1", maximum = "30")
    private final Integer minStr;

    @Schema(description = "The damage dealt by this attack.")
    private final Damage damage;

    @Schema(description = "Ammunition in a ranged weapon (e.g. 1 for a bow or 6 for a colt.", nullable = true)
    private final Integer ammunition;

    @Schema(description = "The armor piercing value of this attack.", nullable = true, minimum = "0")
    private final Integer ap;

    @Schema(description = "The range of the attack.", nullable = true, minLength = 1, maxLength = 20)
    private final String range;

    @Schema(description = "The skill used for this attack.")
    private final Skill skill;
    private final Integer skillLevel;

    @Schema(description = "The attack is a power and not an item.", nullable = true)
    private final AttackPower power;

    private final String attackNote;
    private final String damageNote;
}
