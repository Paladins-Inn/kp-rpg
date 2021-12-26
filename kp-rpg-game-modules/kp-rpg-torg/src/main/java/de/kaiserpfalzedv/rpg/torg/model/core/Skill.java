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

import de.kaiserpfalzedv.rpg.torg.model.MapperEnum;
import de.kaiserpfalzedv.rpg.torg.model.actors.Attribute;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.kaiserpfalzedv.rpg.torg.model.actors.Attribute.*;

/**
 * SkillType -- The skills defined in Torg.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.2.0  2021-05-23
 */
@AllArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
public enum Skill implements MapperEnum<Skill> {
    ENERGY_WEAPONS(DEXTERITY, "attr_EnergyWeapons", "energyWeapons"),
    FIRE_COMBAT(DEXTERITY, "attr_FireCombat", "fireCombat"),
    HEAVY_WEAPONS(DEXTERITY, "attr_HeavyWeapons", "heavyWeapons"),
    MELEE_WEAPONS(DEXTERITY, "attr_MeleeWeapons", "meleeWeapons"),
    MISSILE_WEAPONS(DEXTERITY, "attr_MissileWeapons", "missileWeapons"),
    UNARMED_COMBAT(DEXTERITY, "attr_UnarmedCombat", "unarmedCombat"),

    INTIMIDATION(SPIRIT, "attr_Intimidate", "intimidation"),
    MANEUVER(DEXTERITY, "attr_Maneuver", "maneuver"),
    TAUNT(CHARISMA, "attr_Taunt", "taunt"),
    TRICK(MIND, "attr_Trick", "trick"),

    AIR_VEHICLES(DEXTERITY, "attr_AirVehicles", "airVehicles"),
    ALTERATION(MIND, "attr_Alteration", "alteration"),
    APPORTATION(SPIRIT, "attr_Apportation", "apportation"),
    BEAST_RIDING(DEXTERITY, "attr_BeastRiding", "beastRiding"),
    COMPUTERS(MIND, "attr_Computers", "computers"),
    CONJURATION(SPIRIT, "attr_Conjuration", "conjuration"),
    DIVINATION(MIND, "attr_Divination", "divination"),
    DODGE(DEXTERITY, "attr_Dodge", "dodge"),
    EVIDENCE_ANALYSIS(MIND, "attr_EvidenceAnalysis", "evidenceAnalysis"),
    FAITH(SPIRIT, "attr_Faith", "faith"),
    FIND(MIND, "attr_Find", "find"),
    FIRST_AID(MIND, "attr_FirstAid", "firstAid"),
    KINESIS(SPIRIT, "attr_Kinesis", "kinesis"),
    LAND_VEHICLES(DEXTERITY, "attr_LandVehicles", "landVehicles"),
    LANGUAGE(MIND, "language", "language"),
    LOCKPICKING(DEXTERITY, "attr_Lockpicking", "lockpicking"),
    MEDICINE(MIND, "attr_Medicine", "medicine"),
    PERSUASION(CHARISMA, "attr_Persuasion", "persuasion"),
    PRECOGNITION(MIND, "attr_Precognition", "precognition"),
    PROFESSION(MIND, "attr_Profession", "profession"),
    REALITY(SPIRIT, "attr_Reality", "reality"),
    SCHOLAR(MIND, "attr_Scholar", "scholar"),
    SCIENCE(MIND, "attr_Science", "science"),
    STEALTH(DEXTERITY, "attr_Stealth", "stealth"),
    STREETWISE(CHARISMA, "attr_Streetwise", "streetwise"),
    SURVIVAL(MIND, "attr_Survival", "survival"),
    TELEPATHY(CHARISMA, "attr_Telepathy", "telepathy"),
    TRACKING(MIND, "attr_Tracking", "tracking"),
    WATER_VEHICLES(DEXTERITY, "attr_WaterVehicles", "waterVehicles"),
    WILLPOWER(SPIRIT, "attr_Willpower", "willpower"),
    ;

    private final Attribute attribute;
    private final String roll20;
    private final String foundry;

    public static Optional<Skill> mapFoundry(@NotNull final String name) {
        return REALITY.mapFromFoundry(name);
    }

    public Optional<Skill> mapFromFoundry(@NotNull final String name) {
        return Optional.ofNullable(
                allSkills().stream()
                        .filter(e -> e.foundry.equals(name)).distinct()
                        .collect(Collectors.toList()).get(0)
        );
    }

    public Optional<Skill> mapFromRoll20(@NotNull final String name) {
        return Optional.ofNullable(
                allSkills().stream()
                        .filter(e -> e.roll20.equals(name)).distinct()
                        .collect(Collectors.toList()).get(0)
        );
    }

    public List<Skill> allSkills() {
        return Stream.of(combatSkills(), interactionAttacks(), otherSkills())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<Skill> combatSkills() {
        return List.of(
                ENERGY_WEAPONS,
                FIRE_COMBAT,
                HEAVY_WEAPONS,
                MELEE_WEAPONS,
                MISSILE_WEAPONS,
                UNARMED_COMBAT
        );
    }

    public List<Skill> interactionAttacks() {
        return List.of(
                INTIMIDATION,
                MANEUVER,
                TAUNT,
                TRICK
        );
    }

    public List<Skill> otherSkills() {
        return List.of(
                AIR_VEHICLES,
                ALTERATION,
                APPORTATION,
                BEAST_RIDING,
                COMPUTERS,
                CONJURATION,
                DIVINATION,
                DODGE,
                EVIDENCE_ANALYSIS,
                FAITH,
                FIND,
                FIRST_AID,
                KINESIS,
                LAND_VEHICLES,
                LANGUAGE,
                LOCKPICKING,
                MEDICINE,
                PERSUASION,
                PRECOGNITION,
                PROFESSION,
                REALITY,
                SCHOLAR,
                SCIENCE,
                STEALTH,
                STREETWISE,
                SURVIVAL,
                TELEPATHY,
                TRACKING,
                WATER_VEHICLES,
                WILLPOWER
        );
    }
}