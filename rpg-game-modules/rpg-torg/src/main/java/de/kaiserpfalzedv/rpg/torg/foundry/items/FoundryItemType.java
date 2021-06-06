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

package de.kaiserpfalzedv.rpg.torg.foundry.items;

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
public enum FoundryItemType {
    MELEEWEAPON("meleeweapon", true, false, true, false),
    FIREARM("firearm", true, false, true, false),
    HEAVYWEAPON("heavyweapon", true, false, true, false),
    MISSILEWEAPON("missileweapon", true, false, true, false),
    ARMOR("armor", true, false, false, true),
    SHIELD("shield", true, false, false, true),
    CUSTOM_ATTACK("customAttack", false, true, true, false),

    GEAR("gear", true, false, false, false),
    IMPLANT("implant", true, false, true, true),
    VEHICLE("vehicle", true, false, true, true),

    ETERNITYSHARD("eternityshard", true, true, true, true),

    PERK("perk", false, true, true, true),
    SPECIALABILITY("specialability", false, true, false, true),
    SPECIALABILITY_ROLLABLE("specialability-rollable", false, true, true, false),
    SPELL("spell", false, true, true, true),
    MIRACLE("miracle", false, true, true, true),
    PSIONICPOWER("psionicpower", false, true, true, true),
    ENHANCEMENT("enhancement", false, true, true, true),
    ;

    /**
     * The name of the item type as used within Foundry VTT json.
     */
    @ToString.Include
    @JsonValue
    private final String title;

    /**
     * If this is gear.
     */
    @ToString.Include
    private final boolean gear;
    /**
     * If this is a power or special gear with power (like eternity shards).
     */
    @ToString.Include
    private final boolean power;

    /**
     * If this item could include an attack.
     */
    private final boolean attack;

    /**
     * If this item could include an armor.
     */
    private final boolean armor;

    /**
     * A set of all possible types.
     */
    public Set<FoundryItemType> allTypes() {
        return Set.of(
                MELEEWEAPON, FIREARM, HEAVYWEAPON,
                ARMOR, SHIELD,
                GEAR, IMPLANT, VEHICLE,
                ETERNITYSHARD,
                PERK, SPECIALABILITY, SPECIALABILITY_ROLLABLE, SPELL, MIRACLE, PSIONICPOWER
        );
    }
}
