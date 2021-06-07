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

import de.kaiserpfalzedv.rpg.torg.foundry.FoundryMapper;
import de.kaiserpfalzedv.rpg.torg.model.items.Item;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.Dependent;

/**
 * ItemMapperFactory --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-06-05
 */
@RequiredArgsConstructor
@Dependent
@Slf4j
public class FoundryItemMapper {
    private final AttackMapper attackMapper;
    private final ArmorMapper armorMapper;
    private final GearMapper gearMapper;
    private final PerkMapper perkMapper;
    private final AttackAndArmorMapper attackAndArmorMapper;

    public Item convert(@NotNull final FoundryItem orig) {
        return getMapper(orig).convert(orig);
    }

    public FoundryMapper<FoundryItem, Item> getMapper(FoundryItem item) {
        switch (item.getType()) {
            case MELEEWEAPON: // true false true false
            case FIREARM:
            case HEAVYWEAPON:
            case MISSILEWEAPON:
            case CUSTOM_ATTACK: // false true true false
                return attackMapper;

            case ARMOR: // true false false true
            case SHIELD:
                return armorMapper;

            case GEAR: // true false false false
            case IMPLANT: // true false true true
            case VEHICLE: // true false true true
                return gearMapper;

            case PERK: // ("perk", false, true, true, true),
            case SPELL: // ("spell", false, true, true, true),
            case MIRACLE: // ("miracle", false, true, true, true),
            case PSIONICPOWER: // ("psionicpower", false, true, true, true),
            case ENHANCEMENT: // ("enhancement", false, true, true, true)
                return perkMapper;

            case ETERNITYSHARD: //("eternityshard", true, true, true, true),
            case SPECIALABILITY: // ("specialability", false, true, false, true),
            case SPECIALABILITY_ROLLABLE: // ("specialability-rollable", false, true, true, false),
                return attackAndArmorMapper;

            default:
                throw new UnsupportedOperationException(String.format("Sorry, no mapper for item type '%s' found.", item.getType()));
        }
    }
}