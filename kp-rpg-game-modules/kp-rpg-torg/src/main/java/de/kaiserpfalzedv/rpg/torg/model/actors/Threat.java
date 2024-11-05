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

import de.kaiserpfalzedv.rpg.torg.model.core.Armor;
import de.kaiserpfalzedv.rpg.torg.model.core.Attack;
import de.kaiserpfalzedv.rpg.torg.model.core.Cosm;
import de.kaiserpfalzedv.rpg.torg.model.items.Item;
import de.kaiserpfalzedv.rpg.torg.model.perks.Perk;
import de.kaiserpfalzedv.rpg.torg.model.perks.magic.Spell;
import de.kaiserpfalzedv.rpg.torg.model.perks.psionic.PsiPower;

import java.util.Set;

/**
 * Threat --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.3.0  2021-06-05
 */
public interface Threat {
    String getName();

    String getDescription();

    Cosm getCosm();

    Set<AttributeValue> getAttributes();

    Other getOther();

    Possibility getPossibilities();

    Set<SkillValue> getSkills();

    Set<Attack> getAttacks();

    Set<Armor> getArmor();

    Set<Perk> getPerks();

    Set<Spell> getSpells();

    Set<PsiPower> getPsiPowers();

    Set<Item> getGear();
}
