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

package de.kaiserpfalzedv.rpg.torg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static de.kaiserpfalzedv.rpg.torg.model.AttributeType.*;

/**
 * SkillType -- The type of the skill
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@AllArgsConstructor
@Getter
public enum SkillType {
    AIR_VEHICLES("Air Vehicles", DEXTERITY),
    ALTERATION("Alteration", MIND),
    APPORTATION("Apportation", SPIRIT),
    BEAST_RIDING("Beast Riding", DEXTERITY),
    COMPUTERS("Computers", MIND),
    CONJURATION("Conjuration", SPIRIT),
    DIVINATION("Divination", MIND),
    DODGE("Dodge", DEXTERITY),
    ENERGY_WEAPONS("Energy Weapons", DEXTERITY),
    EVIDENCE_ANALYSIS("Evidence Analysis", MIND),
    FAITH("Faith", SPIRIT),
    FIND("Find", MIND),
    FIRE_COMBAT("Fire Combat", DEXTERITY),
    FIRST_AID("First Aid", MIND),
    HEAVY_WEAPONS("Heavy Weapons", DEXTERITY),
    INTIMIDATION("Intimidation", SPIRIT),
    KINESIS("Kinesis", SPIRIT),
    LAND_VEHICLES("Land Vehicles", DEXTERITY),
    LANGUAGE("Language", MIND),
    LOCKPICKING("Lockpicking", DEXTERITY),
    MANEUVER("Maneuver", DEXTERITY),
    MEDICINE("Medicine", MIND),
    MELEE_WEAPONS("Melee Weapons", DEXTERITY),
    MISSILE_WEAPONS("Missile Weapons", DEXTERITY),
    PERSUASION("Persuasion", CHARISMA),
    PRECOGNITION("Precognition", MIND),
    PROFESSION("Profession", MIND),
    REALITY("Reality", SPIRIT),
    SCHOLAR("Scholar", MIND),
    SCIENCE("Science", MIND),
    STEALTH("Stealth", DEXTERITY),
    STREETWISE("Streetwise", CHARISMA),
    SURVIVAL("Survival", MIND),
    TAUNT("Taunt", CHARISMA),
    TELEPATHY("Telepathy", CHARISMA),
    TRACKING("Tracking", MIND),
    TRICK("Trick", MIND),
    UNARMED_COMBAT("Unarmed Combat", DEXTERITY),
    WATER_VEHICLES("Water Vehicles", DEXTERITY),
    WILLPOWER("Willpower", SPIRIT);

    private final String name;
    private final AttributeType attribute;
}