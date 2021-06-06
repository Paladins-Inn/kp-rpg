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

package de.kaiserpfalzedv.rpg.torg.foundry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.kaiserpfalzedv.rpg.torg.model.actors.SkillValue;
import de.kaiserpfalzedv.rpg.torg.model.core.Skill;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.beans.Transient;
import java.util.List;

/**
 * Skills -- The list of skills of an actor.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.3.0  2021-06-04
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = FoundrySkills.FoundrySkillsBuilder.class)
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FoundrySkills {
    private FoundrySkill airVehicles;
    private FoundrySkill alteration;
    private FoundrySkill apportation;
    private FoundrySkill beastRiding;
    private FoundrySkill computers;
    private FoundrySkill conjuration;
    private FoundrySkill divination;
    private FoundrySkill dodge;
    private FoundrySkill energyWeapons;
    private FoundrySkill evidenceAnalysis;
    private FoundrySkill faith;
    private FoundrySkill find;
    private FoundrySkill fireCombat;
    private FoundrySkill firstAid;
    private FoundrySkill heavyWeapons;
    private FoundrySkill intimidation;
    private FoundrySkill kinesis;
    private FoundrySkill landVehicles;
    private FoundrySkill language;
    private FoundrySkill lockpicking;
    private FoundrySkill maneuver;
    private FoundrySkill medicine;
    private FoundrySkill meleeWeapons;
    private FoundrySkill missileWeapons;
    private FoundrySkill persuasion;
    private FoundrySkill precognition;
    private FoundrySkill profession;
    private FoundrySkill reality;
    private FoundrySkill scholar;
    private FoundrySkill science;
    private FoundrySkill stealth;
    private FoundrySkill streetwise;
    private FoundrySkill survival;
    private FoundrySkill taunt;
    private FoundrySkill telepathy;
    private FoundrySkill tracking;
    private FoundrySkill trick;
    private FoundrySkill unarmedCombat;
    private FoundrySkill waterVehicles;
    private FoundrySkill willpower;

    @JsonIgnore
    @BsonIgnore
    @Transient
    public List<SkillValue> getSkills() {
        return List.of(
                SkillValue.builder().withName(Skill.ENERGY_WEAPONS).withAdds(Integer.parseInt(energyWeapons.getAdds(), 10)).withValue(Integer.parseInt(energyWeapons.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.FIRE_COMBAT).withAdds(Integer.parseInt(fireCombat.getAdds(), 10)).withValue(Integer.parseInt(fireCombat.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.HEAVY_WEAPONS).withAdds(Integer.parseInt(heavyWeapons.getAdds(), 10)).withValue(Integer.parseInt(heavyWeapons.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.MELEE_WEAPONS).withAdds(Integer.parseInt(meleeWeapons.getAdds(), 10)).withValue(Integer.parseInt(meleeWeapons.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.MISSILE_WEAPONS).withAdds(Integer.parseInt(missileWeapons.getAdds(), 10)).withValue(Integer.parseInt(missileWeapons.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.UNARMED_COMBAT).withAdds(Integer.parseInt(unarmedCombat.getAdds(), 10)).withValue(Integer.parseInt(unarmedCombat.getValue(), 10)).build(),

                SkillValue.builder().withName(Skill.INTIMIDATION).withAdds(Integer.parseInt(intimidation.getAdds(), 10)).withValue(Integer.parseInt(intimidation.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.MANEUVER).withAdds(Integer.parseInt(maneuver.getAdds(), 10)).withValue(Integer.parseInt(maneuver.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.TAUNT).withAdds(Integer.parseInt(taunt.getAdds(), 10)).withValue(Integer.parseInt(taunt.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.TRICK).withAdds(Integer.parseInt(trick.getAdds(), 10)).withValue(Integer.parseInt(trick.getValue(), 10)).build(),

                SkillValue.builder().withName(Skill.AIR_VEHICLES).withAdds(Integer.parseInt(airVehicles.getAdds(), 10)).withValue(Integer.parseInt(airVehicles.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.ALTERATION).withAdds(Integer.parseInt(alteration.getAdds(), 10)).withValue(Integer.parseInt(alteration.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.APPORTATION).withAdds(Integer.parseInt(apportation.getAdds(), 10)).withValue(Integer.parseInt(apportation.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.BEAST_RIDING).withAdds(Integer.parseInt(beastRiding.getAdds(), 10)).withValue(Integer.parseInt(beastRiding.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.COMPUTERS).withAdds(Integer.parseInt(computers.getAdds(), 10)).withValue(Integer.parseInt(computers.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.CONJURATION).withAdds(Integer.parseInt(conjuration.getAdds(), 10)).withValue(Integer.parseInt(conjuration.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.DIVINATION).withAdds(Integer.parseInt(divination.getAdds(), 10)).withValue(Integer.parseInt(divination.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.DODGE).withAdds(Integer.parseInt(dodge.getAdds(), 10)).withValue(Integer.parseInt(dodge.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.EVIDENCE_ANALYSIS).withAdds(Integer.parseInt(evidenceAnalysis.getAdds(), 10)).withValue(Integer.parseInt(evidenceAnalysis.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.FAITH).withAdds(Integer.parseInt(faith.getAdds(), 10)).withValue(Integer.parseInt(faith.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.FIND).withAdds(Integer.parseInt(find.getAdds(), 10)).withValue(Integer.parseInt(find.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.FIRST_AID).withAdds(Integer.parseInt(firstAid.getAdds(), 10)).withValue(Integer.parseInt(firstAid.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.KINESIS).withAdds(Integer.parseInt(kinesis.getAdds(), 10)).withValue(Integer.parseInt(kinesis.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.LAND_VEHICLES).withAdds(Integer.parseInt(landVehicles.getAdds(), 10)).withValue(Integer.parseInt(landVehicles.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.LANGUAGE).withAdds(Integer.parseInt(language.getAdds(), 10)).withValue(Integer.parseInt(language.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.LOCKPICKING).withAdds(Integer.parseInt(lockpicking.getAdds(), 10)).withValue(Integer.parseInt(lockpicking.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.MEDICINE).withAdds(Integer.parseInt(medicine.getAdds(), 10)).withValue(Integer.parseInt(medicine.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.PERSUASION).withAdds(Integer.parseInt(persuasion.getAdds(), 10)).withValue(Integer.parseInt(persuasion.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.PRECOGNITION).withAdds(Integer.parseInt(precognition.getAdds(), 10)).withValue(Integer.parseInt(precognition.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.PROFESSION).withAdds(Integer.parseInt(profession.getAdds(), 10)).withValue(Integer.parseInt(profession.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.REALITY).withAdds(Integer.parseInt(reality.getAdds(), 10)).withValue(Integer.parseInt(reality.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.SCHOLAR).withAdds(Integer.parseInt(scholar.getAdds(), 10)).withValue(Integer.parseInt(scholar.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.SCIENCE).withAdds(Integer.parseInt(science.getAdds(), 10)).withValue(Integer.parseInt(science.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.STEALTH).withAdds(Integer.parseInt(stealth.getAdds(), 10)).withValue(Integer.parseInt(stealth.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.STREETWISE).withAdds(Integer.parseInt(streetwise.getAdds(), 10)).withValue(Integer.parseInt(streetwise.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.SURVIVAL).withAdds(Integer.parseInt(survival.getAdds(), 10)).withValue(Integer.parseInt(survival.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.TELEPATHY).withAdds(Integer.parseInt(telepathy.getAdds(), 10)).withValue(Integer.parseInt(telepathy.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.TRACKING).withAdds(Integer.parseInt(tracking.getAdds(), 10)).withValue(Integer.parseInt(tracking.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.WATER_VEHICLES).withAdds(Integer.parseInt(waterVehicles.getAdds(), 10)).withValue(Integer.parseInt(waterVehicles.getValue(), 10)).build(),
                SkillValue.builder().withName(Skill.WILLPOWER).withAdds(Integer.parseInt(willpower.getAdds(), 10)).withValue(Integer.parseInt(willpower.getValue(), 10)).build()
        );


    }
}
