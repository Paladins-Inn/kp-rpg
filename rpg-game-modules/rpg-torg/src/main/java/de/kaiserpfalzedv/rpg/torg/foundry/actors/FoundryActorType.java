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

package de.kaiserpfalzedv.rpg.torg.foundry.actors;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

/**
 * FoundryItemType -- The different item types as defined by the Foundry VTT Roll20 implementation.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-06-05
 */
@AllArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
public enum FoundryActorType {
    STORMKNIGHT("meleeweapon"),
    THREAT("firearm"),
    ;

    /**
     * The name of the item type as used within Foundry VTT json.
     */
    @ToString.Include
    @JsonValue
    private final String title;

    /**
     * A set of all possible types.
     */
    public Set<FoundryActorType> allTypes() {
        return Set.of(
                STORMKNIGHT, THREAT
        );
    }
}
