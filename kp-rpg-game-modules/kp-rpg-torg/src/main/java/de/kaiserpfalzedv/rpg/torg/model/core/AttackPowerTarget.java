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

package de.kaiserpfalzedv.rpg.torg.model.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * PowerTarget -- The target for attack powers on the CharSheet.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@AllArgsConstructor
@Getter
@Schema(description = "A definition of attack DNs")
public enum AttackPowerTarget {
    DefaultAttack("Default Attack", null, null, null),
    DodgeOrDexterity("Dodge or Dexterity", null, null, "Target’s dodge or Dexterity"),
    FaithOrSpirit("Faith Or Spirit", null, null, "Target’s faith or Spirit"),
    WillpowerOrMind("Willpower or Mind", null, null, "Target’s willpower or Mind"),
    WillpowerOrSpirit("Willpower or Spirit", null, null, "Target’s willpower or Spirit"),
    TargetAttribute("Target Attribute", null, null, "Target’s attribute"),
    VERY_EASY("Very Easy", 6, +4, "Very Easy (DN 6)"),
    EASY("Easy", 8, +2, "Easy (DN 8)"),
    STANDARD("Standard", 10, 0, "Standard (DN 10)"),
    STANDARD_DodgeOrDexterity("Standard; or Dodge or Dexterity", 10, 0, "Standard (DN 10); or a target’s dodge or Dexterity"),
    CHALLENGING("Challenging", 12, -2, "Challenging (DN 12)"),
    HARD("Hard", 14, -4, "Hard (DN 14)"),
    VERY_HARD("Very Hard", 16, -6, "Very Hard (DN 16)"),
    HEROIC("Heroic", 18, -8, "Heroic (DN 18)"),
    NEAR_IMPOSSIBLE("Near Impossible", 20, -10, "Near Impossible (DN 20)");

    @Schema(description = "Name of the attack type")
    private final String name;

    @Schema(description = "DN value", nullable = true)
    private final Integer dn;

    @Schema(description = "Modifier for the DN", nullable = true)
    private final Integer modifier;

    @Schema(description = "Foundry VTTs definition text", nullable = true)
    private final String foundry;

    public Optional<AttackPowerTarget> mapFromFoundry(@NotNull final String name) {
        return allAttackPowerTargets().stream()
                .filter(e -> name.equalsIgnoreCase(e.foundry)).distinct()
                .findFirst();
    }

    public List<AttackPowerTarget> allAttackPowerTargets() {
        return List.of(
                DefaultAttack,
                DodgeOrDexterity, FaithOrSpirit, WillpowerOrSpirit, WillpowerOrMind,
                TargetAttribute,
                VERY_EASY, EASY,
                STANDARD, STANDARD_DodgeOrDexterity,
                CHALLENGING, HARD, VERY_HARD, HEROIC, NEAR_IMPOSSIBLE
        );
    }
}