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
import java.util.Set;

/**
 * Skills -- The list of skills of an actor.
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 1.3.0  2021-06-04
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = Skills.SkillsBuilder.class)
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Skills {
    private SkillValue airVehicles;
    private SkillValue alteration;
    private SkillValue apportation;
    private SkillValue beastRiding;
    private SkillValue computers;
    private SkillValue conjuration;
    private SkillValue divination;
    private SkillValue dodge;
    private SkillValue energyWeapons;
    private SkillValue evidenceAnalysis;
    private SkillValue faith;
    private SkillValue find;
    private SkillValue fireCombat;
    private SkillValue firstAid;
    private SkillValue heavyWeapons;
    private SkillValue intimidation;
    private SkillValue kinesis;
    private SkillValue landVehicles;
    private SkillValue language;
    private SkillValue lockpicking;
    private SkillValue maneuver;
    private SkillValue medicine;
    private SkillValue meleeWeapons;
    private SkillValue missileWeapons;
    private SkillValue persuasion;
    private SkillValue precognition;
    private SkillValue profession;
    private SkillValue reality;
    private SkillValue scholar;
    private SkillValue science;
    private SkillValue stealth;
    private SkillValue streetwise;
    private SkillValue survival;
    private SkillValue taunt;
    private SkillValue telepathy;
    private SkillValue tracking;
    private SkillValue trick;
    private SkillValue unarmedCombat;
    private SkillValue waterVehicles;
    private SkillValue willpower;

    @JsonIgnore
    @BsonIgnore
    @Transient
    public Set<SkillValue> getSkills() {
        return Set.of(
                SkillValue.builder().withName(Skill.AIR_VEHICLES).withAdds(airVehicles.getAdds()).withValue(airVehicles.getValue()).build(),
                SkillValue.builder().withName(Skill.ALTERATION).withAdds(alteration.getAdds()).withValue(alteration.getValue()).build(),
                SkillValue.builder().withName(Skill.APPORTATION).withAdds(apportation.getAdds()).withValue(apportation.getValue()).build(),
                SkillValue.builder().withName(Skill.BEAST_RIDING).withAdds(beastRiding.getAdds()).withValue(beastRiding.getValue()).build(),
                SkillValue.builder().withName(Skill.COMPUTERS).withAdds(computers.getAdds()).withValue(computers.getValue()).build(),
                SkillValue.builder().withName(Skill.CONJURATION).withAdds(conjuration.getAdds()).withValue(conjuration.getValue()).build(),
                SkillValue.builder().withName(Skill.DIVINATION).withAdds(divination.getAdds()).withValue(divination.getValue()).build(),
                SkillValue.builder().withName(Skill.DODGE).withAdds(dodge.getAdds()).withValue(dodge.getValue()).build(),
                SkillValue.builder().withName(Skill.ENERGY_WEAPONS).withAdds(energyWeapons.getAdds()).withValue(energyWeapons.getValue()).build(),
                SkillValue.builder().withName(Skill.EVIDENCE_ANALYSIS).withAdds(evidenceAnalysis.getAdds()).withValue(evidenceAnalysis.getValue()).build(),
                SkillValue.builder().withName(Skill.FAITH).withAdds(faith.getAdds()).withValue(faith.getValue()).build(),
                SkillValue.builder().withName(Skill.FIND).withAdds(find.getAdds()).withValue(find.getValue()).build(),
                SkillValue.builder().withName(Skill.FIRE_COMBAT).withAdds(fireCombat.getAdds()).withValue(fireCombat.getValue()).build(),
                SkillValue.builder().withName(Skill.FIRST_AID).withAdds(firstAid.getAdds()).withValue(firstAid.getValue()).build(),
                SkillValue.builder().withName(Skill.HEAVY_WEAPONS).withAdds(heavyWeapons.getAdds()).withValue(heavyWeapons.getValue()).build(),
                SkillValue.builder().withName(Skill.INTIMIDATION).withAdds(intimidation.getAdds()).withValue(intimidation.getValue()).build(),
                SkillValue.builder().withName(Skill.KINESIS).withAdds(kinesis.getAdds()).withValue(kinesis.getValue()).build(),
                SkillValue.builder().withName(Skill.LAND_VEHICLES).withAdds(landVehicles.getAdds()).withValue(landVehicles.getValue()).build(),
                SkillValue.builder().withName(Skill.LANGUAGE).withAdds(language.getAdds()).withValue(language.getValue()).build(),
                SkillValue.builder().withName(Skill.LOCKPICKING).withAdds(lockpicking.getAdds()).withValue(lockpicking.getValue()).build(),
                SkillValue.builder().withName(Skill.MANEUVER).withAdds(maneuver.getAdds()).withValue(maneuver.getValue()).build(),
                SkillValue.builder().withName(Skill.MEDICINE).withAdds(medicine.getAdds()).withValue(medicine.getValue()).build(),
                SkillValue.builder().withName(Skill.MELEE_WEAPONS).withAdds(meleeWeapons.getAdds()).withValue(meleeWeapons.getValue()).build(),
                SkillValue.builder().withName(Skill.MISSILE_WEAPONS).withAdds(missileWeapons.getAdds()).withValue(missileWeapons.getValue()).build(),
                SkillValue.builder().withName(Skill.PERSUASION).withAdds(persuasion.getAdds()).withValue(persuasion.getValue()).build(),
                SkillValue.builder().withName(Skill.PRECOGNITION).withAdds(precognition.getAdds()).withValue(precognition.getValue()).build(),
                SkillValue.builder().withName(Skill.PROFESSION).withAdds(profession.getAdds()).withValue(profession.getValue()).build(),
                SkillValue.builder().withName(Skill.REALITY).withAdds(reality.getAdds()).withValue(reality.getValue()).build(),
                SkillValue.builder().withName(Skill.SCHOLAR).withAdds(scholar.getAdds()).withValue(scholar.getValue()).build(),
                SkillValue.builder().withName(Skill.SCIENCE).withAdds(science.getAdds()).withValue(science.getValue()).build(),
                SkillValue.builder().withName(Skill.STEALTH).withAdds(stealth.getAdds()).withValue(stealth.getValue()).build(),
                SkillValue.builder().withName(Skill.STREETWISE).withAdds(streetwise.getAdds()).withValue(streetwise.getValue()).build(),
                SkillValue.builder().withName(Skill.SURVIVAL).withAdds(survival.getAdds()).withValue(survival.getValue()).build(),
                SkillValue.builder().withName(Skill.TAUNT).withAdds(taunt.getAdds()).withValue(taunt.getValue()).build(),
                SkillValue.builder().withName(Skill.TELEPATHY).withAdds(telepathy.getAdds()).withValue(telepathy.getValue()).build(),
                SkillValue.builder().withName(Skill.TRACKING).withAdds(tracking.getAdds()).withValue(tracking.getValue()).build(),
                SkillValue.builder().withName(Skill.TRICK).withAdds(trick.getAdds()).withValue(trick.getValue()).build(),
                SkillValue.builder().withName(Skill.UNARMED_COMBAT).withAdds(unarmedCombat.getAdds()).withValue(unarmedCombat.getValue()).build(),
                SkillValue.builder().withName(Skill.WATER_VEHICLES).withAdds(waterVehicles.getAdds()).withValue(waterVehicles.getValue()).build(),
                SkillValue.builder().withName(Skill.WILLPOWER).withAdds(willpower.getAdds()).withValue(willpower.getValue()).build()
        );


    }
}
