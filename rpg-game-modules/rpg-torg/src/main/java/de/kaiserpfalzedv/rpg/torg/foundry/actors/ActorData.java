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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.kaiserpfalzedv.rpg.torg.foundry.model.*;
import de.kaiserpfalzedv.rpg.torg.model.actors.Other;
import de.kaiserpfalzedv.rpg.torg.model.core.Level;
import de.kaiserpfalzedv.rpg.torg.model.core.Xp;
import lombok.*;

/**
 * Data --
 *
 * @author klenkes74 {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2.0.0  2021-06-04
 */
@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonDeserialize(builder = ActorData.ActorDataBuilder.class)
@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ActorData {
    private Flag flags;
    private Permission permission;

    private ActorData data;

    private Level shock;
    private Level wounds;
    private Attributes attributes;
    private Other other;
    private FoundrySkills skills;
    private Details details;
    private String editstate;

    private String category;
    private String cosm;
    private String prerequisites;
    private int timestaken;
    private String description;
    private int techlevel;
    private String price;
    private int value;
    private String attackWith;
    private String damageType;
    private int damage;
    private int ap;
    private int bonus;
    private String notes;
    private int maxDex;
    private int minStrength;
    private boolean equipped;
    private String equippedClass;
    private Level ammo;
    private String range;
    private Xp xp;
    private Axioms axioms;
    private int axiom;
    private String skill;
    private int skilllevel;
    private String dn;
    private String castingtime;
    private String duration;

    private String good;
    private String outstanding;
    private boolean isAttack;

    private String topspeed;
    private int pass;
    private int mr;
    private int fatigue;
}
